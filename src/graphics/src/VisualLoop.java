package graphics.src;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import gameLogic.src.*;
import gameLogic.src.projectiles.Projectile;
import gameLogic.src.towers.ArcherTower;
import gameLogic.src.towers.Cannon;
import gameLogic.src.towers.Tower;
import graphics.src.waveFactories.*;

public class VisualLoop implements KeyListener {

    private gameLogic.src.Map loadedMap;
    private GraphicsEngine graphicsEngine;
    private List<Enemy> enemies;
    private List<Projectile> projectiles;
    private List<Tower> towers;
    private List<MovementComponent> movementComponents;
    private WaveFactory waveFactory;
    private int waveCounter = 0;
    private GameLogicLoop gameLogicLoop;

    // Visual tracking
    private final Map<Projectile, EntityVisual> projectileVisuals = new HashMap<>();
    private final Map<Enemy, EntityVisual> enemyVisuals = new HashMap<>();
    private final Map<Tower, EntityVisual> towerVisuals = new HashMap<>();

    // Spawn management
    private List<Enemy> spawnQueue = new ArrayList<>();
    private StopWatch spawnTimer = new StopWatch(0);
    private final int spawnDelay = 1000; // 1 second between spawns
    private final int initialDelay = 15000; // 15 seconds before first wave

    // Reset management
    private volatile boolean needsReset = false;

    public void setGameLogicLoop(GameLogicLoop loop) {
        this.gameLogicLoop = loop;
        if (this.gameLogicLoop != null) {
            this.gameLogicLoop.setOnEnemyKilled(this::addMoney);
            this.gameLogicLoop.setOnEnemyReachedEnd(this::removeLives);
        }
    }

    public gameLogic.src.Map getLoadedMap() {
        return loadedMap;
    }

    public boolean isInMenu() {
        return isInMenu;
    }

    public boolean isPaused() {
        return isPaused;
    }

    private volatile boolean isInMenu = false;
    private volatile boolean goMenu = false;
    private volatile boolean isPaused  = false;
    private volatile boolean hasStarted = false;
    private VisualsFactory visualsFactory;
    private int lives;
    private int money;

    public VisualLoop(GraphicsEngine engine,
                      List<Enemy> enemies,
                      List<Projectile> projectiles,
                      List<Tower> towers,
                      List<MovementComponent> movementComponents) {
        graphicsEngine = engine;
        this.enemies = enemies;
        this.projectiles = projectiles;
        this.towers = towers;
        this.movementComponents = movementComponents;

        engine.addTowerPlacedListener(this::onTowerPlaced);
        engine.registerKeyListener(this);
        visualsFactory = engine.getVisualsFactory();
        graphicsEngine.setMenuActions(this::handlePrimaryMenuAction, this::backToStartScreen, this::quitGame);
        refreshAvailableLevels();
        graphicsEngine.showStartScreen();
        graphicsEngine.setInMenu(true);
        graphicsEngine.setTowerPlacerActive(false);
        isInMenu = true;
        isPaused = true;
        graphicsEngine.startRenderTread();
    }

    public void addMoney(int amount) {
        this.money += amount;
        graphicsEngine.updateHUD(lives, money);
    }

    public void removeLives(int amount) {
        this.lives = Math.max(0, this.lives - amount);
        graphicsEngine.updateHUD(lives, money);
        if (this.lives <= 0) {
            needsReset = true;
        }
    }

    public void update(){
        if (needsReset) {
            System.out.println("GAME OVER - Resetting");
            backToStartScreen();
            needsReset = false;
            return;
        }

        if (!hasStarted) {
            graphicsEngine.setInMenu(true);
            graphicsEngine.setTowerPlacerActive(false);
            return;
        }

        if (goMenu && !isInMenu) {
            graphicsEngine.showPauseScreen();
            isInMenu = true;
            goMenu = false;
            isPaused = true;
        }

        graphicsEngine.setInMenu(isInMenu);
        graphicsEngine.setTowerPlacerActive(!isInMenu && !isPaused);
        
        if (!isInMenu && !isPaused) {
            synchronized (enemies) {
                if (enemies.isEmpty() && spawnQueue.isEmpty() && spawnTimer.isFinished()) {
                    spawnNextWave();
                }
            }
            processSpawnQueue();
            syncProjectiles();
            syncDeadEnemies();
        }
    }

    private void syncProjectiles() {
        synchronized (projectiles) {
            // Add visuals for new projectiles
            for (Projectile p : projectiles) {
                if (!projectileVisuals.containsKey(p)) {
                    EntityVisual visual = visualsFactory.createProjectileVisual(p);
                    projectileVisuals.put(p, visual);
                    graphicsEngine.addVisual(visual);
                }
            }
            // Remove visuals for finished projectiles
            List<Projectile> toRemove = new ArrayList<>();
            for (Projectile p : projectileVisuals.keySet()) {
                if (!projectiles.contains(p)) {
                    toRemove.add(p);
                }
            }
            for (Projectile p : toRemove) {
                EntityVisual visual = projectileVisuals.remove(p);
                graphicsEngine.removeVisual(visual);
            }
        }
    }

    private void syncDeadEnemies() {
        synchronized (enemies) {
            List<Enemy> toRemove = new ArrayList<>();
            for (Enemy e : enemyVisuals.keySet()) {
                if (!enemies.contains(e)) {
                    toRemove.add(e);
                }
            }
            for (Enemy e : toRemove) {
                EntityVisual visual = enemyVisuals.remove(e);
                graphicsEngine.removeVisual(visual);
            }
        }
    }

    private void spawnNextWave() {
        if (waveFactory == null || loadedMap == null) return;
        
        waveCounter++;
        System.out.println("Generating wave " + waveCounter);
        Enemy[] newEnemies = waveFactory.generateWave(waveCounter);
        synchronized (spawnQueue) {
            spawnQueue.addAll(Arrays.asList(newEnemies));
        }
    }

    private void processSpawnQueue() {
        if (spawnQueue.isEmpty() || !spawnTimer.isFinished()) return;

        Enemy e;
        synchronized (spawnQueue) {
            if (spawnQueue.isEmpty()) return;
            e = spawnQueue.remove(0);
        }

        enemies.add(e);
        synchronized (movementComponents) {
            movementComponents.add(e.getMovementComponent());
        }
        
        EntityVisual visual = visualsFactory.createEnemyVisual(e);
        enemyVisuals.put(e, visual);
        graphicsEngine.addVisual(visual);
        
        spawnTimer = new StopWatch(spawnDelay);
    }

    private void handlePrimaryMenuAction() {
        if (!hasStarted) {
            startGameFromMenu();
        } else {
            resumeFromMenu();
        }
    }

    private void startGameFromMenu() {
        String selectedLevel = graphicsEngine.getSelectedLevelResource();
        if (selectedLevel == null || selectedLevel.isBlank()) {
            return;
        }

        gameLogic.src.Map candidateMap = new gameLogic.src.Map(selectedLevel);
        if (candidateMap.getMap() == null || candidateMap.getPaths() == null) {
            System.err.println("Failed to load selected level: " + selectedLevel);
            return;
        }

        resetWorldState();
        loadedMap = candidateMap;
        if (gameLogicLoop != null) gameLogicLoop.setMap(loadedMap);

        Difficulty selectedDifficulty = graphicsEngine.getSelectedDifficulty();
        if (candidateMap.getPaths().length > 0) {
            waveFactory = WaveFactorySelector.getWaveFactory(selectedDifficulty, candidateMap.getPaths()[0]);
            System.out.println("WaveFactory selected for difficulty: " + selectedDifficulty);
        }

        money = 400;
        lives = 200;
        graphicsEngine.updateHUD(lives, money);
        graphicsEngine.addMap(loadedMap);
        hasStarted = true;
        isInMenu = false;
        isPaused = false;
        goMenu = false;
        graphicsEngine.setInMenu(false);
        graphicsEngine.setTowerPlacerActive(true);
        
        // Add the 15 second delay before spawning begins
        spawnTimer = new StopWatch(initialDelay);
    }

    private void resumeFromMenu() {
        isInMenu = false;
        isPaused = false;
        goMenu = false;
        graphicsEngine.setInMenu(false);
        graphicsEngine.setTowerPlacerActive(true);
    }

    private void backToStartScreen() {
        resetWorldState();
        hasStarted = false;
        isInMenu = true;
        isPaused = true;
        goMenu = false;
        refreshAvailableLevels();
        graphicsEngine.showStartScreen();
        graphicsEngine.setInMenu(true);
        graphicsEngine.setTowerPlacerActive(false);
    }

    private void refreshAvailableLevels() {
        graphicsEngine.setAvailableLevels(LevelCatalog.loadAvailableLevels());
    }

    private void resetWorldState() {
        synchronized (enemies) {
            synchronized (projectiles) {
                synchronized (towers) {
                    synchronized (movementComponents) {
                        synchronized (spawnQueue) {
                            loadedMap = null;
                            enemies.clear();
                            projectiles.clear();
                            towers.clear();
                            movementComponents.clear();
                            spawnQueue.clear();
                            enemyVisuals.clear();
                            projectileVisuals.clear();
                            towerVisuals.clear();
                            graphicsEngine.clearGameEntities();
                            waveCounter = 0;
                            if (gameLogicLoop != null) gameLogicLoop.setMap(null);
                            spawnTimer = new StopWatch(0);
                        }
                    }
                }
            }
        }
    }

    private void quitGame() {
        graphicsEngine.shutdown();
        System.exit(0);
    }

    private void onTowerPlaced(int tileCol, int tileRow, int pixelX, int pixelY, Towers towerType) {
        System.out.printf("Tower placed at map tile (%d, %d)%n", tileCol, tileRow);

        Tower tower = null;
        switch (towerType){
            case Archer:
                if (money >= ArcherTower.getPrice()) {
                    tower = new ArcherTower(tileCol, tileRow);
                    money -= ArcherTower.getPrice();
                }
                break;

            case Cannon:
                if (money >= Cannon.getPrice()){
                    tower = new Cannon(tileCol, tileRow);
                    money -= Cannon.getPrice();
                }
                break;
            default:
                break;
        }

        if (tower != null) {
            synchronized (towers) {
                towers.add(tower);
            }
            EntityVisual visual = visualsFactory.createTowerVisual(tower);
            towerVisuals.put(tower, visual);
            graphicsEngine.addVisual(visual);
            graphicsEngine.updateHUD(lives, money);
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {
        if (!hasStarted) return;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_ESCAPE:
            case KeyEvent.VK_P:
                if (!isInMenu) {
                    isPaused = true;
                    goMenu = true;
                }
                return;
        }
    }
}

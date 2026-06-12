package graphics.src;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import gameLogic.src.*;
import gameLogic.src.projectiles.Projectile;
import gameLogic.src.towers.ArcherTower;
import gameLogic.src.towers.Cannon;
import gameLogic.src.towers.Tower;
import graphics.src.towers.ArcherTowerGraphics;
import graphics.src.waveFactories.*;

public class VisualLoop implements KeyListener {

    private Map loadedMap;
    private GraphicsEngine graphicsEngine;
    private List<Enemy> enemies;
    private List<Projectile> projectiles ;
    private List<Tower> towers ;
    private List<MovementComponent> movementComponents;
    private WaveFactory waveFactory;
    private int waveCounter = 0;
    private GameLogicLoop gameLogicLoop;

    public void setGameLogicLoop(GameLogicLoop loop) {
        this.gameLogicLoop = loop;
    }

    public Map getLoadedMap() {
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
    private TowerFactory towerFactory;
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

        // Register the callback that actually spawns a tower entity when the
        // player clicks a valid tile.
        engine.addTowerPlacedListener(this::onTowerPlaced);
        engine.registerKeyListener(this);
        towerFactory = new GraphicsTowerFactory();
        graphicsEngine.setMenuActions(this::handlePrimaryMenuAction, this::backToStartScreen, this::quitGame);
        refreshAvailableLevels();
        graphicsEngine.showStartScreen();
        graphicsEngine.setInMenu(true);
        graphicsEngine.setTowerPlacerActive(false);
        isInMenu = true;
        isPaused = true;
        graphicsEngine.startRenderTread();
    }

    public void update(){
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
                if (enemies.isEmpty()) {
                    spawnNextWave();
                }
            }
        }
    }

    private void spawnNextWave() {
        if (waveFactory == null || loadedMap == null) return;
        
        waveCounter++;
        System.out.println("Spawning wave " + waveCounter);
        Enemy[] newEnemies = waveFactory.generateWave(waveCounter);
        
        for (Enemy e : newEnemies) {
            e.setMap(loadedMap);
            enemies.add(e);
            synchronized (movementComponents) {
                movementComponents.add(e.getMovementComponent());
            }
            graphicsEngine.addEntety(e.getGraphics());
        }
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

        Map candidateMap = new Map(selectedLevel);
        if (candidateMap.getMap() == null || candidateMap.getPaths() == null) {
            System.err.println("Failed to load selected level: " + selectedLevel);
            return;
        }

        resetWorldState();
        loadedMap = candidateMap;
        if (gameLogicLoop != null) gameLogicLoop.setMap(loadedMap);

        // Select WaveFactory based on difficulty
        Difficulty selectedDifficulty = graphicsEngine.getSelectedDifficulty();
        if (candidateMap.getPaths().length > 0) {
            waveFactory = WaveFactorySelector.getWaveFactory(selectedDifficulty, candidateMap.getPaths()[0]);
            System.out.println("WaveFactory selected for difficulty: " + selectedDifficulty);
        }

        money = 400;
        lives = 200;
        graphicsEngine.addMap(loadedMap);
        hasStarted = true;
        isInMenu = false;
        isPaused = false;
        goMenu = false;
        graphicsEngine.setInMenu(false);
        graphicsEngine.setTowerPlacerActive(true);
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
                        loadedMap = null;
                        enemies.clear();
                        projectiles.clear();
                        towers.clear();
                        movementComponents.clear();
                        graphicsEngine.clearGameEntities();
                        waveCounter = 0;
                        if (gameLogicLoop != null) gameLogicLoop.setMap(null);
                    }
                }
            }
        }
    }

    private void quitGame() {
        graphicsEngine.shutdown();
        System.exit(0);
    }

    private void onTowerPlaced(int tileCol, int tileRow, int pixelX, int pixelY,Towers towerType) {
        System.out.printf("Tower placed at map tile (%d, %d)%n", tileCol, tileRow);

        switch (towerType){
            case Archer:
                if (money > ArcherTower.getPrice()) {
                    synchronized (towers) {
                        towers.add(towerFactory.createArcherTower(
                                tileCol,
                                tileRow,
                                pixelX,
                                pixelY));
                    }
                }
                break;

            case Cannon:
                if (money > Cannon.getPrice()){
                    synchronized (towers) {
                        towers.add(towerFactory.createCannonTower(
                                tileCol,
                                tileRow,
                                pixelX,
                                pixelY));}
                    }
                break;
            default:
                break;
        }


    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!hasStarted) {
            return;
        }

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


package graphics.src;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.List;
import gameLogic.src.*;

public class VisualLoop implements KeyListener {

    private Map loadedMap;
    private GraphicsEngine graphicsEngine;
    private List<Enemy> enemies;
    private List<Projectile> projectiles ;
    private List<Tower> towers ;
    private List<MovementComponent> movementComponents;

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
        loadedMap = null;
        enemies.clear();
        projectiles.clear();
        towers.clear();
        movementComponents.clear();
        graphicsEngine.clearGameEntities();
    }

    private void quitGame() {
        graphicsEngine.shutdown();
        System.exit(0);
    }

    /**
     * Called by TowerPlacer (via GraphicsEngine) when the player clicks a
     * PLACEABLE tile.
     *
     * @param tileCol  column index in the map grid
     * @param tileRow  row index in the map grid
     * @param pixelX   top-left x of the tile in panel coordinates
     * @param pixelY   top-left y of the tile in panel coordinates
     */
    private void onTowerPlaced(int tileCol, int tileRow, int pixelX, int pixelY,Towers tower) {
        System.out.printf("Tower placed at map tile (%d, %d) - pixel (%d, %d)%n",
                tileCol, tileRow, pixelX, pixelY);

        Tower createdTower = null;
        switch (tower){
            case Archer:
                createdTower = towerFactory.createArcherTower(
                        tileCol,
                        tileRow,
                        pixelX,
                        pixelY);
                        graphicsEngine.addEntety(createdTower.getGraphics());
                break;
            case Cannon:
                createdTower = towerFactory.createCannonTower(
                        tileCol,
                        tileRow,
                        pixelX,
                        pixelY);
                        graphicsEngine.addEntety(createdTower.getGraphics());
                break;
            default:
                break;
        }

        if (createdTower != null) {
            towers.add(createdTower);
        }

    }


    /**
     * @param e the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e) {

    }

    /**
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {

    }

    /**
     * @param e the event to be processed
     */
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

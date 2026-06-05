import java.util.ArrayList;
import java.util.List;

public class GraphicsLoop {

    private Map loadedMap;
    private GraphicsEngine graphicsEngine;
    private VisualsFactory visualsFactory;
    private List<Enemy> enemies =  new ArrayList<>();
    private List<Projectile> projectiles =  new ArrayList<>();
    private List<Tower> towers =  new ArrayList<>();

    private boolean isInMenu = false;
    private boolean isPaused  = false;

    public GraphicsLoop(GraphicsEngine engine) {
        graphicsEngine = engine;
        visualsFactory = new OriginalVisualsFactory();

        // Load and display the map.
        loadedMap = new Map("/levels/Level_3.csv");
        System.out.println(loadedMap.getName());
        engine.addMap(loadedMap);

        // Register the callback that actually spawns a tower entity when the
        // player clicks a valid tile.
        engine.addTowerPlacedListener(this::onTowerPlaced);

        // Start in placement mode so the player can immediately place towers.
        // Toggle this however you like (button, key press, game-state machine …).
        engine.setTowerPlacerActive(true);

        graphicsEngine.startRenderTread();
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
    private void onTowerPlaced(int tileCol, int tileRow, int pixelX, int pixelY) {
        System.out.printf("Tower placed at tile (%d, %d) – pixel (%d, %d)%n",
                tileCol, tileRow, pixelX, pixelY);

        // Create the visual entity for the tower.
        GraphicsEntety towerEntity = visualsFactory.addTower(pixelX, pixelY);
        graphicsEngine.addEntety(towerEntity);

        // TODO: create the game-logic Tower object here and link it to the entity.
    }
}

package towerrush;

import java.awt.*;

public class Game {

    private int maxX;
    private int MaxY;
    private Map loadedMap;
    private GraphicsEngine graphicsEngine;
    private boolean isInMenu = true;
    private boolean isPaused = false;


    public Game(GraphicsEngine engine){
        // main game loop
        graphicsEngine = engine;
        loadedMap = new Map("src/towerrush/levels/map_level1.csv");
        System.out.println(loadedMap.getName());
        engine.addMap(loadedMap);
        graphicsEngine.startRenderTread();

    }








}

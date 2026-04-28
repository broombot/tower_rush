package towerrush;

import java.awt.*;

public class Game {

    int maxX;
    int MaxY;
    GraphicsEngine graphicsEngine;


    public Game(GraphicsEngine engine){
        // main game loop
        graphicsEngine = engine;
        graphicsEngine.startRenderTread();
        while (true) {
            GraphicsEntety entety = new GraphicsEntety(250, 250, Color.GREEN);
            for (int i = 0; i < 500; i++) {
                entety.setPosition(250 + i, 250 + i);
                graphicsEngine.addEntety(entety);
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            engine.removeEntety(entety);
        }
    }








}

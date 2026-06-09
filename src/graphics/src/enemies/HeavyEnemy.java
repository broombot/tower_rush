package graphics.src.enemies;

import gameLogic.src.Enemy;
import gameLogic.src.Path;
import graphics.src.GraphicsEntety;

import java.awt.*;

public class HeavyEnemy extends Enemy {
    private GraphicsEntety graphics;

    public HeavyEnemy(Path path, float difficultyScale ) {
        super(Math.round(200 * difficultyScale), path, Math.round(200 * difficultyScale),Math.round( 5 * difficultyScale));
        graphics = new GraphicsEntety(path.getPositionCord(0).getX(),path.getPositionCord(0).getY(),
                new Color(60, 25, 25),20);
    }

    public void updateGraphics(){
        graphics.setX(getPosition().getX());
        graphics.setY(getPosition().getY());
    }
}

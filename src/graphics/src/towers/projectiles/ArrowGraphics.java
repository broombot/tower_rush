package graphics.src.towers.projectiles;

import gameLogic.src.Enemy;
import gameLogic.src.MapPoint;
import gameLogic.src.projectiles.Arrow;
import graphics.src.GraphicsEntety;

import java.awt.*;

public class ArrowGraphics extends Arrow {
    private GraphicsEntety graphics;

    public ArrowGraphics(double x, double y, Enemy targetEnemy ,int damage) {
        super(x, y, targetEnemy,damage);
        graphics = new GraphicsEntety(x, y, new Color(119, 84, 55), 10);
    }

    public ArrowGraphics(double x, double y, MapPoint target , int damage) {
        super(x, y, target,damage);
        graphics = new GraphicsEntety(x, y, new Color(119, 84, 55), 10);
    }

    public  GraphicsEntety getGraphics(){
        return graphics;
    };

}

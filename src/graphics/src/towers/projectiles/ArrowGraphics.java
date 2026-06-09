package graphics.src.towers.projectiles;

import gameLogic.src.Enemy;
import gameLogic.src.MapPoint;
import gameLogic.src.Projectile;
import gameLogic.src.projectiles.Arrow;
import graphics.src.GraphicsEntety;

import java.awt.*;

public class ArrowGraphics extends Arrow {
    private GraphicsEntety graphics;


    public ArrowGraphics(int x, int y, Enemy targetEnemy) {
        super(x, y, targetEnemy);
        graphics = new GraphicsEntety(x,y, new Color(119, 84, 55),10);
    }

    public ArrowGraphics(int x, int y, MapPoint target) {
        super(x, y, target);
        graphics = new GraphicsEntety(x,y, new Color(119, 84, 55),10);
    }
}

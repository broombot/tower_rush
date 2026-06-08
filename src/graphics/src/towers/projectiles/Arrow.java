package graphics.src.towers.projectiles;

import gameLogic.src.MapPoint;
import gameLogic.src.Projectile;
import graphics.src.GraphicsEntety;

import java.awt.*;

public class Arrow extends Projectile {
    private GraphicsEntety graphics;

    public Arrow(int x, int y, int speed, MapPoint target, int damage, int life) {
        super(x, y, speed, target, damage, life);
        graphics = new GraphicsEntety(x,y, new Color(119, 84, 55),10);
    }



}

package graphics.src.towers.projectiles;

import gameLogic.src.Enemy;
import gameLogic.src.MapPoint;
import gameLogic.src.Projectile;
import gameLogic.src.projectiles.CannonBall;
import graphics.src.GraphicsEntety;

import java.awt.*;

public class CannonBallGraphics extends CannonBall {

    private GraphicsEntety graphics ;

    public CannonBallGraphics(int x, int y, int speed, MapPoint target, int damage, int life) {
        super(x, y, speed, target, damage, life);
        graphics =  new GraphicsEntety(x,y, new Color(0, 0, 0),10);
    }

    public CannonBallGraphics(int x, int y, int speed, Enemy targetEnemy, int damage, int life) {
        super(x, y, speed, targetEnemy, damage, life);
        graphics = new GraphicsEntety(x,y, new Color(0, 0, 0),10);
    }
}

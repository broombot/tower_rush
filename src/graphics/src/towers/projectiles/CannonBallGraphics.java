package graphics.src.towers.projectiles;

import gameLogic.src.Enemy;
import gameLogic.src.MapPoint;
import gameLogic.src.projectiles.CannonBall;
import graphics.src.GraphicsEntety;

import java.awt.*;

public class CannonBallGraphics extends CannonBall {
    private GraphicsEntety graphics;

    public CannonBallGraphics(double x, double y, Enemy targetEnemy,int damage) {
        super(x, y, targetEnemy,damage);
        graphics = new GraphicsEntety(x, y, new Color(62, 62, 62), 15);
    }

    public CannonBallGraphics(double x, double y, MapPoint target,int damage) {
        super(x, y, target,damage);
        graphics = new GraphicsEntety(x, y, new Color(62, 62, 62), 15);
    }

    public  GraphicsEntety getGraphics(){
        return graphics;
    };


}

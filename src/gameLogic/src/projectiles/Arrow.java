package gameLogic.src.projectiles;

import gameLogic.src.Enemy;
import gameLogic.src.MapPoint;

public class Arrow extends Projectile {
    public Arrow(double x, double y, MapPoint target, int damage) {
        super(x, y, 1.2, target, damage, 1000);
    }

    public Arrow(double x, double y, Enemy targetEnemy, int damage) {
        super(x, y, 1.2, targetEnemy, damage, 1000);
    }
}

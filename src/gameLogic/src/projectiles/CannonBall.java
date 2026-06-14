package gameLogic.src.projectiles;

import gameLogic.src.Enemy;
import gameLogic.src.MapPoint;

public class CannonBall extends Projectile {
    public CannonBall(double x, double y, MapPoint target, int damage) {
        super(x, y, 0.8, target, damage, 2000);
    }

    public CannonBall(double x, double y, Enemy targetEnemy, int damage) {
        super(x, y, 0.8, targetEnemy, damage, 2000);
    }
}

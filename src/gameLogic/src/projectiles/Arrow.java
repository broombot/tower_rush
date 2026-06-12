package gameLogic.src.projectiles;

import gameLogic.src.Enemy;
import gameLogic.src.MapPoint;

public abstract class Arrow extends Projectile {
    public Arrow(int x, int y, MapPoint target) {
        super(x, y, 10, target, 50, 1000);
    }

    public Arrow(int x, int y, Enemy targetEnemy) {
        super(x, y, 10, targetEnemy, 50, 1000);
    }
}

package gameLogic.src.projectiles;

import gameLogic.src.Enemy;
import gameLogic.src.MapPoint;
import gameLogic.src.Projectile;

public class CannonBall extends Projectile {
    public CannonBall(int x, int y, MapPoint target) {
        super(x, y, 20, target, 100, 760);
    }

    public CannonBall(int x, int y, Enemy targetEnemy) {
        super(x, y, 20, targetEnemy, 100, 760);
    }
}

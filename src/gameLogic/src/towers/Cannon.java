package gameLogic.src.towers;

import gameLogic.src.Enemy;
import gameLogic.src.projectiles.CannonBall;
import gameLogic.src.projectiles.Projectile;

public class Cannon extends Tower {
    public Cannon(double x, double y) {
        super( 1200, x, y); // Much faster: 1200ms (was 3000ms)
        setDamage(80);       // High damage
        setRange(12);       // Long range
    }

    @Override
    public Projectile attack(Enemy target) {
        return new CannonBall(position.getX(), position.getY(), target,getDamage());
    }

    public static int getPrice() {
        return 200;
    }
}

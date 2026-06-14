package gameLogic.src.towers;

import gameLogic.src.Enemy;
import gameLogic.src.projectiles.Arrow;
import gameLogic.src.projectiles.Projectile;

public class ArcherTower extends Tower {
    public ArcherTower(double x, double y) {
        super( 500, x, y); // Fast attack: 500ms
        setDamage(25);      // Double damage
        setRange(10);      // Extended range
    }

    @Override
    public Projectile attack(Enemy target) {
        return new Arrow(position.getX(), position.getY(), target, getDamage());
    }

    public static int getPrice() {
        return 100;
    }
}

package gameLogic.src;

public abstract class Cannon extends Tower {
    public Cannon(double x, double y) {
        super(200, 3000, x, y);
        setDamage(20);
        setRange(8);
    }

    @Override
    public Projectile attack(Enemy target) {
        if (target == null) {
            return null;
        }

        return new Projectile(
                getPosition().getIntX(),
                getPosition().getIntY(),
                2,
                target,
                getDamage(),
                3000);
    }
}

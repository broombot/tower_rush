package gameLogic.src;


public abstract class ArcherTower extends Tower {
    protected ArcherTower(double x, double y) {
        super(100, 1000, x, y);
        setDamage(12);
        setRange(6);
    }

    @Override
    public Projectile attack(Enemy target) {
        if (target == null) {
            return null;
        }

        return new Projectile(
                getPosition().getIntX(),
                getPosition().getIntY(),
                4,
                target,
                getDamage(),
                2000);
    }
}

package gameLogic.src;

public abstract class Projectile {
    private final MovementComponent movementComponent;
    private final int damage;
    private final StopWatch life;
    private final Enemy targetEnemy;
    private boolean damageApplied;

    public Projectile(int x, int y, int speed, MapPoint target, int damage, int life) {
        this.movementComponent = new MovementComponent(x, y, speed, target);
        this.damage = damage;
        this.life = new StopWatch(life);
        this.targetEnemy = null;
    }

    public Projectile(int x, int y, int speed, Enemy targetEnemy, int damage, int life) {
        this.movementComponent = new MovementComponent(
                x,
                y,
                speed,
                targetEnemy != null ? targetEnemy.getPosition() : new MapPoint(x, y));
        this.damage = damage;
        this.life = new StopWatch(life);
        this.targetEnemy = targetEnemy;
    }

    public void update() {
        if (targetEnemy != null && targetEnemy.isAlive()) {
            movementComponent.setTarget(targetEnemy.getPosition());
        }

        MapPoint target = movementComponent.getTarget();
        double dx = target.getX() - movementComponent.getX();
        double dy = target.getY() - movementComponent.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);

        if (distance <= movementComponent.getSpeed() || distance == 0) {
            movementComponent.setX(target.getX());
            movementComponent.setY(target.getY());
        } else {
            double ratio = movementComponent.getSpeed() / distance;
            movementComponent.setX(movementComponent.getX() + dx * ratio);
            movementComponent.setY(movementComponent.getY() + dy * ratio);
        }

        if (targetEnemy != null && targetEnemy.isAlive() && reachedTarget() && !damageApplied) {
            targetEnemy.receiveDamage(damage);
            damageApplied = true;
        }
    }

    public boolean checkLife() {
        return life.isFinished();
    }

    public boolean reachedTarget() {
        return movementComponent.getX() == movementComponent.getTarget().getX()
                && movementComponent.getY() == movementComponent.getTarget().getY();
    }

    public boolean isExpired() {
        if (checkLife()) {
            return true;
        }

        if (targetEnemy != null) {
            return damageApplied || !targetEnemy.isAlive();
        }

        return reachedTarget();
    }

    public int getDamage() {
        return damage;
    }

    public MapPoint getPosition() {
        return new MapPoint(movementComponent.getX(), movementComponent.getY());
    }

    public MapPoint getTarget() {
        return movementComponent.getTarget();
    }
}

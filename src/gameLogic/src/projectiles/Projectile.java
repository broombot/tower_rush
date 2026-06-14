package gameLogic.src.projectiles;

import gameLogic.src.*;

public abstract class Projectile implements GameObject {
    private final MovementComponent movementComponent;
    private final int damage;
    private final StopWatch life;
    private final Enemy targetEnemy;

    public Projectile(double x, double y, double speed, MapPoint target, int damage, int life) {
        this.movementComponent = new MovementComponent(x, y, speed, target);
        this.damage = damage;
        this.life = new StopWatch(life);
        this.targetEnemy = null;
    }

    public Projectile(double x, double y, double speed, Enemy targetEnemy, int damage, int life) {
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
    }

    public boolean checkLife() {
        return life.isFinished();
    }

    public boolean reachedTarget() {
        double dx = movementComponent.getX() - movementComponent.getTarget().getX();
        double dy = movementComponent.getY() - movementComponent.getTarget().getY();
        return Math.sqrt(dx * dx + dy * dy) < 0.3;
    }

    public boolean isExpired() {
        if (checkLife()) {
            return true;
        }

        if (targetEnemy != null && !targetEnemy.isAlive()) {
            return true;
        }

        return reachedTarget();
    }

    public int getDamage() {
        return damage;
    }

    @Override
    public MapPoint getPosition() {
        return new MapPoint(movementComponent.getX(), movementComponent.getY());
    }

    public MapPoint getTarget() {
        return movementComponent.getTarget();
    }

    public MovementComponent getMovementComponent() {
        return movementComponent;
    }
}

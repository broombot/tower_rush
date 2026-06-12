package gameLogic.src.projectiles;

import gameLogic.src.*;
import graphics.src.GraphicsEntety;

public abstract class Projectile {
    private final MovementComponent movementComponent;
    private final int damage;
    private final StopWatch life;
    private final Enemy targetEnemy;
    private boolean damageApplied;
    private Map map;

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

    public void setMap(Map map) {
        this.map = map;
        updateGraphics();
    }

    public void update() {
        if (targetEnemy != null && targetEnemy.isAlive()) {
            movementComponent.setTarget(targetEnemy.getPosition());
        }

        if (targetEnemy != null && targetEnemy.isAlive() && reachedTarget() && !damageApplied) {
            targetEnemy.receiveDamage(damage);
            damageApplied = true;
        }
        updateGraphics();
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

    public MovementComponent getMovementComponent() {
        return movementComponent;
    }

    public abstract GraphicsEntety getGraphics();

    public void updateGraphics() {
        if (map == null) return;
        GraphicsEntety graphics = getGraphics();
        if (graphics == null) return;

        double relX = (movementComponent.getX() * 100.0) / map.getMapWith();
        double relY = (movementComponent.getY() * 100.0) / map.getMapHight();
        graphics.setPosition(relX, relY);
    }
}

package gameLogic.src.towers;

import gameLogic.src.*;
import gameLogic.src.projectiles.Projectile;

public abstract class Tower implements GameObject {

    protected final MapPoint position;
    private int damage;
    private StopWatch attackTimer;
    private final int attackTime;
    private int range;

    protected Tower( int attackTime, double x, double y) {
        this.attackTime = attackTime;
        this.position = new MapPoint(x,y);
        this.attackTimer = new StopWatch(attackTime);
    }

    public abstract Projectile attack(Enemy target);

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public boolean isReadyToAttack(){
      return attackTimer.isFinished();
    }

    public void resetTimer(){
        attackTimer = new StopWatch(attackTime);
    }

    public int getAttackTime() {
        return attackTime;
    }

    @Override
    public MapPoint getPosition() {
        return position;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }
}

package gameLogic.src.towers;

import gameLogic.src.*;
import gameLogic.src.projectiles.Projectile;
import graphics.src.GraphicsEntety;

public abstract class Tower {

    protected final MapPoint position;
    private int damage;
    private static final int price;
    private StopWatch attackTimer;
    private final int attackTime;
    private int range;
    protected Map map;

    protected Tower(int price, int attackTime, double x, double y) {
        this.price = price;
        this.attackTime = attackTime;
        this.position = new MapPoint(x,y);
        this.attackTimer = new StopWatch(attackTime);
    }

    public void setMap(Map map) {
        this.map = map;
        updateGraphics();
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
    };

    public void resetTimer(){
        attackTimer = new StopWatch(attackTime);
    };

    public int getAttackTime() {
        return attackTime;
    }

    public MapPoint getPosition() {
        return position;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public static int getPrice() {return price;}

    public abstract GraphicsEntety getGraphics();

    public void updateGraphics() {
        if (map == null) return;
        GraphicsEntety graphics = getGraphics();
        if (graphics == null) return;

        double relX = (position.getX() * 100.0) / map.getMapWith();
        double relY = (position.getY() * 100.0) / map.getMapHight();
        graphics.setPosition(relX, relY);
    }
}


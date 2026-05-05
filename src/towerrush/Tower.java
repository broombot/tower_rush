package towerrush;

public abstract class Tower {

    private MapPoint position;
    private int damage;
    private final int price;
    private  int attackTimer;
    private final int attackTime;
    private int range;

    protected Tower(int price, int attackTime) {
        this.price = price;
        this.attackTime = attackTime;
    }


    abstract void attack();

    public int getDamage() {
        return damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public int getAttackTimer() {
        return attackTimer;
    }

    public void setAttackTimer(int attackTimer) {
        this.attackTimer = attackTimer;
    }

    public int getAttackTime() {
        return attackTime;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }
}

package gameLogic.src.towers;

public abstract class Cannon extends Tower {
    public Cannon(double x, double y) {
        super(200, 3000, x, y);
        setDamage(20);
        setRange(8);
    }

}

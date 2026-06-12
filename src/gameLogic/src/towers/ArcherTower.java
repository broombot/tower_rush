package gameLogic.src.towers;


public abstract class ArcherTower extends Tower {
    protected ArcherTower(double x, double y) {
        super(100, 1000, x, y);
        setDamage(12);
        setRange(6);
    }

}

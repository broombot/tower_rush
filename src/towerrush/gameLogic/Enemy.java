package towerrush.gameLogic;

public class Enemy {
    private MovementComponent movementComponent;
    private int health;
    private int damage;
    private Path path;



    public Enemy(double speed ,Path path , int health, int damage) {
        this.path = path;
        this.movementComponent = new MovementComponent(
                path.getPositionCord(0).getX(),
                path.getPositionCord(0).getY(),
                speed,
                path.getPositionCord(1));
        this.health = health;
        this.damage = damage;
    }
}

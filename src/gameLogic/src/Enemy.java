package gameLogic.src;

public abstract class Enemy {
    private MovementComponent movementComponent;
    private int health;
    private int damage;
    private Path path;
    private int pathPosition = 0;



    public Enemy(double speed ,Path path , int health, int damage) {
        this.path = path;
        this.movementComponent = new MovementComponent(
                path.getPositionCord(0).getX(),
                path.getPositionCord(0).getY(),
                speed,
                path.getPositionCord(2));
        this.health = health;
        this.damage = damage;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public MapPoint getPosition() {
        return new MapPoint(movementComponent.getX(), movementComponent.getY());
    }

    public MovementComponent getMovementComponent() {
        return movementComponent;
    }

    public void receiveDamage(int damage){
        health = Math.max( health - damage , 0);
    }

    public boolean reachedEnd(){
        return pathPosition + movementComponent.getSpeed() >= path.getPath().length - 1;
    };

    public void Update(){
        pathPosition = pathPosition + (int) Math.round(movementComponent.getSpeed());
        movementComponent.setTarget(path.getPath()[pathPosition]);
    }
}

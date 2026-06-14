package gameLogic.src;

public abstract class Enemy implements GameObject {
    private MovementComponent movementComponent;
    private int health;
    private int damage;
    private int reward;
    private Path path;
    private int pathPosition = 0;

    public Enemy(double speed ,Path path , int health, int damage, int reward) {
        this.path = path;
        
        // Add a tiny random offset to avoid exact overlap
        double offset = (Math.random() - 0.5) * 0.2;
        
        this.movementComponent = new MovementComponent(
                path.getPositionCord(0).getX() + offset,
                path.getPositionCord(0).getY() + offset,
                speed,
                path.getPositionCord(0));
        this.health = health;
        this.damage = damage;
        this.reward = reward;
    }

    public int getDamage() {
        return damage;
    }

    public boolean isAlive() {
        return health > 0;
    }

    public int getReward() {
        return reward;
    }

    @Override
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
        return pathPosition >= path.getPath().length - 1;
    }

    public void Update(){
        // Move to next path point if we are close enough to current target
        double dx = movementComponent.getX() - movementComponent.getTarget().getX();
        double dy = movementComponent.getY() - movementComponent.getTarget().getY();
        double dist = Math.sqrt(dx*dx + dy*dy);

        if (dist < 0.1) {
            pathPosition = Math.min(pathPosition + 1, path.getPath().length - 1);
            movementComponent.setTarget(path.getPath()[pathPosition]);
        }
    }

    public int getPathPosition() {
        return pathPosition;
    }
}

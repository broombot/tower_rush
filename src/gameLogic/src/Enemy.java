package gameLogic.src;

import graphics.src.GraphicsEntety;

public abstract class Enemy {
    private MovementComponent movementComponent;
    private int health;
    private int damage;
    private Path path;
    private int pathPosition = 0;
    private Map map;

    public Enemy(double speed ,Path path , int health, int damage) {
        this.path = path;
        // speed here is tiles per frame. 0.1 means 1 tile every 10 frames.
        this.movementComponent = new MovementComponent(
                path.getPositionCord(0).getX(),
                path.getPositionCord(0).getY(),
                speed,
                path.getPositionCord(0));
        this.health = health;
        this.damage = damage;
    }

    public void setMap(Map map) {
        this.map = map;
        updateGraphics();
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
        return pathPosition >= path.getPath().length - 1;
    };

    public void Update(){
        // Move to next path point if we are close enough to current target
        if (movementComponent.getX() == movementComponent.getTarget().getX() && 
            movementComponent.getY() == movementComponent.getTarget().getY()) {
            pathPosition = Math.min(pathPosition + 1, path.getPath().length - 1);
            movementComponent.setTarget(path.getPath()[pathPosition]);
        }
        updateGraphics();
    }

    public int getPathPosition() {
        return pathPosition;
    }

    public abstract GraphicsEntety getGraphics();

    public void updateGraphics() {
        if (map == null) return;
        
        GraphicsEntety graphics = getGraphics();
        if (graphics == null) return;

        double tileX = movementComponent.getX();
        double tileY = movementComponent.getY();
        
        // Convert tile coordinates to percentage (0-100)
        // mapWith and mapHight are now correct dimensions
        double relX = (tileX * 100.0) / map.getMapWith();
        double relY = (tileY * 100.0) / map.getMapHight();
        
        graphics.setPosition(relX, relY);
    }
}

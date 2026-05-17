public class MovementComponent {

    private double x;
    private double y;
    private double speed;
    private MapPoint target;

    public MovementComponent(double x, double y, double speed, MapPoint target) {
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.target = target;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public MapPoint getTarget() {
        return target;
    }

    public void setTarget(MapPoint target) {
        this.target = target;
    }
}

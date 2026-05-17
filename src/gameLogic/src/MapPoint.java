public class MapPoint {

    private double x;
    private double y;

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getIntX() {return (int)Math.round(x);}

    public int getIntY() {return (int)Math.round(y);}

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public MapPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MapPoint mapPoint = (MapPoint) o;
        return getX() == mapPoint.getX() && getY() == mapPoint.getY();
    }

    @Override
    public String toString() {
        return "MapPoint{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }


    public double distance(MapPoint mapPoint){
        return Math.sqrt(Math.pow(this.x - mapPoint.x,2) +
                Math.pow(this.y - mapPoint.y ,2));
    };
}

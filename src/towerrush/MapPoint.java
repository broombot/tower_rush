package towerrush;

import java.util.Objects;

public class MapPoint {

    private int x;
    private int y;

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public MapPoint(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public double getDoublex(){
        return x;
    }

    public double getDoubley(){
        return y;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        MapPoint mapPoint = (MapPoint) o;
        return getX() == mapPoint.getX() && getY() == mapPoint.getY();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }

    public double distance(MapPoint mapPoint){
        return Math.sqrt(Math.pow(this.getDoublex() + mapPoint.getDoublex(),2) +
                Math.pow(this.getDoubley() + mapPoint.getDoubley(),2));
    };
}

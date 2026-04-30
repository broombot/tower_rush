
package towerrush;

public class Path {

    private MapPoint[] Path;

    public MapPoint[] getPath() {
        return Path;
    }

    public void setPath(MapPoint[] path) {
        this.Path = path;
    }

    public MapPoint getPositionCord(int position){
        try {
            if (position > Path.length){
                throw new ArrayIndexOutOfBoundsException("not on path");
            }
            return Path[position];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
    }

    public Path(MapPoint[] path) {
        Path = path;
    }

}

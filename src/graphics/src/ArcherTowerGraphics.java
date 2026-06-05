
import java.awt.Color;

public class ArcherTowerGraphics extends ArcherTower {
    private GraphicsEntety graphics;

    public ArcherTowerGraphics(double x, double y) {
        super(x, y);
        this.graphics = new GraphicsEntety((int)x, (int)y, new Color(0, 150, 0));
    }

    public GraphicsEntety getGraphics() {
        return graphics;
    }


}

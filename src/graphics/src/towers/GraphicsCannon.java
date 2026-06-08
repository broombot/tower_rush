package graphics.src.towers;

import gameLogic.src.Cannon;
import graphics.src.GraphicsEntety;

import java.awt.*;

public class GraphicsCannon extends Cannon {

    GraphicsEntety graphics;

    public GraphicsCannon(double tileX, double tileY, int pixelX, int pixelY) {
        super(tileX, tileY);
        this.graphics = new GraphicsEntety(pixelX, pixelY, new Color(62, 62, 62));
    }

    public GraphicsEntety getGraphics() {
        return graphics;
    }
}

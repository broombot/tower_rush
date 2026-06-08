package graphics.src.towers;

import gameLogic.src.ArcherTower;
import graphics.src.GraphicsEntety;

import java.awt.Color;

public class ArcherTowerGraphics extends ArcherTower {
    private GraphicsEntety graphics;

    public ArcherTowerGraphics(double tileX, double tileY, int pixelX, int pixelY) {
        super(tileX, tileY);
        this.graphics = new GraphicsEntety(pixelX, pixelY, new Color(0, 80, 150));
    }

    public GraphicsEntety getGraphics() {
        return graphics;
    }


}

package graphics.src;

import gameLogic.src.towers.ArcherTower;
import graphics.src.towers.ArcherTowerGraphics;
import graphics.src.towers.GraphicsCannon;


/**
 * Concrete fabriek voor het maken van grafische torens (voor de GUI).
 */

public class GraphicsTowerFactory implements TowerFactory {

    @Override
    public ArcherTower createArcherTower(double x, double y , int pixelX, int pixelY) {
        return new ArcherTowerGraphics(x, y, pixelX, pixelY);
    }

    @Override
    public GraphicsCannon createCannonTower(double x, double y, int pixelX, int pixelY) {
        // Maakt de grafische variant van de kanontoren aan
        return new GraphicsCannon(x, y, pixelX, pixelY);
    }
}

package graphics.src;

import gameLogic.src.towers.ArcherTower;
import graphics.src.towers.GraphicsCannon;

/**
 * De Abstract Factory interface die de blauwdruk definieert
 * voor het aanmaken van verschillende soorten torens.
 */
public interface TowerFactory {
    ArcherTower createArcherTower(double tileX, double tileY, int pixelX, int pixelY);
    GraphicsCannon createCannonTower(double tileX, double tileY, int pixelX, int pixelY);
}



/**
 * Concrete fabriek voor het maken van grafische torens (voor de GUI).
 */

public class GraphicsTowerFactory implements TowerFactory {

    @Override
    public ArcherTower createArcherTower(double x, double y , int screenW, int screenH) {
        // Maakt de grafische variant van de boogschuttertoren aan
        return new ArcherTowerGraphics(x, y);
    }

    @Override
    public GraphicsCannon createCannonTower(double x, double y,int screenW, int screenH) {
        // Maakt de grafische variant van de kanontoren aan
        return new GraphicsCannon(x, y);
    }
}

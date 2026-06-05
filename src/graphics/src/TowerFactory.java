
/**
 * De Abstract Factory interface die de blauwdruk definieert
 * voor het aanmaken van verschillende soorten torens.
 */
public interface TowerFactory {
    ArcherTower createArcherTower(double x, double y , int screenW, int screenH);
    GraphicsCannon createCannonTower(double x, double y , int screenW, int screenH);
}

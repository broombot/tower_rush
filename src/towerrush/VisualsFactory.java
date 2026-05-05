package towerrush;

public interface VisualsFactory {

    GraphicsEntety addEnemy(double x, double y);
    GraphicsEntety addTower(double x, double y);
    GraphicsEntety addEntety(double x, double y);
    GraphicsEntety addProjectile(double x, double y);
    GamePanel creatGamePanel();
    MenuPanel creatMenuPanel();

}

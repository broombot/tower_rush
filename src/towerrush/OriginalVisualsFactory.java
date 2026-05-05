package towerrush;

import java.awt.*;

public class OriginalVisualsFactory implements VisualsFactory {

    private final String SpritesFolder = "originalTheme";



    @Override
    public GraphicsEntety addEnemy(double x ,double y) {
        return new GraphicsEntety(x,y,new Color(10,220,30));
    }


    @Override
    public GraphicsEntety addTower(double x, double y) {
        return null;
    }


    @Override
    public GraphicsEntety addEntety(double x, double y) {
        return null;
    }


    @Override
    public GraphicsEntety addProjectile(double x, double y) {
        return null;
    }


    @Override
    public GamePanel creatGamePanel() {
        return null;
    }


    @Override
    public MenuPanel creatMenuPanel() {
        return null;
    }
}

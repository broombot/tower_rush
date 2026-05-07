package towerrush.graphics;

import towerrush.gameLogic.VisualsFactory;

import java.awt.*;

public class OriginalVisualsFactory implements VisualsFactory {

    private final String SpritesFolder = "originalTheme";



    @Override
    public GraphicsEntety addEnemy(int x , int y) {
        return new GraphicsEntety(x,y,new Color(10,220,30));
    }


    @Override
    public GraphicsEntety addTower(int x, int y) {return new GraphicsEntety(x,y,new Color(182, 47, 86));}


    @Override
    public GraphicsEntety addEntety(int x, int y) {
        return new GraphicsEntety(x,y,Color.MAGENTA);
    }


    @Override
    public GraphicsEntety addProjectile(int x, int y) {
        return new GraphicsEntety(x,y,Color.BLACK);
    }


    @Override
    public GamePanel creatGamePanel() {
        return new GamePanel(
                new Color(6, 147, 193),
                new Color(53, 56, 58),
                new Color(156, 94,0),
                new Color(70, 145, 0),
                2

        );
    }


    @Override
    public MenuPanel creatMenuPanel() {
        return new MenuPanel();
    }
}

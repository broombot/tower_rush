package towerrush.gameLogic;

import towerrush.graphics.GamePanel;
import towerrush.graphics.GraphicsEntety;
import towerrush.graphics.MenuPanel;

public interface VisualsFactory {

    GraphicsEntety addEnemy(int x, int y);
    GraphicsEntety addTower(int x, int y);
    GraphicsEntety addEntety(int x, int y);
    GraphicsEntety addProjectile(int x, int y);
    GamePanel creatGamePanel();
    MenuPanel creatMenuPanel();

}

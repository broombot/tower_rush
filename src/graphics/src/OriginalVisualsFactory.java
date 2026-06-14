package graphics.src;

import gameLogic.src.Enemy;
import gameLogic.src.towers.Tower;
import gameLogic.src.towers.ArcherTower;
import gameLogic.src.towers.Cannon;
import gameLogic.src.projectiles.Projectile;
import gameLogic.src.projectiles.Arrow;
import gameLogic.src.projectiles.CannonBall;
import graphics.src.enemies.*;

import java.awt.*;

public class OriginalVisualsFactory implements VisualsFactory {

    @Override
    public EntityVisual createEnemyVisual(Enemy enemy) {
        Color color = Color.MAGENTA;
        int size = 20;
        
        if (enemy instanceof FastEnemy) {
            color = new Color(255, 110, 236);
        } else if (enemy instanceof NormalEnemy) {
            color = new Color(245, 255, 110);
        } else if (enemy instanceof HeavyEnemy) {
            color = new Color(200, 0, 0);
            size = 30;
        }
        
        GraphicsEntety ge = new GraphicsEntety(0, 0, color, size);
        return new GenericVisual(enemy, ge);
    }

    @Override
    public EntityVisual createTowerVisual(Tower tower) {
        Color color = new Color(182, 47, 86);
        int size = 30;
        
        if (tower instanceof ArcherTower) {
            color = new Color(0, 80, 150);
        } else if (tower instanceof Cannon) {
            color = new Color(62, 62, 62);
        }
        
        GraphicsEntety ge = new GraphicsEntety(0, 0, color, size);
        return new GenericVisual(tower, ge);
    }

    @Override
    public EntityVisual createProjectileVisual(Projectile projectile) {
        Color color = Color.BLACK;
        int size = 10;
        
        if (projectile instanceof Arrow) {
            color = new Color(119, 84, 55);
            size = 10;
        } else if (projectile instanceof CannonBall) {
            color = new Color(62, 62, 62);
            size = 15;
        }
        
        GraphicsEntety ge = new GraphicsEntety(0, 0, color, size);
        return new GenericVisual(projectile, ge);
    }

    @Override
    public GamePanel creatGamePanel() {
        return new GamePanel(
                new Color(6, 147, 193),
                new Color(53, 56, 58),
                new Color(156, 94, 0),
                new Color(70, 145, 0),
                2
        );
    }

    @Override
    public MenuPanel creatMenuPanel() {
        return new MenuPanel();
    }
}

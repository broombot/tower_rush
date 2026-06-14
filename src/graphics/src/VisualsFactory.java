package graphics.src;

import gameLogic.src.Enemy;
import gameLogic.src.towers.Tower;
import gameLogic.src.projectiles.Projectile;

public interface VisualsFactory {
    EntityVisual createEnemyVisual(Enemy enemy);
    EntityVisual createTowerVisual(Tower tower);
    EntityVisual createProjectileVisual(Projectile projectile);
    
    GamePanel creatGamePanel();
    MenuPanel creatMenuPanel();
}

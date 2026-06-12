package gameLogic.src.towers;

import java.util.List;

import gameLogic.src.Enemy;
import gameLogic.src.MovementComponent;
import gameLogic.src.projectiles.Projectile;
import graphics.src.GraphicsEngine;

public class TowerUpdater {

    public void updateTowers(List<Tower> towers, List<Enemy> enemies, List<Projectile> projectiles, List<MovementComponent> movementComponents, GraphicsEngine graphicsEngine) {
        if (towers == null || enemies == null || projectiles == null || movementComponents == null) {
            return;
        }

        for (Tower tower : towers) {
            if (!tower.isReadyToAttack()) {
                continue;
            }

            Enemy target = findTarget(tower, enemies);
            if (target == null) {
                continue;
            }

            Projectile projectile = tower.attack(target);
            if (projectile != null) {
                projectiles.add(projectile);
                movementComponents.add(projectile.getMovementComponent());
                if (graphicsEngine != null) {
                    graphicsEngine.addEntety(projectile.getGraphics());
                }
                tower.resetTimer();
            }
        }
    }

    private Enemy findTarget(Tower tower, List<Enemy> enemies) {
        Enemy closestEnemy = null;
        double closestDistance = Double.MAX_VALUE;

        for (Enemy enemy : enemies) {
            if (enemy == null || !enemy.isAlive()) {
                continue;
            }

            double distance = tower.getPosition().distance(enemy.getPosition());
            if (distance > tower.getRange()) {
                continue;
            }

            // Prefer enemies further along the path, or just closest?
            // Original code used enemy.getPathPosition() > closestDistance
            // but initialized closestDistance = 0. That finds the FURTHEST enemy on path.
            // Let's stick to finding the one furthest along the path within range.
            int pathPos = enemy.getPathPosition();
            if (closestEnemy == null || pathPos > (int)closestDistance) {
                closestDistance = pathPos;
                closestEnemy = enemy;
            }
        }

        return closestEnemy;
    }
}


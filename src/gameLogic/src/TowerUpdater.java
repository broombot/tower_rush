package gameLogic.src;

import java.util.List;

public class TowerUpdater {

    public void updateTowers(List<Tower> towers, List<Enemy> enemies, List<Projectile> projectiles) {
        if (towers == null || enemies == null || projectiles == null) {
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

            if (distance < closestDistance) {
                closestDistance = distance;
                closestEnemy = enemy;
            }
        }

        return closestEnemy;
    }
}

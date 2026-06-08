package gameLogic.src;

import java.util.ArrayList;
import java.util.List;

public class GameLogicLoop extends Thread {

    private final Object pauseLock = new Object();
    private volatile boolean paused = true;
    private volatile boolean running = true;
    private List<MovementComponent> movementComponents = new ArrayList<MovementComponent>();
    private List<Tower> towers;
    private List<Enemy> enemies;
    private List<Projectile> projectiles;
    private final TowerUpdater towerUpdater = new TowerUpdater();

    /**
     *
     */
    @Override
    public void run() {
        while (running && !isInterrupted()) {
            synchronized (pauseLock) {
                while (paused && running) {
                    try {
                        pauseLock.wait();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        return;
                    }
                }
            }

            if (!running || isInterrupted()) {
                return;
            }

            towerUpdater.updateTowers(towers, enemies, projectiles);
            updateProjectiles();

            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return;
            }
        }
    }

    public GameLogicLoop(List<Enemy> enemies, List<Tower> towers, List<MovementComponent> movementComponents, List<Projectile> projectiles) {
        this.enemies = enemies;
        this.towers = towers;
        this.movementComponents = movementComponents;
        this.projectiles = projectiles;
    }

    public void pauseLoop(){
        paused = true;
    }

    public void resumeLoop(){
        synchronized (pauseLock) {
            paused = false;
            pauseLock.notifyAll();
        }
    }

    public void shutdown() {
        running = false;
        interrupt();
        resumeLoop();
    }

    private void colisionDetection(){

    }

    private void updateProjectiles() {
        projectiles.removeIf(projectile -> {
            projectile.update();
            return projectile.isExpired();
        });
    }


}

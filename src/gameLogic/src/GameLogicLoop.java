package gameLogic.src;

import java.util.ArrayList;
import java.util.List;

import gameLogic.src.projectiles.Projectile;
import gameLogic.src.towers.Tower;
import gameLogic.src.towers.TowerUpdater;
import graphics.src.GraphicsEngine;

public class GameLogicLoop extends Thread {

    private final Object pauseLock = new Object();
    private volatile boolean paused = true;
    private volatile boolean running = true;
    private final List<MovementComponent> movementComponents;
    private final List<Tower> towers;
    private final List<Enemy> enemies;
    private final List<Projectile> projectiles;
    private final TowerUpdater towerUpdater = new TowerUpdater();
    private final MovementUpdater movementUpdater = new MovementUpdater();
    private final GraphicsEngine graphicsEngine;
    private StopWatch logicTimer;
    private Map currentMap;

    public GameLogicLoop(List<Enemy> enemies, List<Tower> towers, List<MovementComponent> movementComponents, List<Projectile> projectiles, GraphicsEngine graphicsEngine) {
        this.enemies = enemies;
        this.towers = towers;
        this.movementComponents = movementComponents;
        this.projectiles = projectiles;
        this.graphicsEngine = graphicsEngine;
    }

    public void setMap(Map map) {
        this.currentMap = map;
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
        synchronized (enemies) {
            synchronized (projectiles) {
                List<Projectile> toRemove = new ArrayList<>();
                for (Projectile projectile : projectiles) {
                    for (Enemy enemy : enemies) {
                        if (enemy.isAlive() && enemy.getPosition().distance(projectile.getPosition()) < 1) {
                            enemy.receiveDamage(projectile.getDamage());
                            toRemove.add(projectile);
                            break;
                        }
                    }
                }
                
                for (Projectile p : toRemove) {
                    projectiles.remove(p);
                    synchronized (movementComponents) {
                        movementComponents.remove(p.getMovementComponent());
                    }
                    if (graphicsEngine != null) {
                        graphicsEngine.removeEntety(p.getGraphics());
                    }
                }
            }
        }
    }

    private void updateProjectiles() {
        synchronized (projectiles) {
            List<Projectile> toRemove = new ArrayList<>();
            for (Projectile projectile : projectiles) {
                projectile.update();
                if (projectile.isExpired()) {
                    toRemove.add(projectile);
                }
            }
            
            for (Projectile p : toRemove) {
                projectiles.remove(p);
                synchronized (movementComponents) {
                    movementComponents.remove(p.getMovementComponent());
                }
                if (graphicsEngine != null) {
                    graphicsEngine.removeEntety(p.getGraphics());
                }
            }
        }
    }

    @Override
    public void run() {
        while (running && !isInterrupted()) {

            logicTimer = new StopWatch(50);

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

            // 1. Update Enemies
            synchronized (enemies) {
                for (Enemy enemy : enemies) {
                    if (enemy.isAlive()) {
                        enemy.Update();
                    }
                }
                // Remove dead enemies
                enemies.removeIf(enemy -> {
                    boolean dead = !enemy.isAlive() || enemy.reachedEnd();
                    if (dead) {
                        synchronized (movementComponents) {
                            movementComponents.remove(enemy.getMovementComponent());
                        }
                        if (graphicsEngine != null) {
                            graphicsEngine.removeEntety(enemy.getGraphics());
                        }
                    }
                    return dead;
                });
            }

            // 2. Update Towers
            synchronized (towers) {
                towerUpdater.updateTowers(towers, enemies, projectiles, movementComponents, graphicsEngine);
            }

            // 3. Collision and Projectiles
            colisionDetection();
            updateProjectiles();

            // 4. Movement
            synchronized (movementComponents) {
                movementUpdater.updatePosition(movementComponents);
            }

            if (!logicTimer.isFinished()) {
                try {
                    Thread.sleep(logicTimer.timeLeft());
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }
}

package gameLogic.src;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import gameLogic.src.projectiles.Projectile;
import gameLogic.src.towers.Tower;
import gameLogic.src.towers.TowerUpdater;


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
    private StopWatch logicTimer;
    private Map currentMap;
    private Consumer<Integer> onEnemyKilled;
    private Consumer<Integer> onEnemyReachedEnd;

    public GameLogicLoop(List<Enemy> enemies, List<Tower> towers, List<MovementComponent> movementComponents, List<Projectile> projectiles) {
        this.enemies = enemies;
        this.towers = towers;
        this.movementComponents = movementComponents;
        this.projectiles = projectiles;
    }

    public void setOnEnemyKilled(Consumer<Integer> callback) {
        this.onEnemyKilled = callback;
    }

    public void setOnEnemyReachedEnd(Consumer<Integer> callback) {
        this.onEnemyReachedEnd = callback;
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
        resumeLoop(); // Wake up if paused
    }

    @Override
    public void run() {
        logicTimer = new StopWatch(32);

        while (running) {
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

            if (!running) break;

            if (logicTimer.isFinished()) {
                // 1. Update Projectiles Logic (Homing)
                synchronized (projectiles) {
                    for (Projectile projectile : projectiles) {
                        projectile.update();
                    }
                }

                // 2. Update Projectile Collisions and Expiry
                synchronized (projectiles) {
                    List<Projectile> toRemove = new ArrayList<>();
                    synchronized (enemies) {
                        for (Projectile projectile : projectiles) {
                            boolean hit = false;
                            for (Enemy enemy : enemies) {
                                if (enemy.isAlive() && enemy.getPosition().distance(projectile.getPosition()) < 0.5) {
                                    enemy.receiveDamage(projectile.getDamage());
                                    toRemove.add(projectile);
                                    hit = true;
                                    break;
                                }
                            }
                            
                            if (!hit) {
                                // Check if expired (timer finished, target died, or reached destination)
                                if (projectile.isExpired()) {
                                    toRemove.add(projectile);
                                } 
                                // Check out of bounds
                                else if (currentMap != null && (projectile.getPosition().getX() < 0 || projectile.getPosition().getX() > currentMap.getMapWith() ||
                                         projectile.getPosition().getY() < 0 || projectile.getPosition().getY() > currentMap.getMapHight())) {
                                    toRemove.add(projectile);
                                }
                            }
                        }
                    }
                    for (Projectile p : toRemove) {
                        projectiles.remove(p);
                        synchronized (movementComponents) {
                            movementComponents.remove(p.getMovementComponent());
                        }
                    }
                }

                // 3. Update Towers
                synchronized (towers) {
                    synchronized (enemies) {
                        synchronized (projectiles) {
                            synchronized (movementComponents) {
                                towerUpdater.updateTowers(towers, enemies, projectiles, movementComponents);
                            }
                        }
                    }
                }

                // 4. Update Movements
                synchronized (movementComponents) {
                    movementUpdater.updatePosition(movementComponents);
                }

                // 5. Update Enemies and check for reached end
                synchronized (enemies) {
                    for (Enemy enemy : enemies) {
                        if (enemy.isAlive()) {
                            enemy.Update();
                        }
                    }
                    // Remove dead enemies or those who reached end
                    enemies.removeIf(enemy -> {
                        boolean dead = !enemy.isAlive();
                        boolean reachedEnd = enemy.reachedEnd();
                        if (dead || reachedEnd) {
                            if (dead && !reachedEnd && onEnemyKilled != null) {
                                onEnemyKilled.accept(enemy.getReward());
                            } else if (reachedEnd && onEnemyReachedEnd != null) {
                                onEnemyReachedEnd.accept(enemy.getDamage());
                            }
                            
                            synchronized (movementComponents) {
                                movementComponents.remove(enemy.getMovementComponent());
                            }

                            return true;
                        }
                        return false;
                    });
                }

                logicTimer = new StopWatch(32);
            }

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

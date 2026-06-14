import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import gameLogic.src.*;
import gameLogic.src.projectiles.Projectile;
import gameLogic.src.towers.Tower;
import graphics.src.*;

public final class Game {

    private static Game instance;
    private GameLogicLoop gameLogicLoop;
    private VisualLoop visualLoop;
    private final List<MovementComponent> movementComponents = Collections.synchronizedList(new ArrayList<MovementComponent>());
    private final List<Tower> towers = Collections.synchronizedList(new ArrayList<Tower>());
    private final List<Enemy> enemies = Collections.synchronizedList(new ArrayList<Enemy>());
    private final List<Projectile> projectiles = Collections.synchronizedList(new ArrayList<Projectile>());
    private boolean close = false;

    private Game() {
    }

    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }

        return instance;
    }

    public void run(){
        GraphicsEngine graphicsEngine = new GraphicsEngine();
        visualLoop = new VisualLoop(graphicsEngine,enemies,projectiles,towers,movementComponents);
        gameLogicLoop = new GameLogicLoop(enemies,towers,movementComponents,projectiles);
        visualLoop.setGameLogicLoop(gameLogicLoop);
        
        gameLogicLoop.start();

        while (!close) {
            if (visualLoop.isInMenu() || visualLoop.isPaused()) {
                gameLogicLoop.pauseLoop();
            } else {
                gameLogicLoop.resumeLoop();
            }
            visualLoop.update();

            try {
                Thread.sleep(32);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        gameLogicLoop.shutdown();
    }
}

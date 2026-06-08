import java.util.ArrayList;
import java.util.List;
import gameLogic.src.*;
import graphics.src.*;

public final class Game {

    private static Game instance;
    private GameLogicLoop gameLogicLoop;
    private VisualLoop visualLoop;
    private List<MovementComponent> movementComponents =  new ArrayList<MovementComponent>();
    private List<Tower> towers = new ArrayList<Tower>();
    private List<Enemy> enemies = new ArrayList<Enemy>();
    private List<Projectile> projectiles = new ArrayList<Projectile>();
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
        visualLoop = new VisualLoop(new GraphicsEngine(),enemies,projectiles,towers,movementComponents);
        gameLogicLoop = new GameLogicLoop(enemies,towers,movementComponents,projectiles);
        gameLogicLoop.start();

        while (!close) {
            if (visualLoop.isInMenu() || visualLoop.isPaused()) {
                gameLogicLoop.pauseLoop();
            } else {
                gameLogicLoop.resumeLoop();
            }
            visualLoop.update();

            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

        }

        gameLogicLoop.shutdown();

    };




}

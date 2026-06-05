import java.util.ArrayList;
import java.util.List;

public final class Game {

    private static Game instance;
    private GameLogicLoop  gameLogicLoop;
    private GraphicsLoop  graphicsLoop;
    private List<MovementComponent> movementComponents =  new ArrayList<MovementComponent>();
    private List<Tower> towers = new ArrayList<Tower>();
    private List<Enemy> enemies = new ArrayList<Enemy>();




    private Game() {
    }

    public static Game getInstance() {
        if (instance == null) {
            instance = new Game();
        }

        return instance;
    }


    public void run(){
        graphicsLoop = new GraphicsLoop(new GraphicsEngine());
        gameLogicLoop = new GameLogicLoop();
        boolean isInMenu = false;

        while (true) {
            gameLogicLoop.;


            while ()

        }


    };


}

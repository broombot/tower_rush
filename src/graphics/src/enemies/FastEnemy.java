package graphics.src.enemies;

import gameLogic.src.Enemy;
import gameLogic.src.Path;

public class FastEnemy extends Enemy {

    public FastEnemy(Path path, float difficultyScale ) {
        // Speed 0.15, HP 80
        super(0.15 * difficultyScale, path, Math.round(80 * difficultyScale), Math.round( 5 * difficultyScale), 30);
    }
}

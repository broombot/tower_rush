package graphics.src.enemies;

import gameLogic.src.Enemy;
import gameLogic.src.Path;

public class HeavyEnemy extends Enemy {

    public HeavyEnemy(Path path, float difficultyScale ) {
        // Speed 0.04, HP 400
        super(0.04 * difficultyScale, path, Math.round(400 * difficultyScale), Math.round( 15 * difficultyScale), 50);
    }
}

package graphics.src.enemies;

import gameLogic.src.Enemy;
import gameLogic.src.Path;

public class NormalEnemy extends Enemy {
    
    public NormalEnemy( Path path, float difficultyScale ) {
        // Speed 0.08, HP 150
        super(0.08 * difficultyScale, path, Math.round(150 * difficultyScale), Math.round( 5 * difficultyScale), 20);
    }
}

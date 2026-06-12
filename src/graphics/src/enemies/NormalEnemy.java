package graphics.src.enemies;

import gameLogic.src.Enemy;
import gameLogic.src.Path;
import graphics.src.GraphicsEntety;

import java.awt.*;

public class NormalEnemy extends Enemy {
    
    private GraphicsEntety graphics;

    public NormalEnemy( Path path, float difficultyScale ) {
        // Reduced speed to 0.1 tiles per frame scaled by difficulty
        super(0.1 * difficultyScale, path, Math.round(200 * difficultyScale), Math.round( 5 * difficultyScale));
        graphics = new GraphicsEntety(path.getPositionCord(0).getX(),path.getPositionCord(0).getY(),
                                      new Color(245, 255, 110), 20);
    }

    @Override
    public GraphicsEntety getGraphics() {
        return graphics;
    }
    
    @Override
    public void updateGraphics(){
        super.updateGraphics();
    }
}


package graphics.src.waveFactories;

import gameLogic.src.Enemy;
import gameLogic.src.Path;
import graphics.src.enemies.FastEnemy;
import graphics.src.enemies.HeavyEnemy;
import graphics.src.enemies.NormalEnemy;

import java.util.ArrayList;
import java.util.Collections;

public class EasyWaveFactory implements WaveFactory {

    private Path path;

    public EasyWaveFactory(Path path) {
        this.path = path;
    }

    @Override
    public Enemy[] generateWave(int wave) {
        ArrayList<Enemy> enemies = new ArrayList<Enemy>();
        
        int baseCount = 5 + (wave * 3);
        float difficultyScale = 0.8f + (wave * 0.05f); 

        if (wave < 5) {
            for (int i = 0; i < baseCount; i++) {
                enemies.add(new NormalEnemy(path, difficultyScale));
            }
        } else if (wave < 10) {
            int fastCount = wave;
            int normalCount = baseCount - fastCount;
            for (int i = 0; i < normalCount; i++) enemies.add(new NormalEnemy(path, difficultyScale));
            for (int i = 0; i < fastCount; i++) enemies.add(new FastEnemy(path, difficultyScale));
        } else {
            int heavyCount = wave - 5;
            int fastCount = wave;
            int normalCount = Math.max(0, baseCount - fastCount - heavyCount);
            for (int i = 0; i < normalCount; i++) enemies.add(new NormalEnemy(path, difficultyScale));
            for (int i = 0; i < fastCount; i++) enemies.add(new FastEnemy(path, difficultyScale));
            for (int i = 0; i < heavyCount; i++) enemies.add(new HeavyEnemy(path, difficultyScale));
        }

        Collections.shuffle(enemies);
        return enemies.toArray(Enemy[]::new);
    }
}

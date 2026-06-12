package graphics.src.waveFactories;

import gameLogic.src.Enemy;
import gameLogic.src.Path;
import graphics.src.enemies.FastEnemy;
import graphics.src.enemies.HeavyEnemy;
import graphics.src.enemies.NormalEnemy;

import java.util.ArrayList;
import java.util.Collections;

public class MediumWaveFactory implements WaveFactory {

    private Path path;

    public MediumWaveFactory(Path path) {
        this.path = path;
    }

    /**
     * @param wave
     * @return
     */
    @Override
    public Enemy[] generateWave(int wave) {
        ArrayList<Enemy> enemies = new ArrayList<Enemy>();

        for (int i = 0; i < wave + 1; i++) {
            if (wave < 5){
                for (int j = 0; j < 5 ; j++) {
                    enemies.add(new NormalEnemy(path, (float) wave /5));
                }
            } else if (wave < 10) {
                for (int j = 0; j < 4; j++) {
                    enemies.add(new NormalEnemy(path, (float) wave / 5));
                    enemies.add(new FastEnemy(path, (float) wave / 5));
                }
            } else if (wave < 15) {
                for (int j = 0; j < 4; j++) {
                    enemies.add(new HeavyEnemy(path, (float) wave / 5));
                    enemies.add(new NormalEnemy(path, (float) wave / 5));
                }
            }
            else {
                for (int j = 0; j < 5; j++) {
                    enemies.add(new HeavyEnemy(path,(float) wave/5));
                    enemies.add(new FastEnemy(path, (float) wave/5));
                }
            }

        }

        Collections.shuffle(enemies);
        return enemies.toArray(Enemy[]::new);
    }
}

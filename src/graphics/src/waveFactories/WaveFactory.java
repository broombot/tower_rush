package graphics.src.waveFactories;

import gameLogic.src.Enemy;

public interface WaveFactory {
    public Enemy[] generateWave(int wave);

}

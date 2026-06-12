package graphics.src.waveFactories;

import gameLogic.src.Path;
import graphics.src.Difficulty;

public class WaveFactorySelector {

    public static WaveFactory getWaveFactory(Difficulty difficulty, Path path) {
        if (difficulty == null) {
            return new MediumWaveFactory(path);
        }

        switch (difficulty) {
            case EASY:
                return new EasyWaveFactory(path);
            case NORMAL:
                return new MediumWaveFactory(path);
            case HARD:
                return new HardWaveFactory(path);
            default:
                return new MediumWaveFactory(path);
        }
    }
}
package towerrush.gameLogic;

import java.util.stream.Stream;

public class towerUpdater {

    public void  updatdateTower( Tower[] towers ){

        Stream.of(towers).forEach(tower -> {
            if (tower.getAttackTimer() == tower.getAttackTime()){
                tower.attack();
                tower.setAttackTimer(0);
            }else{
                tower.setAttackTimer(tower.getAttackTimer() + 1);
            }
        });


    }

}

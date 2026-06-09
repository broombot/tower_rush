package gameLogic.src;

import java.util.stream.Stream;

public class MovementUpdater {

    public void UpdatePosition( MovementComponent[] movementComponents){

        Stream.of(movementComponents).forEach(
                mc -> {
                   double distance = mc.getTarget().distance(new MapPoint(mc.getX(), mc.getY()));
                   if (mc.getSpeed() < distance) {
                       double dx = mc.getTarget().getX() - mc.getX();
                       double dy = mc.getTarget().getY() - mc.getY();
                       double angle = Math.atan2(dy, dx);
                       mc.setX(mc.getX() + mc.getSpeed() * Math.cos(angle));
                       mc.setY(mc.getY() + mc.getSpeed() * Math.sin(angle));
                   }else {
                       mc.setX(mc.getTarget().getX());
                       mc.setY(mc.getTarget().getY());
                   }
                }
        );

    }

}


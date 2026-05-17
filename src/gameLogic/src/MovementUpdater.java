import java.util.stream.Stream;

public class MovementUpdater {

    public void UpdatePosition( MovementComponent[] movementComponents){

        Stream.of(movementComponents).forEach(
                mc -> {
                   if (mc.getSpeed() < mc.getTarget().distance(new MapPoint(mc.getX(), mc.getY()))) {
                       mc.setX(mc.getX() + mc.getSpeed() * Math.cos(Math.atan(mc.getTarget().getY() / mc.getTarget().getX())));
                       mc.setY(mc.getY() + mc.getSpeed() * Math.sin(Math.atan(mc.getTarget().getY() / mc.getTarget().getX())));
                   }else {
                       mc.setX(mc.getTarget().getX());
                       mc.setY(mc.getTarget().getY());
                   }
                }
        );

    }

}

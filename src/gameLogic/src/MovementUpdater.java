package gameLogic.src;

import java.util.List;

public class MovementUpdater {

    public void updatePosition(List<MovementComponent> movementComponents) {
        if (movementComponents == null) return;

        synchronized (movementComponents) {
            for (MovementComponent mc : movementComponents) {
                double targetX = mc.getTarget().getX();
                double targetY = mc.getTarget().getY();
                double dx = targetX - mc.getX();
                double dy = targetY - mc.getY();
                double distance = Math.sqrt(dx * dx + dy * dy);

                if (distance <= mc.getSpeed() || distance == 0) {
                    mc.setX(targetX);
                    mc.setY(targetY);
                } else {
                    double ratio = mc.getSpeed() / distance;
                    mc.setX(mc.getX() + dx * ratio);
                    mc.setY(mc.getY() + dy * ratio);
                }
            }
        }
    }
}

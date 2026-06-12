package graphics.src.towers;

import gameLogic.src.towers.Cannon;
import gameLogic.src.Enemy;
import gameLogic.src.projectiles.Projectile;
import graphics.src.GraphicsEntety;
import graphics.src.towers.projectiles.CannonBallGraphics;

import java.awt.*;

public class GraphicsCannon extends Cannon {

    GraphicsEntety graphics;

    public GraphicsCannon(double tileX, double tileY, int pixelX, int pixelY) {
        super(tileX, tileY);
        this.graphics = new GraphicsEntety(0, 0, new Color(62, 62, 62), 30);
    }

    public GraphicsEntety getGraphics() {
        return graphics;
    }

    @Override
    public Projectile attack(Enemy target) {
        Projectile p = new CannonBallGraphics(position.getX(), position.getY(), target);
        if (map != null) p.setMap(map);
        return p;
    }
}


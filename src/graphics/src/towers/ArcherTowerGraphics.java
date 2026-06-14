package graphics.src.towers;

import gameLogic.src.towers.ArcherTower;
import gameLogic.src.Enemy;
import gameLogic.src.projectiles.Projectile;
import graphics.src.GraphicsEntety;
import graphics.src.towers.projectiles.ArrowGraphics;

import java.awt.Color;

public class ArcherTowerGraphics extends ArcherTower {
    private GraphicsEntety graphics;

    public ArcherTowerGraphics(double tileX, double tileY, int pixelX, int pixelY) {
        super(tileX, tileY);
        // Initially use 0,0 relative, will be updated when map is set
        this.graphics = new GraphicsEntety(0, 0, new Color(0, 80, 150), 30);
    }

    @Override
    public Projectile attack(Enemy target) {
        Projectile p = new ArrowGraphics(position.getX(), position.getY(), target,getDamage());
        return p;
    }

    public GraphicsEntety getGraphics() {
        return graphics;
    }
}


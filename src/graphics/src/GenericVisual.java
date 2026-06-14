package graphics.src;

import gameLogic.src.GameObject;
import gameLogic.src.Map;

/**
 * A generic visual wrapper that links a GameObject (logic) to a GraphicsEntety (Swing).
 */
public class GenericVisual implements EntityVisual {
    private final GameObject logicObject;
    private final GraphicsEntety graphics;

    public GenericVisual(GameObject logicObject, GraphicsEntety graphics) {
        this.logicObject = logicObject;
        this.graphics = graphics;
    }

    @Override
    public void update(Map map) {
        if (map == null || logicObject == null || graphics == null) return;
        
        double tileX = logicObject.getPosition().getX();
        double tileY = logicObject.getPosition().getY();
        
        double relX = (tileX * 100.0) / map.getMapWith();
        double relY = (tileY * 100.0) / map.getMapHight();
        
        graphics.setPosition(relX, relY);
    }

    @Override
    public GraphicsEntety getGraphicsEntety() {
        return graphics;
    }
}

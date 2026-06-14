package graphics.src;

import gameLogic.src.Map;

/**
 * Interface for visual components that track a game logic entity.
 * This decouples the graphics from the game logic.
 */
public interface EntityVisual {
    /**
     * Update the visual s position and state based on the logic entity.
     * @param map The current game map for coordinate conversion.
     */
    void update(Map map);

    /**
     * Get the Swing component for rendering.
     */
    GraphicsEntety getGraphicsEntety();
}

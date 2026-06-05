import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;

/**
 * Handles placing towers on the map by clicking on PLACEABLE tiles.
 * Attach to a GamePanel and register a listener to respond to placements.
 */
public class TowerPlacer {

    public interface TowerPlacedListener {
        /** Called when the user successfully places a tower. */
        void onTowerPlaced(int tileCol, int tileRow, int pixelX, int pixelY);
    }

    private final GamePanel gamePanel;
    private Map map;
    private boolean active = false;
    private final List<TowerPlacedListener> listeners = new ArrayList<>();

    // Tracks which tiles already have a tower so we don't double-place.
    private boolean[][] occupied;

    public TowerPlacer(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        gamePanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (active) handleClick(e.getX(), e.getY());
            }
        });
    }

    /** Supply (or update) the map that is currently loaded. */
    public void setMap(Map map) {
        this.map = map;
        this.occupied = new boolean[map.getMapHight()][map.getMapWith()];
    }

    /** Enable / disable the placer (e.g. toggle with a button). */
    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public void addListener(TowerPlacedListener listener) {
        listeners.add(listener);
    }



    private void handleClick(int pixelX, int pixelY) {
        if (map == null) return;

        int panelWidth  = gamePanel.getWidth();
        int panelHeight = gamePanel.getHeight();
        int cols = map.getMapWith();
        int rows = map.getMapHight();

        // Tile dimensions as rendered (may differ from tileSize field when panel is resized)
        int tileW = (int) Math.ceil((double) panelWidth  / cols);
        int tileH = (int) Math.ceil((double) panelHeight / rows);

        int col = pixelX / tileW;
        int row = pixelY / tileH;

        // Bounds check
        if (col < 0 || col >= cols || row < 0 || row >= rows) return;

        // Only allow placement on PLACEABLE tiles
        TileType tile = map.getMap()[row][col];
        if (tile != TileType.PLACEABLE) return;

        // Prevent placing two towers on the same tile
        if (occupied[row][col]) return;

        // Top-left pixel of the tile, centred for the entity
        int px = col * tileW;
        int py = row * tileH;

        showPlaceMenu(col, row, px, py, pixelX, pixelY);
    }

    private void showPlaceMenu(int tileCol, int tileRow, int tilePixelX, int tilePixelY, int clickX, int clickY) {
        JPopupMenu placeMenu = new JPopupMenu();

        JMenuItem placeArcher = new JMenuItem("Place archer");
        placeArcher.addActionListener(e -> {
            if (occupied[tileRow][tileCol]) return;
            occupied[tileRow][tileCol] = true;

            for (TowerPlacedListener listener : listeners) {
                listener.onTowerPlaced(tileCol, tileRow, tilePixelX, tilePixelY);
            }
        });

        JMenuItem cancel = new JMenuItem("Annuleer");
        cancel.addActionListener(e -> placeMenu.setVisible(false));

        placeMenu.add(placeArcher);
        placeMenu.addSeparator();
        placeMenu.add(cancel);

        SwingUtilities.invokeLater(() -> placeMenu.show(gamePanel, clickX, clickY));
    }
}

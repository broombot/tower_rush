package graphics.src;

import gameLogic.src.Map;
import gameLogic.src.TileType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel {

    private final int originalTileSize = 16;
    private double scale = 2;
    private Map map;

    private int tileSize = (int)(originalTileSize * scale);
    private final int screenCol = 50;
    private final int screenRow = 25;
    private int screenWidth = tileSize * screenCol;
    private int screenHeight = tileSize * screenRow;
    private List<GraphicsEntety> enties = new ArrayList<GraphicsEntety>();

    private Color placebleColor;
    private Color pathColor;
    private Color projectileBlockingColor;
    private Color blockedColor;

    public GamePanel(Color blockedColor, Color projectileBlockingColor, Color pathColor, Color placebleColor, double scale) {
        this.blockedColor = blockedColor;
        this.projectileBlockingColor = projectileBlockingColor;
        this.pathColor = pathColor;
        this.placebleColor = placebleColor;
        this.scale = scale;
        setLayout(null);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateAllEntityBounds();
                repaint();
            }
        });
        reScale();
    }

    public void reScale(){
        tileSize = (int)(originalTileSize * scale);
        screenWidth = tileSize * screenCol;
        screenHeight = tileSize * screenRow;
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        updateAllEntityBounds();
    };

    public void setMap(Map map){
        this.map = map;
    }

    public void addEntety(GraphicsEntety entety){
        synchronized (enties) {
            enties.add(entety);
        }
        updateEntityBounds(entety);
        this.add(entety);
        this.revalidate();
        this.repaint();
    }

    public void clearEnteties() {
        this.removeAll();
        synchronized (enties) {
            enties.clear();
        }
        this.revalidate();
        this.repaint();
    }

    public void updateAllEntityBounds() {
        synchronized (enties) {
            for (GraphicsEntety entety : enties) {
                updateEntityBounds(entety);
            }
        }
    }

    public void updateEntityBounds(GraphicsEntety entety) {
        int referenceWidth = getWidth();
        if (referenceWidth <= 0) referenceWidth = getPreferredSize().width;
        int referenceHeight = getHeight();
        if (referenceHeight <= 0) referenceHeight = getPreferredSize().height;

        if (referenceWidth <= 0 || referenceHeight <= 0) {
            return;
        }

        int x = (int) Math.round(referenceWidth * entety.getRelativex() / 100.0);
        int y = (int) Math.round(referenceHeight * entety.getRelativey() / 100.0);
        entety.setBounds(x, y, entety.getEntetySize(), entety.getEntetySize());
    }

    @Override
    public void paintComponent(Graphics g ){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        if(map != null){
            int mapW = map.getMapWith();
            int mapH = map.getMapHight();
            int tileWith = (int) Math.ceil((double) getWidth() / mapW);
            int tileHight = (int) Math.ceil((double) getHeight() / mapH);

            for (int i = 0; i < mapH ; i++) {
                for (int j = 0; j < mapW ; j++) {
                    TileType type = map.getMap()[i][j];
                    if (type == null) {
                        g2.setColor(Color.RED);
                    } else {
                        switch (type){
                            case PLACEABLE:
                                g2.setColor(placebleColor);
                                break;
                            case PATH1:case PATH2:case PATH3:case INTERSECTION:
                                g2.setColor(pathColor);
                                break;
                            case PROJECTILE_BLOCKING:
                                g2.setColor(projectileBlockingColor);
                                break;
                            case BLOCKED:
                                g2.setColor(blockedColor);
                                break;
                            default:
                                g2.setColor(Color.RED);
                        }
                    }
                    g2.fillRect(j * tileWith, i * tileHight, tileWith, tileHight);
                }
            }
        }
    }

    public void removeEntety(GraphicsEntety entety){
        this.remove(entety);
        synchronized (enties) {
            enties.remove(entety);
        }
        this.revalidate();
    }

    public int getScreenWidth() {
        return getWidth() > 0 ? getWidth() : screenWidth;
    }

    public int getScreenHeight() {
        return getHeight() > 0 ? getHeight() : screenHeight;
    }

    public int getOriginalTileSize() {
        return originalTileSize;
    }

    public double getScale() {
        return scale;
    }

    public void setScale(double scale) {
        this.scale = scale;
        reScale();
    }

    public int getTileSize() {
        return tileSize;
    }
}

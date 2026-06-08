package graphics.src;

import gameLogic.src.Map;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


public class GamePanel extends JPanel {



    private final int originalTileSize = 16;
    private double scale = 2;
    private Map map;

    private int tileSize = (int)(originalTileSize * scale);
    private final int screenCol = 50;
    private final int screenRow = 25;
    private int screenWidth = tileSize * screenCol;
    private int screenHeight = tileSize * screenRow;
    private List<GraphicsEntety> enties =  new ArrayList<GraphicsEntety>();

    private Color placebleColor;
    private Color pathColor;
    private Color projectileBlockingColor;
    private Color blockedColor;


    /**
     * @param blockedColor
     * @param projectileBlockingColor
     * @param pathColor
     * @param placebleColor
     * @param scale
     */

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
                updateEntityBounds();
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
        updateEntityBounds();
        enties.stream().forEach(ent -> {ent.setBounds((int) Math.round(screenWidth * ent.getRelativex()/100),
                (int) Math.round( screenHeight * ent.getRelativey()/100),ent.getEntetySize(),ent.getEntetySize());});
    };


    public void setMap(Map map){
        this.map = map;
    }


    /**
     * @param entety
     */
    public void addEntety(GraphicsEntety entety){
        enties.add(entety);
        storeRelativePosition(entety);
        updateEntityBounds(entety);
        this.add(entety);
        this.revalidate();
        this.repaint();
    }

    public void clearEnteties() {
        this.removeAll();
        enties.clear();
        this.revalidate();
        this.repaint();
    }

    private void storeRelativePosition(GraphicsEntety entety) {
        int referenceWidth = getReferenceWidth();
        int referenceHeight = getReferenceHeight();

        if (referenceWidth <= 0 || referenceHeight <= 0) {
            return;
        }

        double relativeX = entety.getRelativex() * 100.0 / referenceWidth;
        double relativeY = entety.getRelativey() * 100.0 / referenceHeight;
        entety.setPosition(relativeX, relativeY);
    }

    private void updateEntityBounds() {
        for (GraphicsEntety entety : enties) {
            updateEntityBounds(entety);
        }
    }

    private void updateEntityBounds(GraphicsEntety entety) {
        int referenceWidth = getReferenceWidth();
        int referenceHeight = getReferenceHeight();

        if (referenceWidth <= 0 || referenceHeight <= 0) {
            return;
        }

        int x = (int) Math.round(referenceWidth * entety.getRelativex() / 100.0);
        int y = (int) Math.round(referenceHeight * entety.getRelativey() / 100.0);
        entety.setBounds(x, y, entety.getEntetySize(), entety.getEntetySize());
    }

    private int getReferenceWidth() {
        int currentWidth = getWidth();
        if (currentWidth > 0) {
            return currentWidth;
        }

        if (getPreferredSize() != null) {
            return getPreferredSize().width;
        } else {
            return screenWidth;
        }
    }

    private int getReferenceHeight() {
        int currentHeight = getHeight();
        if (currentHeight > 0) {
            return currentHeight;
        }
        if (getPreferredSize() != null) {
            return getPreferredSize().height;
        } else {
            return screenHeight;
        }
    }

    public void paintComponent(Graphics g ){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;



        if(map != null){

            int tileWith = (int) Math.ceil((double) getWidth() / map.getMapWith());
            int tileHight = (int) Math.ceil((double) getHeight() / map.getMapHight());


            for (int i = 0; i < map.getMapHight() ; i++) {
                for (int j = 0; j < map.getMapWith() ; j++) {

                    switch (map.getMap()[i][j]){
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
                        case null, default:
                            g2.setColor(Color.RED);
                    }
                    g2.fillRect(j * tileWith, i * tileHight, tileWith, tileHight);

                }
            }
        }else{
            System.out.println("no map found");
        }

    }

    public void removeEntety(GraphicsEntety entety){
        this.remove(entety);
        enties.remove(entety);
        this.revalidate();
    }

    public int getScreenWidth() {
        return getReferenceWidth();
    }


    public int getScreenHeight() {
        return getReferenceHeight();
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

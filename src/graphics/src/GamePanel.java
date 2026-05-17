

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    private final int originalTileSize = 16;
    private double scale = 2;
    private Map map;

    private int tileSize = (int)(originalTileSize * scale);
    private final int screenCol = 50;
    private final int screenRow = 25;
    private int screenWidth = tileSize * screenCol;
    private int screenHeight = tileSize * screenRow;

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
        reScale();
    }

    public void reScale(){
        tileSize = (int)(originalTileSize * scale);
        screenWidth = tileSize * screenCol;
        screenHeight = tileSize * screenRow;
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
    };


    public void setMap(Map map){
        this.map = map;
    }


    public void addEntety(GraphicsEntety entety){

        entety.setBounds(entety.getX(),entety.getY(),entety.getEntetySize(),entety.getEntetySize());
        this.add(entety);
        this.revalidate();

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
        this.revalidate();
    }

    public int getScreenWidth() {
        return screenWidth;
    }


    public int getScreenHeight() {
        return screenHeight;
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

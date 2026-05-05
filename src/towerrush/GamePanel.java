package towerrush;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    private final int originalTileSize = 16;
    private int scale = 2;
    private Map map;

    private int tileSize = originalTileSize * scale;
    private final int screenCol = 50;
    private final int screenRow = 25;
    private int screenWidth = tileSize * screenCol;
    private int screenHeight = tileSize * screenRow;

    private Color placebleColor;
    private Color pathColor;
    private Color projectileBlockingColor;
    private Color blockedColor;



    Thread gameThread;

    public GamePanel(Color blockedColor, Color projectileBlockingColor, Color pathColor, Color placebleColor, int screenHeight, int screenWidth) {
        this.blockedColor = blockedColor;
        this.projectileBlockingColor = projectileBlockingColor;
        this.pathColor = pathColor;
        this.placebleColor = placebleColor;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;
    }

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


            float colomScale = Math.max(1, screenCol/ map.getMapWith());
            float rowScale = Math.max(1,screenRow/ map.getMapHight());

            int tileWith = Math.round( tileSize * colomScale );
            int tileHight = Math.round( tileSize * rowScale );



            for (int i = 0; i < map.getMapHight() ; i++) {
                for (int j = 0; j < map.getMapWith() ; j++) {

                    switch (map.getMap()[i][j]){
                        case PLACEABLE:
                            g2.setColor(new Color(26,153,12));
                            break;
                        case PATH1:case PATH2:case PATH3:case INTERSECTION:
                            g2.setColor(new Color(156, 94, 0));
                            break;
                        case PROJECTILE_BLOCKING:
                            g2.setColor(new Color(73, 73, 73));
                            break;
                        case BLOCKED:
                            g2.setColor(new Color(0, 132, 189));
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

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public int getTileSize() {
        return tileSize;
    }


}

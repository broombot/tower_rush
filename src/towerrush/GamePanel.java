package towerrush;

import javax.swing.*;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ImageObserver;
import java.awt.image.RenderedImage;
import java.awt.image.renderable.RenderableImage;
import java.text.AttributedCharacterIterator;

public class GamePanel extends JPanel {

    final int originalTileSize = 16;
    int scale = 2;
    private Map map;

    int tileSize = originalTileSize * scale;
    final int screenCol = 50;
    final int screenRow = 25;
    int screenWidth = tileSize * screenCol;
    int screenHeight = tileSize * screenRow;

    Thread gameThread;

    public GamePanel(){

        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setDoubleBuffered(true);
        this.setBackground(Color.black);
        this.setLayout(null);

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

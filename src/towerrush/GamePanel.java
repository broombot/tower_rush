package towerrush;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    final int originalTileSize = 16;
    int scale = 1;

    int tileSize = originalTileSize * scale;
    final int screenCol = 80;
    final int screenRow = 60;
    int screenWidth = tileSize * screenCol;
    int screenHeight = tileSize * screenRow;

    Thread gameThread;

    public GamePanel(){

        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setDoubleBuffered(true);
        this.setBackground(Color.black);
        this.setLayout(null);

    }
    public void addEntety(GraphicsEntety entety){

        entety.setBounds(entety.getX(),entety.getY(),entety.getEntetySize(),entety.getEntetySize());
        this.add(entety);
        this.revalidate();

    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
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

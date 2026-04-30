package towerrush;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GraphicsEntety extends JLabel {

    private int x;
    private int y;
    private int entetySize ;
    private Color color;

    public GraphicsEntety(BufferedImage image, int x, int y,int size) {
        this.entetySize = size;
        Image scaled = image.getScaledInstance(size,size,Image.SCALE_DEFAULT);
        super(new ImageIcon(scaled));
        this.x = x;
        this.y = y;
    }

    public GraphicsEntety(int x, int y, Color color) {
        this.x = x;
        this.y = y;
        this.color = color;
        this.entetySize = 50;

    }

    @Override
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }


    public Color getColor() {
        return color;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(color);
        g2.fillOval(0,0,entetySize, entetySize);


        super.paintComponent(g);
    }

    public int getEntetySize() {
        return entetySize;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public  void setPosition(int x,int y){
        this.x = x;
        this.y = y;
    }
}

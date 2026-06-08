package graphics.src;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GraphicsEntety extends JLabel {
    // Position stored as a percentage of the current GamePanel size.

    private double relativex;
    private double relativey;
    private int entetySize ;
    private Color color;

    public GraphicsEntety(BufferedImage image, double x, double y,int size) {
        super();
        this.entetySize = size;
        Image scaled = image.getScaledInstance(size,size,Image.SCALE_DEFAULT);
        setIcon(new ImageIcon(scaled));
        this.relativex = x;
        this.relativey = y;
    }

    public GraphicsEntety(double x, double y, Color color,int size) {
        this.relativex = x;
        this.relativey = y;
        this.color = color;
        this.entetySize = size;
    }

    public GraphicsEntety(double x, double y, Color color) {
        this.relativex = x;
        this.relativey = y;
        this.color = color;
        this.entetySize = 50;

    }




    public double getRelativex() {
        return relativex;
    }


    public double getRelativey() {
        return relativey;
    }


    public Color getColor() {
        return color;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (color != null) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setColor(color);
            g2.fillOval(0,0,entetySize, entetySize);
        }
    }

    public int getEntetySize() {
        return entetySize;
    }

    public void setX(int x) {
        this.relativex = x;
    }

    public void setY(double y) {
        this.relativey = y;
    }

    public  void setPosition(double x,double y){
        this.relativex = x;
        this.relativey = y;
    }
}

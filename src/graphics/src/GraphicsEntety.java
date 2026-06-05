import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class GraphicsEntety extends JLabel {
    // relative cordinates are % of the screen with and hight from 0 to 10

    private double relativex;
    private double relativey;
    private int entetySize ;
    private Color color;

    public GraphicsEntety(BufferedImage image, double x, double y,int size) {
        this.entetySize = size;
        Image scaled = image.getScaledInstance(size,size,Image.SCALE_DEFAULT);
        super(new ImageIcon(scaled));
        this.relativex = Math.max(Math.min(x,0),100) ;
        this.relativey = Math.max(Math.min(y,0),100) ;
    }

    public GraphicsEntety(double x, double y, Color color,int size) {
        this.relativex = Math.max(Math.min(x,0),100);
        this.relativey = Math.max(Math.min(y,0),100);
        this.color = color;
        this.entetySize = size;
    }

    public GraphicsEntety(double x, double y, Color color) {
        this.relativex = Math.max(Math.min(x,0),100) ;
        this.relativey = Math.max(Math.min(y,0),100) ;
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
        Graphics2D g2 = (Graphics2D) g;

        g2.setColor(color);
        g2.fillOval(0,0,entetySize, entetySize);


        super.paintComponent(g);
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

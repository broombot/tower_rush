
import javax.swing.*;

public class GraphicsEngine implements Runnable {

    private boolean inMenu;
    private GamePanel gamePanel;
    private MenuPanel menuPanel;
    private Thread renderTread;
    private VisualsFactory visualsFactory = new OriginalVisualsFactory();

    public void setVisualsFactory(VisualsFactory visualsFactory) {
        this.visualsFactory = visualsFactory;
    }

    public GraphicsEngine() {

        inMenu = false;

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setTitle("tower rush");

        gamePanel = visualsFactory.creatGamePanel();
        menuPanel = visualsFactory.creatMenuPanel();
        window.add(gamePanel);
        window.pack();

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        window.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                stopRenderThread();
            }
        });

    }

    private void draw(){
        if (!inMenu){
            gamePanel.repaint();
        }else{
            menuPanel.repaint();
        }
    }

    public void addMap(Map map){
        gamePanel.setMap(map);
    }

    public void addEntety(GraphicsEntety entety){

        gamePanel.addEntety(entety);

    }



    public void startRenderTread(){
        renderTread = new Thread(this);
        renderTread.start();
    }

    public void stopRenderThread(){
        this.renderTread = null;
    }



    @Override
    public void run() {
        while (renderTread != null) {
            draw();
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                System.out.println("\t\\u001B[38m \\u001B[41m tread was interrupted wile sleping");
                System.out.println(e.getMessage() + "\\u001B[0m \\u001B[0m");
            }
        }
    }

    public void removeEntety(GraphicsEntety entety){
        gamePanel.removeEntety(entety);
    }
}

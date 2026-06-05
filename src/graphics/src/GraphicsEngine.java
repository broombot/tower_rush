import javax.swing.*;

public class GraphicsEngine implements Runnable {

    private boolean inMenu;
    private GamePanel gamePanel;
    private MenuPanel menuPanel;
    private Thread renderTread;
    private VisualsFactory visualsFactory = new OriginalVisualsFactory();

    // Tower placer is created once and reused when a map is loaded.
    private TowerPlacer towerPlacer;

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

        // Create the placer once; it attaches its own mouse listener to gamePanel.
        towerPlacer = new TowerPlacer(gamePanel);
    }

    private void draw(){
        if (!inMenu){
            gamePanel.repaint();
        }else{
            menuPanel.repaint();
        }
    }

    /**
     * Load a map into the game panel and the tower placer.
     */
    public void addMap(Map map){
        gamePanel.setMap(map);
        towerPlacer.setMap(map);
    }

    public void addEntety(GraphicsEntety entety){
        gamePanel.addEntety(entety);
    }

    // ---------------------------------------------------------------
    // Tower-placer API
    // ---------------------------------------------------------------

    /**
     * Activate or deactivate tower-placement mode.
     * While active, clicking a PLACEABLE tile calls every registered listener.
     */
    public void setTowerPlacerActive(boolean active) {
        towerPlacer.setActive(active);
    }

    public boolean isTowerPlacerActive() {
        return towerPlacer.isActive();
    }

    /**
     * Register a callback that is fired whenever the player successfully
     * places a tower.  The Game class should use this to spawn the actual
     * tower entity.
     */
    public void addTowerPlacedListener(TowerPlacer.TowerPlacedListener listener) {
        towerPlacer.addListener(listener);
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
                System.out.println("\t\u001B[38m \u001B[41m thread was interrupted while sleeping");
                System.out.println(e.getMessage() + "\u001B[0m \u001B[0m");
            }
        }
    }

    public void removeEntety(GraphicsEntety entety){
        gamePanel.removeEntety(entety);
    }
}

package graphics.src;
import gameLogic.src.Map;
import gameLogic.src.LevelCatalog;

import java.awt.event.KeyListener;
import java.util.List;
import javax.swing.*;

public class GraphicsEngine implements Runnable {

    private volatile boolean inMenu;
    private GamePanel gamePanel;
    private MenuPanel menuPanel;
    private volatile Thread renderTread;
    private VisualsFactory visualsFactory = new OriginalVisualsFactory();
    JFrame window;

    // Tower placer is created once and reused when a map is loaded.
    private TowerPlacer towerPlacer;

    public void setVisualsFactory(VisualsFactory visualsFactory) {
        this.visualsFactory = visualsFactory;
    }

    public GraphicsEngine() {

        inMenu = false;

        window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(true);
        window.setTitle("tower rush");

        gamePanel = visualsFactory.creatGamePanel();
        menuPanel = visualsFactory.creatMenuPanel();
        gamePanel.setFocusable(true);
        menuPanel.setFocusable(true);
        menuPanel.setPreferredSize(gamePanel.getPreferredSize());
        window.setContentPane(gamePanel);
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

    public void setInMenu(boolean inMenu) {
        if (this.inMenu == inMenu) {
            return;
        }

        this.inMenu = inMenu;
        SwingUtilities.invokeLater(() -> {
            window.setContentPane(inMenu ? menuPanel : gamePanel);
            window.revalidate();
            window.repaint();
            if (inMenu) {
                menuPanel.requestFocusInWindow();
            } else {
                gamePanel.requestFocusInWindow();
            }
        });
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

    public void clearGameEntities() {
        gamePanel.clearEnteties();
    }


    /**
     * Activate or deactivate tower-placement mode.
     * While active, clicking a PLACEABLE tile calls every registered listener.
     */
    public void setTowerPlacerActive(boolean active) {
        towerPlacer.setActive(active);
    }

    public void setMenuActions(Runnable primaryAction, Runnable secondaryAction, Runnable quitAction) {
        menuPanel.setPrimaryAction(primaryAction);
        menuPanel.setSecondaryAction(secondaryAction);
        menuPanel.setQuitAction(quitAction);
    }

    public void setAvailableLevels(List<LevelCatalog.LevelEntry> levels) {
        menuPanel.setAvailableLevels(levels);
    }

    public String getSelectedLevelResource() {
        return menuPanel.getSelectedLevelResource();
    }

    public void showStartScreen() {
        menuPanel.showStartScreen();
    }

    public void showPauseScreen() {
        menuPanel.showPauseScreen();
    }

    public void registerKeyListener(KeyListener listener) {
        window.addKeyListener(listener);
        gamePanel.addKeyListener(listener);
        menuPanel.addKeyListener(listener);

        SwingUtilities.invokeLater(() -> gamePanel.requestFocusInWindow());
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

    public int getScreenWidth(){
        return gamePanel.getScreenWidth();
    }

    public int getScreenHeight(){
        return gamePanel.getScreenHeight();
    }

    public void stopRenderThread(){
        this.renderTread = null;
    }

    public void shutdown() {
        stopRenderThread();
        window.dispose();
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

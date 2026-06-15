package graphics.src;
import gameLogic.src.Map;
import gameLogic.src.LevelCatalog;

import java.awt.event.KeyListener;
import java.util.List;
import javax.swing.*;

/**
 * The GraphicsEngine is responsible for managing the game"s window,
 * switching between the menu and the game panel, and running the main render thread.
 * It serves as the bridge between the visual representation and the game logic.
 */
public class GraphicsEngine implements Runnable {

    /** Flag indicating if the engine is currently showing the menu. */
    private volatile boolean inMenu;
    /** The main gameplay panel. */
    private GamePanel gamePanel;
    /** The main menu panel. */
    private MenuPanel menuPanel;
    /** The thread dedicated to rendering at a consistent frame rate. */
    private volatile Thread renderTread;
    /** The factory used to create visual components for game entities. */
    private VisualsFactory visualsFactory = new OriginalVisualsFactory();
    /** The main window frame of the application. */
    JFrame window;

    /** Component responsible for handling tower placement logic on the map. */
    private TowerPlacer towerPlacer;

    /**
     * Sets a custom visuals factory for creating entities and panels.
     * @param visualsFactory The factory to use.
     */
    public void setVisualsFactory(VisualsFactory visualsFactory) {
        this.visualsFactory = visualsFactory;
    }

    /**
     * Gets the currently active visuals factory.
     * @return The active visuals factory.
     */
    public VisualsFactory getVisualsFactory() {
        return visualsFactory;
    }

    /**
     * Initializes the GraphicsEngine, creates the main window, and sets up the panels.
     */
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

    /**
     * The internal draw method called by the render thread.
     * Updates entity bounds and repaints the active panel.
     */
    private void draw(){
        if (!inMenu){
            gamePanel.updateAllEntityBounds();
            gamePanel.repaint();
        }else{
            menuPanel.repaint();
        }
    }

    /**
     * Switches the view between the menu and the gameplay panel.
     * @param inMenu True to show the menu, false to show the game.
     */
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
     * Updates the Heads-Up Display with the current player stats.
     * @param lives The remaining lives of the player.
     * @param money The current amount of money available.
     */
    public void updateHUD(int lives, int money) {
        gamePanel.setStats(lives, money);
    }

    /**
     * Loads a map into the game panel and initializes the tower placer with it.
     * @param map The map to load.
     */
    public void addMap(Map map){
        gamePanel.setMap(map);
        towerPlacer.setMap(map);
    }

    /**
     * Adds a visual representation of an entity to the game panel.
     * @param visual The EntityVisual to add.
     */
    public void addVisual(EntityVisual visual){
        gamePanel.addVisual(visual);
    }

    /**
     * Removes a visual representation of an entity from the game panel.
     * @param visual The EntityVisual to remove.
     */
    public void removeVisual(EntityVisual visual) {
        gamePanel.removeVisual(visual);
    }

    /**
     * Clears all entities from the game panel and resets the tower placer map.
     */
    public void clearGameEntities() {
        gamePanel.clearEnteties();
        towerPlacer.setMap(null);
    }

    /**
     * Toggles the tower placement mode.
     * @param active True to enable placement, false to disable.
     */
    public void setTowerPlacerActive(boolean active) {
        towerPlacer.setActive(active);
    }

    /**
     * Sets the actions to be performed by the menu buttons.
     * @param primaryAction Action for the start/resume button.
     * @param secondaryAction Action for the back button.
     * @param quitAction Action for the quit button.
     */
    public void setMenuActions(Runnable primaryAction, Runnable secondaryAction, Runnable quitAction) {
        menuPanel.setPrimaryAction(primaryAction);
        menuPanel.setSecondaryAction(secondaryAction);
        menuPanel.setQuitAction(quitAction);
    }

    /**
     * Sets the list of levels available for selection in the menu.
     * @param levels The list of level entries.
     */
    public void setAvailableLevels(List<LevelCatalog.LevelEntry> levels) {
        menuPanel.setAvailableLevels(levels);
    }

    /**
     * Retrieves the resource path of the currently selected level in the menu.
     * @return The level resource path.
     */
    public String getSelectedLevelResource() {
        return menuPanel.getSelectedLevelResource();
    }

    /**
     * Retrieves the currently selected difficulty in the menu.
     * @return The selected difficulty.
     */
    public Difficulty getSelectedDifficulty() {
        return menuPanel.getSelectedDifficulty();
    }

    /**
     * Shows the initial start screen on the menu panel.
     */
    public void showStartScreen() {
        menuPanel.showStartScreen();
    }

    /**
     * Shows the pause screen on the menu panel.
     */
    public void showPauseScreen() {
        menuPanel.showPauseScreen();
    }

    /**
     * Registers a key listener to the window and all major panels.
     * @param listener The KeyListener to register.
     */
    public void registerKeyListener(KeyListener listener) {
        window.addKeyListener(listener);
        gamePanel.addKeyListener(listener);
        menuPanel.addKeyListener(listener);

        SwingUtilities.invokeLater(() -> gamePanel.requestFocusInWindow());
    }


    /**
     * Checks if the tower placer is currently active.
     * @return True if active, false otherwise.
     */
    public boolean isTowerPlacerActive() {
        return towerPlacer.isActive();
    }

    /**
     * Registers a listener that is notified when a tower is successfully placed.
     * @param listener The listener to add.
     */
    public void addTowerPlacedListener(TowerPlacer.TowerPlacedListener listener) {
        towerPlacer.addListener(listener);
    }

    /**
     * Starts the render thread to begin drawing frames.
     */
    public void startRenderTread(){
        renderTread = new Thread(this);
        renderTread.start();
    }

    /**
     * Gets the current width of the gameplay screen.
     * @return The screen width in pixels.
     */
    public int getScreenWidth(){
        return gamePanel.getScreenWidth();
    }

    /**
     * Gets the current height of the gameplay screen.
     * @return The screen height in pixels.
     */
    public int getScreenHeight(){
        return gamePanel.getScreenHeight();
    }

    /**
     * Stops the render thread.
     */
    public void stopRenderThread(){
        this.renderTread = null;
    }

    /**
     * Shuts down the engine, stops the render thread, and disposes of the window.
     */
    public void shutdown() {
        stopRenderThread();
        window.dispose();
    }

    /**
     * The main loop of the render thread.
     */
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
}

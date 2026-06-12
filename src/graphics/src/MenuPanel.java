package graphics.src;

import gameLogic.src.LevelCatalog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.List;

public class MenuPanel extends JPanel {



    private final JLabel titleLabel;
    private final JLabel subtitleLabel;

    private final JLabel levelLabel;
    private final JComboBox<LevelCatalog.LevelEntry> levelSelector;
    private final JPanel levelSelectionPanel;

    // Nieuwe UI-componenten voor de moeilijkheidsgraad
    private final JLabel difficultyLabel;
    private final JComboBox<Difficulty> difficultySelector;
    private final JPanel difficultySelectionPanel;

    private final JButton primaryButton;
    private final JButton secondaryButton;
    private final JButton quitButton;

    private Runnable primaryAction = () -> {};
    private Runnable secondaryAction = () -> {};
    private Runnable quitAction = () -> {};
    private boolean startScreenVisible = true;

    public MenuPanel() {
        setOpaque(true);
        setLayout(new GridBagLayout());
        setBackground(new Color(14, 18, 28));

        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setOpaque(true);
        card.setBackground(new Color(22, 28, 40));
        card.setBorder(new EmptyBorder(36, 44, 36, 44));

        titleLabel = new JLabel("Tower Rush");
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 36));
        titleLabel.setForeground(new Color(242, 245, 250));

        subtitleLabel = new JLabel("Press start to play");
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subtitleLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        subtitleLabel.setForeground(new Color(176, 186, 204));

        // --- LEVEL SELECTION PANEL ---
        levelLabel = new JLabel("Select a level");
        levelLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        levelLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        levelLabel.setForeground(new Color(176, 186, 204));

        levelSelector = new JComboBox<>();
        levelSelector.setAlignmentX(Component.CENTER_ALIGNMENT);
        levelSelector.setMaximumSize(new Dimension(220, 32));
        levelSelector.setPreferredSize(new Dimension(220, 32));
        levelSelector.setFocusable(false);
        levelSelector.setBackground(new Color(34, 40, 54));
        levelSelector.setForeground(Color.WHITE);
        levelSelector.setOpaque(true);

        levelSelectionPanel = new JPanel();
        levelSelectionPanel.setOpaque(false);
        levelSelectionPanel.setLayout(new BoxLayout(levelSelectionPanel, BoxLayout.Y_AXIS));
        levelSelectionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        levelSelectionPanel.add(levelLabel);
        levelSelectionPanel.add(Box.createVerticalStrut(6));
        levelSelectionPanel.add(levelSelector);

        // --- DIFFICULTY SELECTION PANEL ---
        difficultyLabel = new JLabel("Select Difficulty");
        difficultyLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        difficultyLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        difficultyLabel.setForeground(new Color(176, 186, 204));

        difficultySelector = new JComboBox<>(Difficulty.values()); // Vul de combobox met Easy, Normal, Hard
        difficultySelector.setSelectedItem(Difficulty.NORMAL);     // Standaard op Normal zetten
        difficultySelector.setAlignmentX(Component.CENTER_ALIGNMENT);
        difficultySelector.setMaximumSize(new Dimension(220, 32));
        difficultySelector.setPreferredSize(new Dimension(220, 32));
        difficultySelector.setFocusable(false);
        difficultySelector.setBackground(new Color(34, 40, 54));
        difficultySelector.setForeground(Color.WHITE);
        difficultySelector.setOpaque(true);

        difficultySelectionPanel = new JPanel();
        difficultySelectionPanel.setOpaque(false);
        difficultySelectionPanel.setLayout(new BoxLayout(difficultySelectionPanel, BoxLayout.Y_AXIS));
        difficultySelectionPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        difficultySelectionPanel.add(difficultyLabel);
        difficultySelectionPanel.add(Box.createVerticalStrut(6));
        difficultySelectionPanel.add(difficultySelector);

        // --- BUTTONS ---
        primaryButton = createButton("Start Game");
        primaryButton.addActionListener(e -> primaryAction.run());

        secondaryButton = createButton("Main Menu");
        secondaryButton.addActionListener(e -> secondaryAction.run());

        quitButton = createButton("Quit");
        quitButton.addActionListener(e -> quitAction.run());

        installKeyBindings();

        // UI-elementen toevoegen aan de card (Inclusief de nieuwe panel)
        card.add(titleLabel);
        card.add(Box.createVerticalStrut(10));
        card.add(subtitleLabel);
        card.add(Box.createVerticalStrut(24));
        card.add(levelSelectionPanel);
        card.add(Box.createVerticalStrut(12)); // Ruimte tussen level en difficulty
        card.add(difficultySelectionPanel);
        card.add(Box.createVerticalStrut(20));
        card.add(primaryButton);
        card.add(Box.createVerticalStrut(12));
        card.add(secondaryButton);
        card.add(Box.createVerticalStrut(12));
        card.add(quitButton);

        add(card);
        showStartScreen();
    }

    public void setPrimaryAction(Runnable primaryAction) {
        this.primaryAction = primaryAction != null ? primaryAction : () -> {};
    }

    public void setSecondaryAction(Runnable secondaryAction) {
        this.secondaryAction = secondaryAction != null ? secondaryAction : () -> {};
    }

    public void setQuitAction(Runnable quitAction) {
        this.quitAction = quitAction != null ? quitAction : () -> {};
    }

    public void setAvailableLevels(List<LevelCatalog.LevelEntry> levels) {
        LevelCatalog.LevelEntry previousSelection = (LevelCatalog.LevelEntry) levelSelector.getSelectedItem();

        levelSelector.removeAllItems();
        if (levels != null) {
            for (LevelCatalog.LevelEntry level : levels) {
                levelSelector.addItem(level);
            }
        }

        if (previousSelection != null) {
            for (int i = 0; i < levelSelector.getItemCount(); i++) {
                LevelCatalog.LevelEntry current = levelSelector.getItemAt(i);
                if (current.getResourcePath().equals(previousSelection.getResourcePath())) {
                    levelSelector.setSelectedIndex(i);
                    break;
                }
            }
        }

        if (levelSelector.getSelectedIndex() < 0 && levelSelector.getItemCount() > 0) {
            levelSelector.setSelectedIndex(0);
        }

        updateStartScreenState();
    }

    public String getSelectedLevelResource() {
        LevelCatalog.LevelEntry selectedLevel = (LevelCatalog.LevelEntry) levelSelector.getSelectedItem();
        return selectedLevel != null ? selectedLevel.getResourcePath() : null;
    }

    // Getter om de gekozen moeilijkheidsgraad op te vragen in je game-loop
    public Difficulty getSelectedDifficulty() {
        return (Difficulty) difficultySelector.getSelectedItem();
    }

    public void showStartScreen() {
        startScreenVisible = true;
        titleLabel.setText("Tower Rush");
        subtitleLabel.setText("Choose a level and press start.");
        primaryButton.setText("Start Game");
        secondaryButton.setVisible(false);
        levelSelectionPanel.setVisible(true);
        difficultySelectionPanel.setVisible(true); // Tonen op het startscherm
        updateStartScreenState();
        revalidate();
        repaint();
    }

    public void showPauseScreen() {
        startScreenVisible = false;
        titleLabel.setText("Paused");
        subtitleLabel.setText("Resume or return to the start screen");
        primaryButton.setText("Resume");
        secondaryButton.setText("Main Menu");
        secondaryButton.setVisible(true);
        levelSelectionPanel.setVisible(false);
        difficultySelectionPanel.setVisible(false);
        primaryButton.setEnabled(true);
        revalidate();
        repaint();
    }

    private void updateStartScreenState() {
        if (!startScreenVisible) {
            return;
        }

        boolean hasLevels = levelSelector.getItemCount() > 0;
        levelSelector.setEnabled(hasLevels);
        difficultySelector.setEnabled(hasLevels);
        primaryButton.setEnabled(hasLevels);
        subtitleLabel.setText(hasLevels
                ? "Choose a level and press start."
                : "No levels found in the levels folder.");
    }

    private void installKeyBindings() {
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "primary");
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), "primary");
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "primary");
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_M, 0), "secondary");
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_Q, 0), "quit");

        getActionMap().put("primary", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                primaryAction.run();
            }
        });

        getActionMap().put("secondary", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                secondaryAction.run();
            }
        });

        getActionMap().put("quit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                quitAction.run();
            }
        });
    }

    private void bind(KeyStroke keyStroke, String actionName) {
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(keyStroke, actionName);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusPainted(false);
        button.setBackground(new Color(59, 87, 140));
        button.setForeground(Color.WHITE);
        button.setOpaque(true);
        button.setBorder(BorderFactory.createEmptyBorder(12, 22, 12, 22));
        button.setMaximumSize(new Dimension(220, 44));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return button;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setPaint(new GradientPaint(0, 0, new Color(12, 16, 24), 0, getHeight(), new Color(24, 30, 44)));
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.dispose();
    }
}
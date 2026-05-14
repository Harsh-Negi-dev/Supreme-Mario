package com.mario.game;

import com.mario.framework.GameConstants;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 * Main game window
 */
public class GameWindow extends JFrame {
    private GamePanel gamePanel;

    public GameWindow() {
        this.setTitle("Supreme Mario - Platformer Game");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);

        // Create game panel
        gamePanel = new GamePanel();
        this.add(gamePanel);

        // Set window properties
        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        // Start game loop in a separate thread
        new Thread(gamePanel).start();
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new GameWindow());
    }
}

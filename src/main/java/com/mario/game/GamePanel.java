package com.mario.game;

import com.mario.entity.Player;
import com.mario.framework.GameConstants;
import com.mario.framework.InputHandler;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import javax.swing.JPanel;

/**
 * Main game rendering and update panel
 */
public class GamePanel extends JPanel implements Runnable {
    private Player player;
    private InputHandler inputHandler;
    private boolean running;
    private long lastFrameTime;

    public GamePanel() {
        this.setFocusable(true);
        this.setBackground(new Color(135, 206, 235)); // Sky blue
        this.setPreferredSize(new java.awt.Dimension(GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT));

        // Initialize input handler
        inputHandler = new InputHandler();
        this.addKeyListener(inputHandler);

        // Initialize player
        player = new Player(100, 100, inputHandler);

        this.running = true;
        this.lastFrameTime = System.currentTimeMillis();
    }

    @Override
    public void run() {
        while (running) {
            long currentTime = System.currentTimeMillis();
            double deltaTime = (currentTime - lastFrameTime) / 1000.0;
            lastFrameTime = currentTime;

            // Cap delta time to prevent large jumps
            if (deltaTime > 0.05) {
                deltaTime = 0.05;
            }

            // Update game logic
            update(deltaTime);

            // Render
            repaint();

            // Frame rate limiting
            try {
                Thread.sleep(1000 / GameConstants.FPS);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void update(double deltaTime) {
        if (player.isActive()) {
            player.update(deltaTime);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw ground
        g2d.setColor(new Color(34, 139, 34)); // Forest green
        g2d.fillRect(0, (int) GameConstants.GROUND_LEVEL, GameConstants.WINDOW_WIDTH, 
                     GameConstants.WINDOW_HEIGHT - (int) GameConstants.GROUND_LEVEL);

        // Draw game objects
        player.render(g2d);

        // Draw FPS counter
        drawFPS(g2d);
    }

    private void drawFPS(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.setFont(g.getFont().deriveFont(14f));
        g.drawString("FPS: " + (int) (1.0 / GameConstants.DELTA_TIME), 10, 20);
    }

    public void stopGame() {
        running = false;
    }
}

package com.mario.game;

import com.mario.entity.Coin;
import com.mario.entity.GameState;
import com.mario.entity.Player;
import com.mario.entity.enemies.Enemy;
import com.mario.framework.GameConstants;
import com.mario.framework.InputHandler;
import com.mario.level.Level;
import com.mario.level.LevelManager;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import javax.swing.JPanel;

/**
 * Main game rendering and update panel
 */
public class GamePanel extends JPanel implements Runnable {
    private Player player;
    private InputHandler inputHandler;
    private LevelManager levelManager;
    private GameState gameState;
    private boolean running;
    private long lastFrameTime;
    private double gameOverTimer = 0;
    private static final double LEVEL_TRANSITION_TIME = 2.0; // seconds

    public GamePanel() {
        this.setFocusable(true);
        this.setBackground(new Color(135, 206, 235)); // Sky blue
        this.setPreferredSize(new java.awt.Dimension(GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT));

        // Initialize input handler
        inputHandler = new InputHandler();
        this.addKeyListener(inputHandler);

        // Initialize level manager
        levelManager = new LevelManager();

        // Initialize game state
        gameState = new GameState(3); // 3 lives

        // Initialize player
        player = new Player(100, 500, inputHandler);

        // Set total coins in level
        Level currentLevel = levelManager.getCurrentLevel();
        gameState.setTotalCoins(currentLevel.getCoins().size());

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
        if (gameState.isGameOver()) {
            return; // Game over - no updates
        }

        Level currentLevel = levelManager.getCurrentLevel();

        if (gameState.isLevelComplete()) {
            gameOverTimer += deltaTime;
            if (gameOverTimer >= LEVEL_TRANSITION_TIME) {
                if (levelManager.nextLevel()) {
                    // Load next level
                    currentLevel = levelManager.getCurrentLevel();
                    gameState.resetLevel();
                    gameState.setTotalCoins(currentLevel.getCoins().size());
                    player = new Player(100, 500, inputHandler);
                    gameOverTimer = 0;
                } else {
                    // Game complete!
                    gameState.setGameOver(true);
                }
            }
            return;
        }

        // Update player
        if (player.isActive()) {
            player.update(deltaTime, currentLevel);
        } else {
            // Player died (fell or hazard)
            gameState.loseLife();
            if (gameState.isGameOver()) {
                return;
            }
            // Respawn player
            player = new Player(100, 500, inputHandler);
        }

        // Update level
        currentLevel.update(deltaTime);

        // Handle coin collection
        for (Coin coin : new java.util.ArrayList<>(currentLevel.getCoins())) {
            if (player.intersects(coin)) {
                player.collectCoin(coin);
                gameState.collectCoin(coin.getPoints());
                currentLevel.removeCoin(coin);
            }
        }

        // Handle enemy collisions
        for (Enemy enemy : new java.util.ArrayList<>(currentLevel.getEnemies())) {
            if (player.intersects(enemy)) {
                if (player.canStompEnemy()) {
                    // Enemy stomped
                    enemy.defeat();
                    gameState.addScore(100);
                    // Bounce player
                    player.setVelocityY(-300);
                } else {
                    // Player hit - take damage
                    player.setActive(false);
                }
            }
        }

        // Handle goal flag
        if (currentLevel.getGoalFlag() != null && player.intersects(currentLevel.getGoalFlag())) {
            gameState.completeLevel();
        }

        // Allow restart with R key
        if (inputHandler.isKeyPressed(KeyEvent.VK_R)) {
            resetLevel();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        Level currentLevel = levelManager.getCurrentLevel();

        // Draw level
        currentLevel.render(g2d);

        // Draw player
        if (player.isActive()) {
            player.render(g2d);
        }

        // Draw HUD
        drawHUD(g2d);

        // Draw level complete message
        if (gameState.isLevelComplete()) {
            drawLevelCompleteMessage(g2d);
        }

        // Draw game over message
        if (gameState.isGameOver()) {
            drawGameOverMessage(g2d);
        }
    }

    private void drawHUD(Graphics2D g) {
        g.setColor(Color.BLACK);
        g.setFont(g.getFont().deriveFont(16f));

        // Score
        g.drawString("Score: " + gameState.getScore(), 10, 25);

        // Lives
        g.drawString("Lives: " + gameState.getLives(), 10, 50);

        // Coins
        g.drawString("Coins: " + gameState.getCoinsCollected() + "/" + gameState.getTotalCoins(),
                    10, 75);

        // Level
        g.drawString("Level: " + (levelManager.getCurrentLevelIndex() + 1),
                    GameConstants.WINDOW_WIDTH - 150, 25);
    }

    private void drawLevelCompleteMessage(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT);

        g.setColor(Color.YELLOW);
        g.setFont(g.getFont().deriveFont(48f));
        String msg = "LEVEL COMPLETE!";
        int x = (GameConstants.WINDOW_WIDTH - g.getFontMetrics().stringWidth(msg)) / 2;
        g.drawString(msg, x, GameConstants.WINDOW_HEIGHT / 2);

        g.setFont(g.getFont().deriveFont(24f));
        String next = "Next level in...";
        x = (GameConstants.WINDOW_WIDTH - g.getFontMetrics().stringWidth(next)) / 2;
        g.drawString(next, x, GameConstants.WINDOW_HEIGHT / 2 + 50);
    }

    private void drawGameOverMessage(Graphics2D g) {
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, GameConstants.WINDOW_WIDTH, GameConstants.WINDOW_HEIGHT);

        g.setColor(Color.RED);
        g.setFont(g.getFont().deriveFont(48f));
        String msg = "GAME OVER";
        int x = (GameConstants.WINDOW_WIDTH - g.getFontMetrics().stringWidth(msg)) / 2;
        g.drawString(msg, x, GameConstants.WINDOW_HEIGHT / 2);

        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(24f));
        String final_score = "Final Score: " + gameState.getScore();
        x = (GameConstants.WINDOW_WIDTH - g.getFontMetrics().stringWidth(final_score)) / 2;
        g.drawString(final_score, x, GameConstants.WINDOW_HEIGHT / 2 + 50);
    }

    public void resetLevel() {
        gameState.resetScore();
        levelManager = new LevelManager();
        Level currentLevel = levelManager.getCurrentLevel();
        gameState.setTotalCoins(currentLevel.getCoins().size());
        player = new Player(100, 500, inputHandler);
    }

    public void stopGame() {
        running = false;
    }
}

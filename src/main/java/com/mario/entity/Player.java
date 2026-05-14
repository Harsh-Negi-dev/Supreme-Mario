package com.mario.entity;

import com.mario.framework.GameObject;
import com.mario.framework.GameConstants;
import com.mario.framework.InputHandler;
import com.mario.level.Level;
import com.mario.level.Tile;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;

/**
 * Player character (Mario)
 */
public class Player extends GameObject {
    private InputHandler inputHandler;
    private boolean onGround;
    private Color marioColor = new Color(255, 0, 0); // Red Mario
    private boolean facingRight = true;

    public Player(double x, double y, InputHandler inputHandler) {
        super(x, y, GameConstants.PLAYER_WIDTH, GameConstants.PLAYER_HEIGHT);
        this.inputHandler = inputHandler;
        this.onGround = false;
    }

    public void update(double deltaTime, Level level) {
        // Handle horizontal input
        velocityX = 0;
        if (inputHandler.isKeyPressed(KeyEvent.VK_A) || inputHandler.isKeyPressed(KeyEvent.VK_LEFT)) {
            velocityX = -GameConstants.PLAYER_SPEED;
            facingRight = false;
        }
        if (inputHandler.isKeyPressed(KeyEvent.VK_D) || inputHandler.isKeyPressed(KeyEvent.VK_RIGHT)) {
            velocityX = GameConstants.PLAYER_SPEED;
            facingRight = true;
        }

        // Handle jumping
        if ((inputHandler.isKeyPressed(KeyEvent.VK_SPACE) || inputHandler.isKeyPressed(KeyEvent.VK_W) ||
             inputHandler.isKeyPressed(KeyEvent.VK_UP)) && onGround) {
            velocityY = -GameConstants.PLAYER_JUMP_POWER;
            onGround = false;
        }

        // Apply gravity
        velocityY += GameConstants.GRAVITY * deltaTime;

        // Update position
        x += velocityX * deltaTime;
        y += velocityY * deltaTime;

        // Collision with tiles
        onGround = false;
        handleTileCollisions(level);

        // Ground collision fallback
        if (y >= GameConstants.GROUND_LEVEL) {
            y = GameConstants.GROUND_LEVEL;
            velocityY = 0;
            onGround = true;
        }

        // Boundary checks
        if (x < 0) {
            x = 0;
        }
        if (x + width > level.getPixelWidth()) {
            x = level.getPixelWidth() - width;
        }
    }

    /**
     * Handle collision with level tiles
     */
    private void handleTileCollisions(Level level) {
        // Check four corners of the player
        double[] checkPoints = {
            x + 2, y + height,           // Bottom left
            x + width - 2, y + height,   // Bottom right
            x + 2, y,                    // Top left
            x + width - 2, y             // Top right
        };

        for (int i = 0; i < checkPoints.length; i += 2) {
            Tile tile = level.getTileAt(checkPoints[i], checkPoints[i + 1]);
            if (tile != null && tile.isSolid()) {
                if (i == 0 || i == 2) { // Bottom points
                    if (velocityY > 0) { // Falling down
                        y = tile.getPixelY() - height;
                        velocityY = 0;
                        onGround = true;
                    }
                } else if (i == 4 || i == 6) { // Top points
                    if (velocityY < 0) { // Going up
                        y = tile.getPixelY() + Tile.TILE_SIZE;
                        velocityY = 0;
                    }
                }
            }

            // Check for hazards
            if (tile != null && tile.isHazard()) {
                // Take damage - for now, just mark as dead
                active = false;
            }
        }
    }

    @Override
    public void update(double deltaTime) {
        // Legacy method - not used anymore
    }

    @Override
    public void render(Graphics2D g) {
        if (!active) {
            return;
        }

        // Draw simple Mario character
        g.setColor(marioColor);
        g.fillRect((int) x, (int) y, (int) width, (int) height);

        // Draw eyes
        g.setColor(Color.WHITE);
        int leftEyeX = facingRight ? 8 : 18;
        int rightEyeX = facingRight ? 18 : 8;
        g.fillRect((int) (x + leftEyeX), (int) (y + 10), 6, 6);
        g.fillRect((int) (x + rightEyeX), (int) (y + 10), 6, 6);

        // Draw pupils
        g.setColor(Color.BLACK);
        g.fillRect((int) (x + leftEyeX + 1), (int) (y + 11), 4, 4);
        g.fillRect((int) (x + rightEyeX + 1), (int) (y + 11), 4, 4);

        // Draw mustache
        g.setColor(Color.BLACK);
        g.fillRect((int) (x + 6), (int) (y + 22), 20, 2);
    }

    public boolean canStompEnemy() {
        return velocityY > 0 && onGround == false;
    }

    public void collectCoin(Coin coin) {
        coin.collect();
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}

package com.mario.entity;

import com.mario.framework.GameObject;
import com.mario.framework.GameConstants;
import com.mario.framework.InputHandler;
import com.mario.level.Level;
import com.mario.level.Tile;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Player character (Mario)
 */
public class Player extends GameObject {
    private InputHandler inputHandler;
    private boolean onGround;
    private Color marioColor = new Color(255, 0, 0); // Red Mario
    private boolean facingRight = true;
    private PowerUpType powerUp = PowerUpType.NONE;
    private List<Projectile> projectiles = new ArrayList<>();
    private double fireDelay = 0;

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

        // Handle fire flower attack
        if (powerUp == PowerUpType.FIRE_FLOWER && inputHandler.isKeyPressed(KeyEvent.VK_CONTROL)) {
            fireDelay -= deltaTime;
            if (fireDelay <= 0) {
                projectiles.add(new Projectile(
                    facingRight ? x + width : x - 12,
                    y + height / 2,
                    facingRight ? 1 : -1
                ));
                fireDelay = 0.2; // 0.2 seconds between shots
            }
        } else {
            fireDelay = 0;
        }

        // Update projectiles
        for (Projectile proj : projectiles) {
            proj.update(deltaTime);
            proj.updateWithLevel(level);
        }
        projectiles.removeIf(p -> !p.isActive());

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

        // Draw Mario with power-up color
        if (powerUp == PowerUpType.FIRE_FLOWER) {
            marioColor = new Color(255, 165, 0); // Orange Fire Mario
        } else if (powerUp == PowerUpType.MUSHROOM) {
            marioColor = new Color(0, 128, 0); // Green Super Mario
        } else {
            marioColor = new Color(255, 0, 0); // Red normal Mario
        }

        g.setColor(marioColor);
        g.fillRect((int) x, (int) y, (int) width, (int) height);

        // Draw eyes
        g.setColor(Color.WHITE);
        int leftEyeX = facingRight ? 8 : 18;
        int rightEyeX = facingRight ? 18 : 8;
        int eyeY = powerUp == PowerUpType.MUSHROOM ? 15 : 10;
        g.fillRect((int) (x + leftEyeX), (int) (y + eyeY), 6, 6);
        g.fillRect((int) (x + rightEyeX), (int) (y + eyeY), 6, 6);

        // Draw pupils
        g.setColor(Color.BLACK);
        g.fillRect((int) (x + leftEyeX + 1), (int) (y + eyeY + 1), 4, 4);
        g.fillRect((int) (x + rightEyeX + 1), (int) (y + eyeY + 1), 4, 4);

        // Draw mustache
        g.setColor(Color.BLACK);
        int mustacheY = powerUp == PowerUpType.MUSHROOM ? 30 : 22;
        g.fillRect((int) (x + 6), (int) (y + mustacheY), 20, 2);

        // Draw power-up indicator
        if (powerUp != PowerUpType.NONE) {
            g.setColor(Color.YELLOW);
            g.setFont(g.getFont().deriveFont(10f));
            g.drawString(powerUp.getDisplayName(), (int) x, (int) (y - 5));
        }

        // Render projectiles
        for (Projectile proj : projectiles) {
            if (proj.isActive()) {
                proj.render(g);
            }
        }
    }

    public boolean canStompEnemy() {
        return velocityY > 0 && onGround == false;
    }

    public void collectCoin(Coin coin) {
        coin.collect();
    }

    public void setPowerUp(PowerUpType type) {
        this.powerUp = type;
    }

    public PowerUpType getPowerUp() {
        return powerUp;
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    @Override
    public void setHeight(double height) {
        this.height = height;
        // Adjust Y position so the player doesn't fall through the ground
        if (y + height > GameConstants.GROUND_LEVEL) {
            y = GameConstants.GROUND_LEVEL - height;
        }
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}

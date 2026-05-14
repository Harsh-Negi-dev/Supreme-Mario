package com.mario.entity;

import com.mario.framework.GameObject;
import com.mario.framework.GameConstants;
import com.mario.framework.InputHandler;
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

    public Player(double x, double y, InputHandler inputHandler) {
        super(x, y, GameConstants.PLAYER_WIDTH, GameConstants.PLAYER_HEIGHT);
        this.inputHandler = inputHandler;
        this.onGround = false;
    }

    @Override
    public void update(double deltaTime) {
        // Handle horizontal input
        velocityX = 0;
        if (inputHandler.isKeyPressed(KeyEvent.VK_A) || inputHandler.isKeyPressed(KeyEvent.VK_LEFT)) {
            velocityX = -GameConstants.PLAYER_SPEED;
        }
        if (inputHandler.isKeyPressed(KeyEvent.VK_D) || inputHandler.isKeyPressed(KeyEvent.VK_RIGHT)) {
            velocityX = GameConstants.PLAYER_SPEED;
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

        // Ground collision
        if (y >= GameConstants.GROUND_LEVEL) {
            y = GameConstants.GROUND_LEVEL;
            velocityY = 0;
            onGround = true;
        }

        // Boundary checks
        if (x < 0) {
            x = 0;
        }
        if (x + width > GameConstants.WINDOW_WIDTH) {
            x = GameConstants.WINDOW_WIDTH - width;
        }
    }

    @Override
    public void render(Graphics2D g) {
        // Draw simple Mario character
        g.setColor(marioColor);
        g.fillRect((int) x, (int) y, (int) width, (int) height);

        // Draw eyes
        g.setColor(Color.WHITE);
        g.fillRect((int) (x + 8), (int) (y + 10), 6, 6);
        g.fillRect((int) (x + 18), (int) (y + 10), 6, 6);

        // Draw pupils
        g.setColor(Color.BLACK);
        g.fillRect((int) (x + 9), (int) (y + 11), 4, 4);
        g.fillRect((int) (x + 19), (int) (y + 11), 4, 4);
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }
}

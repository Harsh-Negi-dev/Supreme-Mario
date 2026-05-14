package com.mario.entity.enemies;

import com.mario.framework.GameConstants;
import com.mario.level.Level;
import com.mario.level.Tile;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Goomba enemy - walks left and right, can be stomped
 */
public class Goomba extends Enemy {
    private static final double GOOMBA_WIDTH = 32;
    private static final double GOOMBA_HEIGHT = 24;
    private static final double GOOMBA_SPEED = 80.0; // pixels/second
    private double animationFrame = 0;

    public Goomba(double x, double y) {
        super(x, y, GOOMBA_WIDTH, GOOMBA_HEIGHT, GOOMBA_SPEED);
        this.velocityY = 0;
    }

    @Override
    public void update(double deltaTime, Level level) {
        if (!active) {
            return;
        }

        // Horizontal movement
        velocityX = direction * speed;
        x += velocityX * deltaTime;

        // Apply gravity
        velocityY += GameConstants.GRAVITY * deltaTime;

        // Update vertical position
        y += velocityY * deltaTime;

        // Check ground collision
        onGround = false;
        if (y >= GameConstants.GROUND_LEVEL) {
            y = GameConstants.GROUND_LEVEL;
            velocityY = 0;
            onGround = true;
        }

        // Check tile collision
        Tile tileBelow = level.getTileAt(x + width / 2, y + height);
        if (tileBelow != null && tileBelow.isSolid()) {
            y = tileBelow.getPixelY() - height;
            velocityY = 0;
            onGround = true;
        }

        // Check patrol bounds - turn around at edges
        if (x < startX - patrolDistance || x > startX + patrolDistance) {
            direction *= -1;
        }

        // Keep in level bounds
        if (x < 0) {
            x = 0;
            direction = 1;
        }
        if (x + width > level.getPixelWidth()) {
            x = level.getPixelWidth() - width;
            direction = -1;
        }

        // Animation
        animationFrame += deltaTime * 5;
        if (animationFrame >= 2) {
            animationFrame -= 2;
        }
    }

    @Override
    public void render(Graphics2D g) {
        if (!active) {
            return;
        }

        // Draw Goomba body (brown)
        g.setColor(new Color(139, 69, 19));
        g.fillOval((int) x, (int) y, (int) width, (int) height);

        // Draw eyes
        int eyeY = (int) (y + 6);
        int leftEyeX = (int) (x + 8);
        int rightEyeX = (int) (x + width - 14);

        // Eye whites
        g.setColor(Color.WHITE);
        g.fillOval(leftEyeX, eyeY, 6, 6);
        g.fillOval(rightEyeX, eyeY, 6, 6);

        // Eye pupils (direction of movement)
        g.setColor(Color.BLACK);
        int pupilOffset = direction > 0 ? 2 : 1;
        g.fillOval(leftEyeX + pupilOffset, eyeY + 2, 3, 3);
        g.fillOval(rightEyeX + pupilOffset, eyeY + 2, 3, 3);

        // Draw feet (simple walking animation)
        g.setColor(new Color(100, 50, 0));
        int footOffset = (int) (animationFrame * 2);
        g.drawLine((int) (x + 6 + footOffset), (int) (y + height - 2),
                  (int) (x + 6 + footOffset), (int) (y + height + 2));
        g.drawLine((int) (x + width - 8 - footOffset), (int) (y + height - 2),
                  (int) (x + width - 8 - footOffset), (int) (y + height + 2));
    }

    @Override
    public void defeat() {
        active = false;
    }
}

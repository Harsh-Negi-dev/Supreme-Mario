package com.mario.entity.enemies;

import com.mario.framework.GameConstants;
import com.mario.level.Level;
import com.mario.level.Tile;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Koopa Troopa enemy - armored, can be stomped for shells
 */
public class Koopa extends Enemy {
    private static final double KOOPA_WIDTH = 36;
    private static final double KOOPA_HEIGHT = 40;
    private static final double KOOPA_SPEED = 100.0;
    private boolean shellMode = false;
    private double shellRotation = 0;

    public Koopa(double x, double y) {
        super(x, y, KOOPA_WIDTH, KOOPA_HEIGHT, KOOPA_SPEED);
        this.velocityY = 0;
    }

    @Override
    public void update(double deltaTime, Level level) {
        if (!active) {
            return;
        }

        if (!shellMode) {
            // Normal walking mode
            velocityX = direction * speed;
            x += velocityX * deltaTime;

            // Apply gravity
            velocityY += GameConstants.GRAVITY * deltaTime;
        } else {
            // Shell mode - roll faster
            velocityX = direction * speed * 2;
            x += velocityX * deltaTime;
            shellRotation += direction * 20 * deltaTime;
        }

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

        // Patrol bounds
        if (x < startX - patrolDistance || x > startX + patrolDistance) {
            direction *= -1;
        }

        // Keep in bounds
        if (x < 0) {
            x = 0;
            direction = 1;
        }
        if (x + width > level.getPixelWidth()) {
            x = level.getPixelWidth() - width;
            direction = -1;
        }
    }

    @Override
    public void render(Graphics2D g) {
        if (!active) {
            return;
        }

        if (!shellMode) {
            // Draw Koopa body
            g.setColor(new Color(34, 139, 34)); // Forest green
            g.fillRect((int) x, (int) y, (int) width, (int) (height * 0.6));

            // Draw shell
            g.setColor(new Color(184, 134, 11)); // Dark gold (shell)
            g.fillOval((int) (x + 4), (int) (y + 6), (int) (width - 8), (int) (height * 0.5));

            // Draw shell pattern
            g.setColor(new Color(218, 165, 32));
            g.setStroke(new BasicStroke(1));
            g.drawOval((int) (x + 8), (int) (y + 10), (int) (width - 16), (int) (height * 0.35));

            // Draw eyes
            g.setColor(Color.WHITE);
            int eyeY = (int) (y + 8);
            g.fillOval((int) (x + 8), eyeY, 4, 4);
            g.fillOval((int) (x + width - 12), eyeY, 4, 4);

            // Draw pupils
            g.setColor(Color.BLACK);
            g.fillOval((int) (x + 9), eyeY + 1, 2, 2);
            g.fillOval((int) (x + width - 11), eyeY + 1, 2, 2);

            // Draw feet
            g.setColor(new Color(139, 69, 19));
            g.fillRect((int) (x + 6), (int) (y + height - 4), 6, 4);
            g.fillRect((int) (x + width - 12), (int) (y + height - 4), 6, 4);
        } else {
            // Draw shell in rolling mode
            java.awt.geom.AffineTransform oldTransform = g.getTransform();
            double centerX = x + width / 2;
            double centerY = y + height / 2;

            g.translate(centerX, centerY);
            g.rotate(Math.toRadians(shellRotation));

            g.setColor(new Color(184, 134, 11));
            g.fillOval(-(int) (width / 2), -(int) (height / 2), (int) width, (int) height);

            // Draw shell segments
            g.setColor(new Color(218, 165, 32));
            g.setStroke(new BasicStroke(2));
            g.drawOval(-(int) (width / 3), -(int) (height / 3), (int) (width * 0.66), (int) (height * 0.66));

            g.setTransform(oldTransform);
        }
    }

    public void hitByPlayer() {
        if (!shellMode) {
            shellMode = true;
            speed = KOOPA_SPEED;
        } else {
            // If already in shell mode and hit, defeated
            active = false;
        }
    }

    @Override
    public void defeat() {
        // First hit = shell mode, second hit = defeat
        if (!shellMode) {
            shellMode = true;
        } else {
            active = false;
        }
    }

    public boolean isInShellMode() {
        return shellMode;
    }
}

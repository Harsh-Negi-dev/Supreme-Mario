package com.mario.entity;

import com.mario.framework.GameObject;
import com.mario.level.Level;
import com.mario.level.Tile;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Projectile (fireball) from Fire Mario
 */
public class Projectile extends GameObject {
    private static final double SPEED = 300.0;
    private static final int SIZE = 12;
    private int lifetime = 0;
    private static final int MAX_LIFETIME = 300; // 5 seconds at 60 FPS

    public Projectile(double x, double y, int direction) {
        super(x, y, SIZE, SIZE);
        this.velocityX = direction * SPEED;
        this.velocityY = 0;
    }

    @Override
    public void update(double deltaTime) {
        lifetime++;
        if (lifetime >= MAX_LIFETIME) {
            active = false;
            return;
        }

        x += velocityX * deltaTime;
        y += velocityY * deltaTime;

        // Apply gravity
        velocityY += 200 * deltaTime;

        // Clamp lifetime and deactivate if out of bounds
        if (y > 1000) {
            active = false;
        }
    }

    @Override
    public void render(Graphics2D g) {
        if (!active) {
            return;
        }

        // Draw fireball
        g.setColor(new Color(255, 140, 0)); // Dark orange
        g.fillOval((int) x, (int) y, SIZE, SIZE);

        // Draw glow
        g.setColor(new Color(255, 215, 0)); // Gold
        g.drawOval((int) x, (int) y, SIZE, SIZE);
    }

    public void updateWithLevel(Level level) {
        // Tile collision
        Tile tile = level.getTileAt(x + width / 2, y + height);
        if (tile != null && tile.isSolid()) {
            active = false;
        }
    }
}

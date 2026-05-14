package com.mario.entity;

import com.mario.framework.GameObject;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Collectible coin entity
 */
public class Coin extends GameObject {
    private static final int COIN_SIZE = 16;
    private static final int POINTS = 10;
    private double rotationAngle;

    public Coin(double x, double y) {
        super(x, y, COIN_SIZE, COIN_SIZE);
        this.rotationAngle = 0;
    }

    @Override
    public void update(double deltaTime) {
        // Rotate the coin
        rotationAngle += 360 * deltaTime; // 1 full rotation per second
        if (rotationAngle >= 360) {
            rotationAngle -= 360;
        }
    }

    @Override
    public void render(Graphics2D g) {
        if (!active) {
            return;
        }

        // Save the transform
        java.awt.geom.AffineTransform oldTransform = g.getTransform();

        // Translate to coin center and apply rotation
        g.translate(x + width / 2, y + height / 2);
        g.rotate(Math.toRadians(rotationAngle));

        // Draw coin circle
        g.setColor(new Color(255, 215, 0)); // Gold
        g.fillOval(-(int) (width / 2), -(int) (height / 2), (int) width, (int) height);

        // Draw coin border
        g.setColor(new Color(184, 134, 11)); // Dark gold
        g.setStroke(new java.awt.BasicStroke(1));
        g.drawOval(-(int) (width / 2), -(int) (height / 2), (int) width, (int) height);

        // Draw coin details
        g.setColor(new Color(255, 235, 0));
        g.fillOval(-(int) (width / 4), -(int) (height / 2.5), (int) (width / 2), (int) (height / 5));

        // Restore the transform
        g.setTransform(oldTransform);
    }

    public int getPoints() {
        return POINTS;
    }

    public void collect() {
        active = false;
    }
}

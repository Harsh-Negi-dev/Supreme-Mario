package com.mario.entity.powerups;

import com.mario.entity.Player;
import com.mario.entity.PowerUpType;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Fire Flower - gives player ability to throw fireballs
 */
public class FireFlower extends PowerUp {
    private static final int SIZE = 20;
    private double rotationAngle = 0;

    public FireFlower(double x, double y) {
        super(x, y, SIZE, SIZE);
    }

    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);
        rotationAngle += 360 * deltaTime; // Rotate
        if (rotationAngle >= 360) {
            rotationAngle -= 360;
        }
    }

    @Override
    public void render(Graphics2D g) {
        if (!active) {
            return;
        }

        // Save transform
        java.awt.geom.AffineTransform oldTransform = g.getTransform();

        // Center and rotate
        double centerX = x + SIZE / 2;
        double centerY = getFloatY() + SIZE / 2;
        g.translate(centerX, centerY);
        g.rotate(Math.toRadians(rotationAngle));

        // Draw flower petals (4 petals)
        g.setColor(Color.RED);
        for (int i = 0; i < 4; i++) {
            double angle = Math.toRadians(i * 90);
            int px = (int) (Math.cos(angle) * 6);
            int py = (int) (Math.sin(angle) * 6);
            g.fillOval(px - 3, py - 3, 6, 6);
        }

        // Draw center
        g.setColor(Color.YELLOW);
        g.fillOval(-3, -3, 6, 6);

        // Restore transform
        g.setTransform(oldTransform);
    }

    @Override
    public void applyEffect(Player player) {
        player.setPowerUp(PowerUpType.FIRE_FLOWER);
    }
}

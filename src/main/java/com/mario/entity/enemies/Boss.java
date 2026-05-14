package com.mario.entity.enemies;

import com.mario.framework.GameConstants;
import com.mario.level.Level;
import com.mario.level.Tile;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Boss enemy - Bowser-like character
 */
public class Boss extends Enemy {
    private static final double BOSS_WIDTH = 80;
    private static final double BOSS_HEIGHT = 60;
    private static final double BOSS_SPEED = 120.0;
    private int health = 3; // Needs 3 hits to defeat
    private double shootTimer = 0;
    private static final double SHOOT_INTERVAL = 1.5; // seconds between shots

    public Boss(double x, double y) {
        super(x, y, BOSS_WIDTH, BOSS_HEIGHT, BOSS_SPEED);
        this.velocityY = 0;
    }

    @Override
    public void update(double deltaTime, Level level) {
        if (!active) {
            return;
        }

        // Patrol movement
        velocityX = direction * speed;
        x += velocityX * deltaTime;

        // Apply gravity
        velocityY += GameConstants.GRAVITY * deltaTime;
        y += velocityY * deltaTime;

        // Ground collision
        onGround = false;
        if (y >= GameConstants.GROUND_LEVEL - height) {
            y = GameConstants.GROUND_LEVEL - height;
            velocityY = 0;
            onGround = true;
        }

        // Tile collision
        Tile tileBelow = level.getTileAt(x + width / 2, y + height);
        if (tileBelow != null && tileBelow.isSolid()) {
            y = tileBelow.getPixelY() - height;
            velocityY = 0;
            onGround = true;
        }

        // Patrol bounds - turn at edges
        if (x < startX - patrolDistance || x > startX + patrolDistance) {
            direction *= -1;
        }

        // Shooting cooldown
        shootTimer -= deltaTime;

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

        // Draw boss body (larger, more menacing)
        g.setColor(new Color(200, 0, 0)); // Dark red
        g.fillRect((int) x, (int) y, (int) width, (int) (height * 0.7));

        // Draw shell/armor
        g.setColor(new Color(100, 100, 100)); // Gray armor
        g.fillOval((int) (x + 8), (int) (y + 8), (int) (width - 16), (int) (height * 0.5));

        // Draw spikes on shell
        g.setColor(Color.BLACK);
        for (int i = 0; i < 5; i++) {
            int spikeX = (int) (x + 16 + i * 12);
            int[] xPoints = {spikeX, spikeX - 4, spikeX + 4};
            int[] yPoints = {(int) (y + 8), (int) (y + 2), (int) (y + 2)};
            g.fillPolygon(xPoints, yPoints, 3);
        }

        // Draw eyes (angry)
        g.setColor(Color.WHITE);
        g.fillOval((int) (x + 16), (int) (y + 12), 8, 8);
        g.fillOval((int) (x + width - 24), (int) (y + 12), 8, 8);

        // Draw pupils (angry look)
        g.setColor(Color.BLACK);
        g.fillOval((int) (x + 18), (int) (y + 13), 4, 4);
        g.fillOval((int) (x + width - 22), (int) (y + 13), 4, 4);

        // Draw mouth
        g.setColor(Color.BLACK);
        g.drawLine((int) (x + 20), (int) (y + 28), (int) (x + width - 20), (int) (y + 28));
        g.drawLine((int) (x + 25), (int) (y + 30), (int) (x + width - 25), (int) (y + 30));

        // Draw health indicator
        g.setColor(Color.RED);
        g.fillRect((int) (x + 5), (int) (y - 10), health * 15, 5);
        g.setColor(Color.BLACK);
        g.drawRect((int) (x + 5), (int) (y - 10), 45, 5);

        // Draw health text
        g.setColor(Color.WHITE);
        g.setFont(g.getFont().deriveFont(12f));
        g.drawString("Boss", (int) (x + width / 2 - 15), (int) (y - 15));
    }

    public void takeDamage() {
        health--;
        if (health <= 0) {
            active = false;
        }
    }

    public int getHealth() {
        return health;
    }

    @Override
    public void defeat() {
        takeDamage();
    }
}

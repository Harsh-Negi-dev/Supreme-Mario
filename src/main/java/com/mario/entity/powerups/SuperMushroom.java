package com.mario.entity.powerups;

import com.mario.entity.Player;
import com.mario.entity.PowerUpType;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Super Mushroom - makes player grow
 */
public class SuperMushroom extends PowerUp {
    private static final int SIZE = 24;

    public SuperMushroom(double x, double y) {
        super(x, y, SIZE, SIZE);
    }

    @Override
    public void render(Graphics2D g) {
        if (!active) {
            return;
        }

        // Draw mushroom
        g.setColor(new Color(220, 20, 60)); // Crimson red
        g.fillOval((int) x, (int) getFloatY(), SIZE, SIZE);

        // Draw white spots
        g.setColor(Color.WHITE);
        g.fillOval((int) (x + 6), (int) (getFloatY() + 4), 5, 5);
        g.fillOval((int) (x + 14), (int) (getFloatY() + 4), 5, 5);
        g.fillOval((int) (x + 4), (int) (getFloatY() + 12), 5, 5);
        g.fillOval((int) (x + 14), (int) (getFloatY() + 12), 5, 5);

        // Draw stem
        g.setColor(Color.GREEN);
        g.fillRect((int) (x + 9), (int) (getFloatY() - 4), 6, 6);
    }

    @Override
    public void applyEffect(Player player) {
        player.setPowerUp(PowerUpType.MUSHROOM);
        // Grow player
        player.setHeight(64);
    }
}

package com.mario.entity;

import com.mario.framework.GameObject;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Goal flag at the end of each level
 */
public class GoalFlag extends GameObject {
    private double flagWave = 0;
    private static final int POLE_WIDTH = 8;
    private static final int POLE_HEIGHT = 120;
    private static final int FLAG_WIDTH = 50;
    private static final int FLAG_HEIGHT = 30;

    public GoalFlag(double x, double y) {
        super(x, y, FLAG_WIDTH, FLAG_HEIGHT);
    }

    @Override
    public void update(double deltaTime) {
        // Wave the flag
        flagWave += 8 * deltaTime; // Wave speed
        if (flagWave >= 360) {
            flagWave -= 360;
        }
    }

    @Override
    public void render(Graphics2D g) {
        if (!active) {
            return;
        }

        // Draw pole
        g.setColor(new Color(139, 69, 19)); // Brown
        g.fillRect((int) (x + FLAG_WIDTH / 2 - POLE_WIDTH / 2), (int) (y - POLE_HEIGHT),
                   POLE_WIDTH, POLE_HEIGHT);

        // Draw flag with wave effect
        g.setColor(new Color(255, 0, 0)); // Red
        int[] flagXPoints = new int[4];
        int[] flagYPoints = new int[4];

        // Base position
        flagXPoints[0] = (int) (x + FLAG_WIDTH / 2);
        flagYPoints[0] = (int) (y - POLE_HEIGHT + 10);

        // Right point (with wave)
        double waveOffset = Math.sin(Math.toRadians(flagWave)) * 8;
        flagXPoints[1] = (int) (x + FLAG_WIDTH / 2 + FLAG_WIDTH + waveOffset);
        flagYPoints[1] = (int) (y - POLE_HEIGHT + 10);

        // Bottom right
        flagXPoints[2] = (int) (x + FLAG_WIDTH / 2 + FLAG_WIDTH + waveOffset);
        flagYPoints[2] = (int) (y - POLE_HEIGHT + 10 + FLAG_HEIGHT);

        // Bottom left
        flagXPoints[3] = (int) (x + FLAG_WIDTH / 2);
        flagYPoints[3] = (int) (y - POLE_HEIGHT + 10 + FLAG_HEIGHT);

        g.fillPolygon(flagXPoints, flagYPoints, 4);

        // Draw flag border
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2));
        g.drawPolygon(flagXPoints, flagYPoints, 4);

        // Draw star on flag
        g.setColor(Color.YELLOW);
        drawStar(g, (int) (x + FLAG_WIDTH / 2 + FLAG_WIDTH / 2 + waveOffset),
                (int) (y - POLE_HEIGHT + 10 + FLAG_HEIGHT / 2), 8);
    }

    private void drawStar(Graphics2D g, int centerX, int centerY, int size) {
        int[] xPoints = new int[10];
        int[] yPoints = new int[10];

        for (int i = 0; i < 10; i++) {
            double angle = Math.toRadians(i * 36 - 90);
            int radius = (i % 2 == 0) ? size : size / 2;
            xPoints[i] = centerX + (int) (radius * Math.cos(angle));
            yPoints[i] = centerY + (int) (radius * Math.sin(angle));
        }

        g.fillPolygon(xPoints, yPoints, 10);
    }

    public void reach() {
        active = false;
    }
}

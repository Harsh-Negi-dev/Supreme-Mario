package com.mario.entity;

import com.mario.framework.GameObject;
import com.mario.level.Level;
import com.mario.level.Tile;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Moving platform - moves horizontally or vertically
 */
public class MovingPlatform extends GameObject {
    public enum Direction {
        HORIZONTAL, VERTICAL
    }

    private Direction direction;
    private double minPos;
    private double maxPos;
    private double speed;
    private int moveDirection = 1; // 1 or -1

    public MovingPlatform(double x, double y, double width, double height, 
                         Direction direction, double minPos, double maxPos, double speed) {
        super(x, y, width, height);
        this.direction = direction;
        this.minPos = minPos;
        this.maxPos = maxPos;
        this.speed = speed;
    }

    @Override
    public void update(double deltaTime) {
        if (direction == Direction.HORIZONTAL) {
            x += moveDirection * speed * deltaTime;
            if (x <= minPos || x >= maxPos) {
                moveDirection *= -1;
            }
        } else { // VERTICAL
            y += moveDirection * speed * deltaTime;
            if (y <= minPos || y >= maxPos) {
                moveDirection *= -1;
            }
        }
    }

    @Override
    public void render(Graphics2D g) {
        // Draw platform with animated color
        Color platformColor = new Color(255, 165, 0);
        g.setColor(platformColor);
        g.fillRect((int) x, (int) y, (int) width, (int) height);

        // Draw border
        g.setColor(Color.BLACK);
        g.setStroke(new java.awt.BasicStroke(2));
        g.drawRect((int) x, (int) y, (int) width, (int) height);

        // Draw movement indicator
        g.setColor(new Color(255, 200, 0));
        if (direction == Direction.HORIZONTAL) {
            g.drawLine((int) (x + width / 4), (int) (y + height / 2),
                      (int) (x + 3 * width / 4), (int) (y + height / 2));
        } else {
            g.drawLine((int) (x + width / 2), (int) (y + height / 4),
                      (int) (x + width / 2), (int) (y + 3 * height / 4));
        }
    }

    public boolean isPlayerOnTop(Player player) {
        return player.getX() < x + width &&
               player.getX() + player.getWidth() > x &&
               player.getY() + player.getHeight() <= y + 5 &&
               player.getY() + player.getHeight() >= y;
    }

    public void movePlayerWith(Player player, double deltaTime) {
        if (direction == Direction.HORIZONTAL) {
            player.setX(player.getX() + moveDirection * speed * deltaTime);
        } else {
            player.setY(player.getY() + moveDirection * speed * deltaTime);
        }
    }

    public Direction getDirection() {
        return direction;
    }
}

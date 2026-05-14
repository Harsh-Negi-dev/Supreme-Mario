package com.mario.entity;

/**
 * Conveyor belt - moves player/objects in one direction
 */
public class ConveyorBelt extends MovingPlatform {
    private static final double CONVEYOR_SPEED = 150.0;

    public ConveyorBelt(double x, double y, double width, double height, 
                       Direction direction) {
        super(x, y, width, height, direction, x, x + width * 3, CONVEYOR_SPEED);
    }

    @Override
    public void render(java.awt.Graphics2D g) {
        // Draw conveyor belt with animated stripes
        java.awt.Color beltColor = new java.awt.Color(100, 100, 100);
        g.setColor(beltColor);
        g.fillRect((int) x, (int) y, (int) width, (int) height);

        // Draw animated stripes to show direction
        g.setColor(new java.awt.Color(150, 150, 150));
        if (getDirection() == Direction.HORIZONTAL) {
            for (int i = 0; i < 5; i++) {
                g.fillRect((int) (x + i * 30), (int) y, 15, (int) height);
            }
        } else {
            for (int i = 0; i < 5; i++) {
                g.fillRect((int) x, (int) (y + i * 30), (int) width, 15);
            }
        }

        // Draw border
        g.setColor(java.awt.Color.BLACK);
        g.setStroke(new java.awt.BasicStroke(2));
        g.drawRect((int) x, (int) y, (int) width, (int) height);
    }
}

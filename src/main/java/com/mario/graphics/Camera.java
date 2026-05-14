package com.mario.graphics;

/**
 * Camera that follows the player
 */
public class Camera {
    private double x;
    private double y;
    private int width;
    private int height;

    public Camera(int width, int height) {
        this.width = width;
        this.height = height;
        this.x = 0;
        this.y = 0;
    }

    /**
     * Update camera to follow target entity
     */
    public void follow(double targetX, double targetY, int levelWidth, int levelHeight) {
        // Center the camera on the target
        x = targetX - width / 2.0;
        y = targetY - height / 3.0; // Bias upward to see above player

        // Clamp to level bounds
        if (x < 0) {
            x = 0;
        }
        if (x + width > levelWidth) {
            x = levelWidth - width;
        }

        if (y < 0) {
            y = 0;
        }
        if (y + height > levelHeight) {
            y = levelHeight - height;
        }
    }

    /**
     * Convert world coordinates to screen coordinates
     */
    public double getScreenX(double worldX) {
        return worldX - x;
    }

    public double getScreenY(double worldY) {
        return worldY - y;
    }

    public double getWorldX(double screenX) {
        return screenX + x;
    }

    public double getWorldY(double screenY) {
        return screenY + y;
    }

    public double getCameraX() {
        return x;
    }

    public double getCameraY() {
        return y;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void reset() {
        x = 0;
        y = 0;
    }
}

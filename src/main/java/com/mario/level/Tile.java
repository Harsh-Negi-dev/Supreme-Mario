package com.mario.level;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Represents a single tile in the game world
 */
public class Tile {
    public static final int TILE_SIZE = 40;

    public enum TileType {
        EMPTY(0, new Color(135, 206, 235)),           // Sky blue
        SOLID(1, new Color(139, 69, 19)),             // Brown
        SPIKE(2, new Color(200, 0, 0)),               // Red
        ICE(3, new Color(173, 216, 230)),             // Light blue
        MOVING_PLATFORM(4, new Color(255, 165, 0));  // Orange

        public final int id;
        public final Color color;

        TileType(int id, Color color) {
            this.id = id;
            this.color = color;
        }

        public static TileType fromId(int id) {
            for (TileType type : TileType.values()) {
                if (type.id == id) {
                    return type;
                }
            }
            return EMPTY;
        }
    }

    private TileType type;
    private int gridX;
    private int gridY;

    public Tile(TileType type, int gridX, int gridY) {
        this.type = type;
        this.gridX = gridX;
        this.gridY = gridY;
    }

    public void render(Graphics2D g) {
        if (type == TileType.EMPTY) {
            return;
        }

        int x = gridX * TILE_SIZE;
        int y = gridY * TILE_SIZE;

        g.setColor(type.color);
        g.fillRect(x, y, TILE_SIZE, TILE_SIZE);

        // Draw border
        g.setColor(Color.BLACK);
        g.setStroke(new java.awt.BasicStroke(1));
        g.drawRect(x, y, TILE_SIZE, TILE_SIZE);
    }

    public TileType getType() {
        return type;
    }

    public void setType(TileType type) {
        this.type = type;
    }

    public int getGridX() {
        return gridX;
    }

    public int getGridY() {
        return gridY;
    }

    public int getPixelX() {
        return gridX * TILE_SIZE;
    }

    public int getPixelY() {
        return gridY * TILE_SIZE;
    }

    public boolean isSolid() {
        return type != TileType.EMPTY && type != TileType.SPIKE;
    }

    public boolean isHazard() {
        return type == TileType.SPIKE;
    }
}

package com.mario.editor;

import com.mario.level.Level;
import com.mario.level.Tile;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;

/**
 * Handles the tile grid display and editing in the level editor
 */
public class EditorGrid {
    private static final int TILE_SIZE = 40;
    private Level level;
    private EditorState editorState;

    public EditorGrid(Level level, EditorState editorState) {
        this.level = level;
        this.editorState = editorState;
    }

    public void render(Graphics2D g, int panelWidth, int panelHeight) {
        // Draw grid background
        g.setColor(new Color(200, 200, 200));
        g.fillRect(0, 0, panelWidth, panelHeight);

        // Draw tiles
        int offsetX = editorState.getPanOffsetX();
        int offsetY = editorState.getPanOffsetY();
        double zoom = editorState.getZoom();
        int scaledTileSize = (int) (TILE_SIZE * zoom);

        for (int y = 0; y < level.getHeight(); y++) {
            for (int x = 0; x < level.getWidth(); x++) {
                Tile tile = level.getTile(x, y);
                if (tile != null) {
                    int screenX = offsetX + x * scaledTileSize;
                    int screenY = offsetY + y * scaledTileSize;

                    // Only render tiles visible on screen
                    if (screenX + scaledTileSize > 0 && screenX < panelWidth &&
                        screenY + scaledTileSize > 0 && screenY < panelHeight) {
                        renderTile(g, tile, screenX, screenY, scaledTileSize);
                    }
                }
            }
        }

        // Draw grid lines
        g.setColor(new Color(150, 150, 150));
        for (int x = 0; x <= level.getWidth(); x++) {
            int screenX = offsetX + x * scaledTileSize;
            if (screenX > 0 && screenX < panelWidth) {
                g.drawLine(screenX, 0, screenX, panelHeight);
            }
        }

        for (int y = 0; y <= level.getHeight(); y++) {
            int screenY = offsetY + y * scaledTileSize;
            if (screenY > 0 && screenY < panelHeight) {
                g.drawLine(0, screenY, panelWidth, screenY);
            }
        }
    }

    private void renderTile(Graphics2D g, Tile tile, int x, int y, int size) {
        Color tileColor = getTileColor(tile.getType());
        g.setColor(tileColor);
        g.fillRect(x, y, size, size);

        // Draw border
        g.setColor(Color.BLACK);
        g.drawRect(x, y, size, size);
    }

    private Color getTileColor(Tile.TileType type) {
        switch (type) {
            case SOLID:
                return new Color(139, 69, 19);      // Brown
            case SPIKE:
                return new Color(255, 0, 0);        // Red
            case ICE:
                return new Color(173, 216, 230);    // Light blue
            case EMPTY:
                return new Color(200, 200, 200);    // Gray
            case MOVING_PLATFORM:
                return new Color(255, 165, 0);      // Orange
            default:
                return Color.WHITE;
        }
    }

    public void handleMousePress(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        int tileX = getTileXFromScreen(mouseX);
        int tileY = getTileYFromScreen(mouseY);

        if (tileX >= 0 && tileX < level.getWidth() && tileY >= 0 && tileY < level.getHeight()) {
            if (editorState.getCurrentMode() == EditorState.EditorMode.TILE_SELECTION) {
                level.setTile(tileX, tileY, editorState.getSelectedTile());
            }
        }
    }

    public void handleMouseDrag(MouseEvent e) {
        handleMousePress(e);
    }

    public void handleRightClick(MouseEvent e) {
        int mouseX = e.getX();
        int mouseY = e.getY();
        int tileX = getTileXFromScreen(mouseX);
        int tileY = getTileYFromScreen(mouseY);

        if (tileX >= 0 && tileX < level.getWidth() && tileY >= 0 && tileY < level.getHeight()) {
            // Right click to erase
            level.setTile(tileX, tileY, Tile.TileType.EMPTY);
        }
    }

    private int getTileXFromScreen(int screenX) {
        int offsetX = editorState.getPanOffsetX();
        double zoom = editorState.getZoom();
        int scaledTileSize = (int) (40 * zoom);
        return (screenX - offsetX) / scaledTileSize;
    }

    private int getTileYFromScreen(int screenY) {
        int offsetY = editorState.getPanOffsetY();
        double zoom = editorState.getZoom();
        int scaledTileSize = (int) (40 * zoom);
        return (screenY - offsetY) / scaledTileSize;
    }

    public void clearLevel() {
        for (int y = 0; y < level.getHeight(); y++) {
            for (int x = 0; x < level.getWidth(); x++) {
                level.setTile(x, y, Tile.TileType.EMPTY);
            }
        }
    }
}

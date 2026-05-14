package com.mario.editor;

import com.mario.level.Tile.TileType;

/**
 * Tracks the state of the level editor
 */
public class EditorState {
    public enum EditorMode {
        TILE_SELECTION,      // Selecting what tile to place
        ENTITY_SELECTION,    // Selecting what entity to place
        PLATFORM_PLACEMENT   // Placing moving platforms
    }

    public enum EntityType {
        COIN,
        POWER_UP_MUSHROOM,
        POWER_UP_FIRE_FLOWER,
        ENEMY_GOOMBA,
        ENEMY_KOOPA,
        GOAL_FLAG,
        SPAWN_POINT
    }

    private EditorMode currentMode;
    private TileType selectedTile;
    private EntityType selectedEntity;
    private int levelWidth;
    private int levelHeight;
    private double zoom = 1.0;
    private int panOffsetX = 0;
    private int panOffsetY = 0;

    public EditorState() {
        this.currentMode = EditorMode.TILE_SELECTION;
        this.selectedTile = TileType.SOLID;
        this.levelWidth = 40;
        this.levelHeight = 20;
    }

    public EditorMode getCurrentMode() {
        return currentMode;
    }

    public void setCurrentMode(EditorMode mode) {
        this.currentMode = mode;
    }

    public TileType getSelectedTile() {
        return selectedTile;
    }

    public void setSelectedTile(TileType tile) {
        this.selectedTile = tile;
    }

    public EntityType getSelectedEntity() {
        return selectedEntity;
    }

    public void setSelectedEntity(EntityType entity) {
        this.selectedEntity = entity;
    }

    public int getLevelWidth() {
        return levelWidth;
    }

    public int getLevelHeight() {
        return levelHeight;
    }

    public void setLevelDimensions(int width, int height) {
        this.levelWidth = width;
        this.levelHeight = height;
    }

    public double getZoom() {
        return zoom;
    }

    public void setZoom(double zoom) {
        this.zoom = Math.max(0.5, Math.min(3.0, zoom));
    }

    public int getPanOffsetX() {
        return panOffsetX;
    }

    public int getPanOffsetY() {
        return panOffsetY;
    }

    public void setPan(int offsetX, int offsetY) {
        this.panOffsetX = offsetX;
        this.panOffsetY = offsetY;
    }

    public void pan(int deltaX, int deltaY) {
        this.panOffsetX += deltaX;
        this.panOffsetY += deltaY;
    }
}

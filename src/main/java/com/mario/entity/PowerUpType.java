package com.mario.entity;

/**
 * Player power-up states and effects
 */
public enum PowerUpType {
    NONE("None"),
    MUSHROOM("Super Mario"),
    FIRE_FLOWER("Fire Mario");

    private String displayName;

    PowerUpType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}

package com.mario.framework;

/**
 * Global game constants
 */
public class GameConstants {
    // Window dimensions
    public static final int WINDOW_WIDTH = 1200;
    public static final int WINDOW_HEIGHT = 800;

    // Game physics
    public static final double GRAVITY = 500.0; // pixels/second^2
    public static final double GROUND_LEVEL = 650.0;

    // Player constants
    public static final double PLAYER_WIDTH = 32;
    public static final double PLAYER_HEIGHT = 48;
    public static final double PLAYER_SPEED = 250.0; // pixels/second
    public static final double PLAYER_JUMP_POWER = 400.0; // pixels/second

    // Game speed
    public static final int FPS = 60;
    public static final double DELTA_TIME = 1.0 / FPS;
}

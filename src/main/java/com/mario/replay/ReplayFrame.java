package com.mario.replay;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a single frame of gameplay data for replay purposes
 */
public class ReplayFrame {
    // Player state
    public double playerX;
    public double playerY;
    public double playerVelX;
    public double playerVelY;
    public boolean playerOnGround;
    public String playerPowerUp;  // NONE, MUSHROOM, FIRE_FLOWER

    // Game state
    public int score;
    public int coins;
    public int lives;
    public int levelIndex;

    // Enemy positions
    public List<EnemyState> enemies = new ArrayList<>();

    // Coin/PowerUp states
    public List<EntityState> collectibles = new ArrayList<>();

    // Projectile positions
    public List<ProjectileState> projectiles = new ArrayList<>();

    // Timestamp
    public long frameNumber;

    public ReplayFrame() {
    }

    /**
     * Inner class to store enemy data
     */
    public static class EnemyState {
        public double x;
        public double y;
        public String type;  // Goomba, Koopa, Boss
        public boolean active;
        public int health;  // For boss

        public EnemyState() {
        }

        public EnemyState(double x, double y, String type, boolean active, int health) {
            this.x = x;
            this.y = y;
            this.type = type;
            this.active = active;
            this.health = health;
        }
    }

    /**
     * Inner class to store coin/powerup data
     */
    public static class EntityState {
        public double x;
        public double y;
        public String type;  // Coin, Mushroom, FireFlower
        public boolean active;

        public EntityState() {
        }

        public EntityState(double x, double y, String type, boolean active) {
            this.x = x;
            this.y = y;
            this.type = type;
            this.active = active;
        }
    }

    /**
     * Inner class to store projectile data
     */
    public static class ProjectileState {
        public double x;
        public double y;
        public double velX;
        public double velY;
        public boolean active;

        public ProjectileState() {
        }

        public ProjectileState(double x, double y, double velX, double velY, boolean active) {
            this.x = x;
            this.y = y;
            this.velX = velX;
            this.velY = velY;
            this.active = active;
        }
    }
}

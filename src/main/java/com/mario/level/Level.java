package com.mario.level;

import com.mario.entity.Coin;
import com.mario.entity.GoalFlag;
import com.mario.entity.Projectile;
import com.mario.entity.enemies.Enemy;
import com.mario.entity.powerups.PowerUp;
import com.mario.framework.GameObject;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a game level with tiles and entities
 */
public class Level {
    private Tile[][] tiles;
    private int width;  // in tiles
    private int height; // in tiles
    private List<Enemy> enemies;
    private List<Coin> coins;
    private List<PowerUp> powerUps;
    private List<Projectile> projectiles;
    private GoalFlag goalFlag;
    private String levelName;

    public Level(int width, int height, String levelName) {
        this.width = width;
        this.height = height;
        this.levelName = levelName;
        this.tiles = new Tile[height][width];
        this.enemies = new ArrayList<>();
        this.coins = new ArrayList<>();
        this.powerUps = new ArrayList<>();
        this.projectiles = new ArrayList<>();

        // Initialize empty tiles
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tiles[y][x] = new Tile(Tile.TileType.EMPTY, x, y);
            }
        }
    }

    /**
     * Set a tile at the given grid coordinates
     */
    public void setTile(int x, int y, Tile.TileType type) {
        if (isInBounds(x, y)) {
            tiles[y][x].setType(type);
        }
    }

    /**
     * Get tile at grid coordinates
     */
    public Tile getTile(int x, int y) {
        if (isInBounds(x, y)) {
            return tiles[y][x];
        }
        return null;
    }

    /**
     * Get tile from pixel coordinates
     */
    public Tile getTileAt(double pixelX, double pixelY) {
        int gridX = (int) (pixelX / Tile.TILE_SIZE);
        int gridY = (int) (pixelY / Tile.TILE_SIZE);
        return getTile(gridX, gridY);
    }

    /**
     * Check if coordinates are within level bounds
     */
    public boolean isInBounds(int x, int y) {
        return x >= 0 && x < width && y >= 0 && y < height;
    }

    public void addEnemy(Enemy enemy) {
        enemies.add(enemy);
    }

    public void addCoin(Coin coin) {
        coins.add(coin);
    }

    public void addPowerUp(PowerUp powerUp) {
        powerUps.add(powerUp);
    }

    public void addProjectile(Projectile projectile) {
        projectiles.add(projectile);
    }

    public void setGoalFlag(GoalFlag flag) {
        this.goalFlag = flag;
    }

    public void update(double deltaTime) {
        // Update enemies
        for (Enemy enemy : enemies) {
            if (enemy.isActive()) {
                enemy.update(deltaTime, this);
            }
        }
        enemies.removeIf(e -> !e.isActive());

        // Update coins
        for (Coin coin : coins) {
            coin.update(deltaTime);
        }
        coins.removeIf(c -> !c.isActive());

        // Update power-ups
        for (PowerUp powerUp : powerUps) {
            if (powerUp.isActive()) {
                powerUp.update(deltaTime);
            }
        }
        powerUps.removeIf(p -> !p.isActive());

        // Update projectiles
        for (Projectile projectile : projectiles) {
            if (projectile.isActive()) {
                projectile.updateWithLevel(this);
            }
        }
        projectiles.removeIf(p -> !p.isActive());

        // Update goal flag
        if (goalFlag != null && goalFlag.isActive()) {
            goalFlag.update(deltaTime);
        }
    }

    public void render(Graphics2D g) {
        // Render tiles
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                tiles[y][x].render(g);
            }
        }

        // Render power-ups
        for (PowerUp powerUp : powerUps) {
            if (powerUp.isActive()) {
                powerUp.render(g);
            }
        }

        // Render coins
        for (Coin coin : coins) {
            if (coin.isActive()) {
                coin.render(g);
            }
        }

        // Render projectiles
        for (Projectile projectile : projectiles) {
            if (projectile.isActive()) {
                projectile.render(g);
            }
        }

        // Render enemies
        for (Enemy enemy : enemies) {
            if (enemy.isActive()) {
                enemy.render(g);
            }
        }

        // Render goal flag
        if (goalFlag != null && goalFlag.isActive()) {
            goalFlag.render(g);
        }
    }

    // Getters
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getPixelWidth() {
        return width * Tile.TILE_SIZE;
    }

    public int getPixelHeight() {
        return height * Tile.TILE_SIZE;
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }

    public List<Coin> getCoins() {
        return coins;
    }

    public List<PowerUp> getPowerUps() {
        return powerUps;
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    public GoalFlag getGoalFlag() {
        return goalFlag;
    }

    public String getLevelName() {
        return levelName;
    }

    public void removeCoin(Coin coin) {
        coins.remove(coin);
    }

    public void removePowerUp(PowerUp powerUp) {
        powerUps.remove(powerUp);
    }

    public void removeEnemy(Enemy enemy) {
        enemies.remove(enemy);
    }
}

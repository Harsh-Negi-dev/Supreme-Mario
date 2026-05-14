package com.mario.level;

import com.mario.entity.Coin;
import com.mario.entity.GoalFlag;
import com.mario.entity.enemies.Goomba;
import com.mario.entity.enemies.Koopa;
import com.mario.entity.powerups.FireFlower;
import com.mario.entity.powerups.SuperMushroom;

/**
 * Manages level creation and progression
 */
public class LevelManager {
    private Level currentLevel;
    private int currentLevelIndex;
    private Level[] levels;

    public LevelManager() {
        this.currentLevelIndex = 0;
        this.levels = new Level[3];
        initializeLevels();
        this.currentLevel = levels[0];
    }

    private void initializeLevels() {
        // Level 1 - Basic platformer
        levels[0] = createLevel1();

        // Level 2 - More challenging
        levels[1] = createLevel2();

        // Level 3 - Advanced
        levels[2] = createLevel3();
    }

    private Level createLevel1() {
        Level level = new Level(30, 20, "Level 1 - Starting");

        // Create platforms
        for (int x = 0; x < 30; x++) {
            level.setTile(x, 19, Tile.TileType.SOLID); // Bottom
        }

        // Platform 1
        for (int x = 3; x < 8; x++) {
            level.setTile(x, 15, Tile.TileType.SOLID);
        }

        // Platform 2
        for (int x = 10; x < 15; x++) {
            level.setTile(x, 12, Tile.TileType.SOLID);
        }

        // Platform 3
        for (int x = 17; x < 22; x++) {
            level.setTile(x, 10, Tile.TileType.SOLID);
        }

        // Platform 4 (goal area)
        for (int x = 25; x < 30; x++) {
            level.setTile(x, 8, Tile.TileType.SOLID);
        }

        // Add coins
        level.addCoin(new Coin(220, 550));
        level.addCoin(new Coin(420, 480));
        level.addCoin(new Coin(680, 400));
        level.addCoin(new Coin(900, 320));

        // Add power-ups
        level.addPowerUp(new SuperMushroom(340, 420));
        level.addPowerUp(new FireFlower(800, 260));

        // Add enemies
        level.addEnemy(new Goomba(400, 560));
        level.addEnemy(new Goomba(700, 480));
        level.addEnemy(new Koopa(900, 560));

        // Add goal flag
        level.setGoalFlag(new GoalFlag(1000, 280));

        return level;
    }

    private Level createLevel2() {
        Level level = new Level(35, 20, "Level 2 - Intermediate");

        // Ground
        for (int x = 0; x < 35; x++) {
            level.setTile(x, 19, Tile.TileType.SOLID);
        }

        // Various platforms
        for (int x = 0; x < 5; x++) {
            level.setTile(x, 16, Tile.TileType.SOLID);
        }

        for (int x = 8; x < 14; x++) {
            level.setTile(x, 14, Tile.TileType.SOLID);
        }

        for (int x = 17; x < 23; x++) {
            level.setTile(x, 12, Tile.TileType.SOLID);
        }

        for (int x = 26; x < 32; x++) {
            level.setTile(x, 10, Tile.TileType.SOLID);
        }

        // Add spikes
        level.setTile(7, 18, Tile.TileType.SPIKE);
        level.setTile(15, 16, Tile.TileType.SPIKE);
        level.setTile(24, 14, Tile.TileType.SPIKE);

        // Add coins
        for (int i = 0; i < 8; i++) {
            level.addCoin(new Coin(100 + i * 80, 450));
        }

        // Add power-ups
        level.addPowerUp(new FireFlower(450, 380));

        // Add enemies
        level.addEnemy(new Goomba(320, 560));
        level.addEnemy(new Koopa(560, 480));
        level.addEnemy(new Goomba(800, 400));

        // Add goal
        level.setGoalFlag(new GoalFlag(1200, 360));

        return level;
    }

    private Level createLevel3() {
        Level level = new Level(40, 20, "Level 3 - Advanced");

        // Complex level with multiple platforms
        for (int x = 0; x < 40; x++) {
            level.setTile(x, 19, Tile.TileType.SOLID);
        }

        // Scattered platforms
        for (int x = 1; x < 6; x++) {
            level.setTile(x, 16, Tile.TileType.SOLID);
        }

        for (int x = 10; x < 16; x++) {
            level.setTile(x, 13, Tile.TileType.SOLID);
        }

        for (int x = 20; x < 26; x++) {
            level.setTile(x, 10, Tile.TileType.SOLID);
        }

        for (int x = 30; x < 36; x++) {
            level.setTile(x, 8, Tile.TileType.SOLID);
        }

        // Hazards
        for (int x = 9; x <= 9; x++) {
            level.setTile(x, 18, Tile.TileType.SPIKE);
        }
        for (int x = 19; x <= 19; x++) {
            level.setTile(x, 16, Tile.TileType.SPIKE);
        }

        // Add coins
        for (int i = 0; i < 12; i++) {
            level.addCoin(new Coin(80 + i * 80, 400 + (i % 3) * 80));
        }

        // Add power-ups
        level.addPowerUp(new SuperMushroom(350, 320));
        level.addPowerUp(new FireFlower(700, 240));

        // Add enemies
        level.addEnemy(new Goomba(280, 560));
        level.addEnemy(new Koopa(520, 480));
        level.addEnemy(new Goomba(760, 400));
        level.addEnemy(new Koopa(1000, 320));

        // Add goal
        level.setGoalFlag(new GoalFlag(1360, 240));

        return level;
    }

    public Level getCurrentLevel() {
        return currentLevel;
    }

    public boolean nextLevel() {
        if (currentLevelIndex + 1 < levels.length) {
            currentLevelIndex++;
            currentLevel = levels[currentLevelIndex];
            return true;
        }
        return false;
    }

    public int getCurrentLevelIndex() {
        return currentLevelIndex;
    }

    public int getTotalLevels() {
        return levels.length;
    }

    public void resetCurrentLevel() {
        currentLevel = levels[currentLevelIndex];
    }
}

package com.mario.level;

import com.mario.entity.Coin;
import com.mario.entity.ConveyorBelt;
import com.mario.entity.GoalFlag;
import com.mario.entity.MovingPlatform;
import com.mario.entity.enemies.Boss;
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
        Level level = new Level(45, 20, "Level 3 - Boss Arena");

        // Ground and arena walls
        for (int x = 0; x < 45; x++) {
            level.setTile(x, 19, Tile.TileType.SOLID);
        }

        // Side walls
        for (int y = 18; y >= 10; y--) {
            level.setTile(0, y, Tile.TileType.SOLID);
            level.setTile(44, y, Tile.TileType.SOLID);
        }

        // Platform arena
        for (int x = 5; x < 40; x++) {
            level.setTile(x, 15, Tile.TileType.SOLID);
        }

        // Upper platforms for challenge
        for (int x = 8; x < 15; x++) {
            level.setTile(x, 10, Tile.TileType.SOLID);
        }

        for (int x = 30; x < 37; x++) {
            level.setTile(x, 10, Tile.TileType.SOLID);
        }

        // Add moving platforms to make arena challenging
        level.addMovingPlatform(new MovingPlatform(200, 520, 80, 15,
                MovingPlatform.Direction.HORIZONTAL, 200, 600, 100));
        level.addMovingPlatform(new MovingPlatform(1000, 450, 80, 15,
                MovingPlatform.Direction.HORIZONTAL, 800, 1200, 120));

        // Add coins
        for (int i = 0; i < 10; i++) {
            level.addCoin(new Coin(150 + i * 100, 350));
        }

        // Add fire power-ups for boss fight
        level.addPowerUp(new FireFlower(400, 380));
        level.addPowerUp(new FireFlower(900, 300));

        // Add hazards at bottom
        for (int x = 10; x < 35; x++) {
            level.setTile(x, 18, Tile.TileType.SPIKE);
        }

        // Add the BOSS
        level.addEnemy(new Boss(1100, 320));

        // Add some minion enemies
        level.addEnemy(new Goomba(300, 560));
        level.addEnemy(new Koopa(800, 560));

        // Add goal flag after boss
        level.setGoalFlag(new GoalFlag(1700, 240));

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

package com.mario.replay;

import com.mario.entity.Player;
import com.mario.entity.enemies.Boss;
import com.mario.entity.enemies.Enemy;
import com.mario.entity.Coin;
import com.mario.entity.GameState;
import com.mario.entity.Projectile;
import com.mario.entity.powerups.PowerUp;
import com.mario.level.Level;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Records gameplay for later replay
 */
public class ReplayRecorder {
    private List<ReplayFrame> frames = new ArrayList<>();
    private boolean isRecording = false;
    private long frameNumber = 0;

    public void startRecording() {
        this.isRecording = true;
        this.frames.clear();
        this.frameNumber = 0;
    }

    public void stopRecording() {
        this.isRecording = false;
    }

    public boolean isRecording() {
        return isRecording;
    }

    public void recordFrame(Player player, Level level, GameState gameState) {
        if (!isRecording) {
            return;
        }

        ReplayFrame frame = new ReplayFrame();
        frame.frameNumber = frameNumber++;

        // Record player state
        frame.playerX = player.getX();
        frame.playerY = player.getY();
        frame.playerVelX = player.getVelocityX();
        frame.playerVelY = player.getVelocityY();
        frame.playerOnGround = player.isOnGround();
        frame.playerPowerUp = player.getPowerUp().name();

        // Record game state
        frame.score = gameState.getScore();
        frame.coins = gameState.getCoinsCollected();
        frame.lives = gameState.getLives();

        // Record enemies
        for (Enemy enemy : level.getEnemies()) {
            if (enemy.isActive()) {
                String enemyType = "Unknown";
                int health = 1;

                if (enemy instanceof Boss) {
                    enemyType = "Boss";
                    health = ((Boss) enemy).getHealth();
                } else if (enemy.getClass().getSimpleName().equals("Goomba")) {
                    enemyType = "Goomba";
                } else if (enemy.getClass().getSimpleName().equals("Koopa")) {
                    enemyType = "Koopa";
                }

                ReplayFrame.EnemyState enemyState = new ReplayFrame.EnemyState(
                    enemy.getX(), enemy.getY(), enemyType, true, health
                );
                frame.enemies.add(enemyState);
            }
        }

        // Record coins
        for (Coin coin : level.getCoins()) {
            if (coin.isActive()) {
                ReplayFrame.EntityState coinState = new ReplayFrame.EntityState(
                    coin.getX(), coin.getY(), "Coin", true
                );
                frame.collectibles.add(coinState);
            }
        }

        // Record power-ups
        for (PowerUp powerUp : level.getPowerUps()) {
            if (powerUp.isActive()) {
                String powerUpType = powerUp.getClass().getSimpleName();
                ReplayFrame.EntityState powerUpState = new ReplayFrame.EntityState(
                    powerUp.getX(), powerUp.getY(), powerUpType, true
                );
                frame.collectibles.add(powerUpState);
            }
        }

        // Record projectiles
        for (Projectile projectile : level.getProjectiles()) {
            if (projectile.isActive()) {
                ReplayFrame.ProjectileState projState = new ReplayFrame.ProjectileState(
                    projectile.getX(), projectile.getY(),
                    projectile.getVelocityX(), projectile.getVelocityY(),
                    true
                );
                frame.projectiles.add(projState);
            }
        }

        frames.add(frame);
    }

    public void saveReplay(String filename) {
        try {
            java.io.File dir = new java.io.File("replays");
            if (!dir.exists()) {
                dir.mkdir();
            }

            ReplayData replayData = new ReplayData(frames);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String json = gson.toJson(replayData);

            FileWriter writer = new FileWriter("replays/" + filename + ".json");
            writer.write(json);
            writer.close();

            System.out.println("Replay saved: " + filename + " (" + frames.size() + " frames)");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<ReplayFrame> getFrames() {
        return frames;
    }

    public int getTotalFrames() {
        return frames.size();
    }

    /**
     * Data class for JSON serialization
     */
    public static class ReplayData {
        public List<ReplayFrame> frames;
        public int totalFrames;
        public long timestamp;

        public ReplayData() {
        }

        public ReplayData(List<ReplayFrame> frames) {
            this.frames = frames;
            this.totalFrames = frames.size();
            this.timestamp = System.currentTimeMillis();
        }
    }
}

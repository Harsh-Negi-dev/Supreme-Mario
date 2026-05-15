package com.mario.entity;

/**
 * Tracks game state - score, lives, level progress
 */
public class GameState {
    private int score;
    private int lives;
    private int coinsCollected;
    private int totalCoins;
    private boolean levelComplete;
    private boolean gameOver;

    public GameState(int initialLives) {
        this.score = 0;
        this.lives = initialLives;
        this.coinsCollected = 0;
        this.totalCoins = 0;
        this.levelComplete = false;
        this.gameOver = false;
    }

    public void addScore(int points) {
        this.score += points;
    }

    public void collectCoin(int points) {
        coinsCollected++;
        addScore(points);
    }

    public void loseLife() {
        lives--;
        if (lives <= 0) {
            gameOver = true;
        }
    }

    public void completeLevel() {
        levelComplete = true;
        // Bonus points for collected coins
        addScore(coinsCollected * 5);
    }

    public void resetLevel() {
        coinsCollected = 0;
        levelComplete = false;
    }

    // Getters
    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getLives() {
        return lives;
    }

    public int getCoinsCollected() {
        return coinsCollected;
    }

    public int getTotalCoins() {
        return totalCoins;
    }

    public void setTotalCoins(int total) {
        this.totalCoins = total;
    }

    public boolean isLevelComplete() {
        return levelComplete;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }

    public void resetScore() {
        this.score = 0;
        this.lives = 3;
        this.coinsCollected = 0;
        this.gameOver = false;
        this.levelComplete = false;
    }
}

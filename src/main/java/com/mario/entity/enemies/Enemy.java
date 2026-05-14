package com.mario.entity.enemies;

import com.mario.framework.GameObject;
import com.mario.level.Level;

/**
 * Base class for all enemies
 */
public abstract class Enemy extends GameObject {
    protected double speed;
    protected int direction; // 1 for right, -1 for left
    protected double patrolDistance;
    protected double startX;
    protected boolean onGround;

    public Enemy(double x, double y, double width, double height, double speed) {
        super(x, y, width, height);
        this.speed = speed;
        this.direction = 1;
        this.startX = x;
        this.patrolDistance = 200;
        this.onGround = false;
    }

    /**
     * Update enemy with level collision (override this in subclasses)
     */
    public abstract void update(double deltaTime, Level level);

    /**
     * Required abstract method from GameObject (not used for enemies)
     */
    @Override
    public void update(double deltaTime) {
        // Not used - override update(double, Level) instead
    }

    public boolean isOnGround() {
        return onGround;
    }

    public void setOnGround(boolean onGround) {
        this.onGround = onGround;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public double getSpeed() {
        return speed;
    }

    public void defeat() {
        active = false;
    }
}

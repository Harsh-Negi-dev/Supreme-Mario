package com.mario.entity.powerups;

import com.mario.entity.Player;
import com.mario.framework.GameObject;
import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Power-up base class
 */
public abstract class PowerUp extends GameObject {
    protected double floatOffset = 0;
    protected boolean collected = false;

    public PowerUp(double x, double y, double width, double height) {
        super(x, y, width, height);
    }

    @Override
    public void update(double deltaTime) {
        // Floating animation
        floatOffset += 2.0 * deltaTime;
        if (floatOffset >= 2 * Math.PI) {
            floatOffset -= 2 * Math.PI;
        }
    }

    protected double getFloatY() {
        return y + Math.sin(floatOffset) * 5; // Bounce up/down
    }

    public abstract void applyEffect(Player player);

    public boolean isCollected() {
        return collected;
    }

    public void collect() {
        collected = true;
        active = false;
    }
}

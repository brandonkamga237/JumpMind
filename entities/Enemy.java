package entities;

import world.Platform;

import java.awt.*;
import java.util.List;

public class Enemy extends Entity {


    public Enemy(float x, float y) {
        super(x, y, 30, 30);
    }

    // Main update method with collision handling
    public void update(List<Platform> platforms) {

    }

    // Required override but not used here (custom update method above)
    @Override
    public void update() {

    }

    // Draw the enemy on screen relative to camera
    @Override
    public void render(Graphics2D g, int camX, int camY) {

        g.setColor(new Color(255, 120, 40));

        g.fillRoundRect(
                (int)(x - camX),
                (int)(y - camY),
                (int)width,
                (int)height,
                10,
                10
        );
    }
}
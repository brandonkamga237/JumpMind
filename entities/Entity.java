package entities;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

public abstract class Entity {

    // Position of the entity in the game world
    protected float x, y;

    // Size of the entity
    protected float width, height;

    // Velocity of the entity
    protected float vx, vy;

    // Collision box used for physics and collisions
    protected Rectangle2D.Float hitbox;

    public Entity(float x, float y, float width, float height) {

        // Initialize position
        this.x = x;
        this.y = y;

        // Initialize size
        this.width = width;
        this.height = height;

        // Create the hitbox with the same position and size
        hitbox = new Rectangle2D.Float(x, y, width, height);
    }

    // Update the hitbox position to match the entity position
    protected void updateHitbox() {
        hitbox.x = x;
        hitbox.y = y;
    }

    // Return the collision bounds of the entity
    public Rectangle2D.Float getBounds() {
        return hitbox;
    }

    // Getters
    public float getX() { return x; }
    public float getY() { return y; }
    public float getWidth() { return width; }
    public float getHeight() { return height; }
    public float getVx() { return vx; }
    public float getVy() { return vy; }

    // Update logic of the entity
    public abstract void update();

    // Render the entity on screen
    public abstract void render(Graphics2D g, int camX, int camY);
}
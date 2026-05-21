package world;

import java.awt.*;

public class PowerUp {

    private int x, y;
    private static final int SIZE = 16;
    private boolean collected = false;

    public PowerUp(int x, int y) {
        this.x = x; this.y = y;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, SIZE, SIZE);
    }

    public boolean isCollected() { return collected; }
    public void collect() { collected = true; }

    public void render(Graphics g, int camX) {
        if (collected) return;
        g.setColor(Color.YELLOW);
        g.fillRect(x - camX, y, SIZE, SIZE);
    }
}
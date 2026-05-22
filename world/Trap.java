package world;

import java.awt.*;

public class Trap {

    private int x, y, w, h;

    public Trap(int x, int y, int w, int h) {
        this.x = x; this.y = y;
        this.w = w; this.h = h;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, w, h);
    }

    public void render(Graphics g, int camX, int camY) {
        g.setColor(Color.RED);
        int[] px = {x - camX, x - camX + w / 2, x - camX + w};
        int[] py = {y - camY + h, y - camY, y - camY + h};
        g.fillPolygon(px, py, 3);
    }
}
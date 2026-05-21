package world;


import java.awt.*;

public class Platform {

    private int x, y, w, h;

    public Platform(int x, int y, int w, int h) {
        this.x = x; this.y = y;
        this.w = w; this.h = h;
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, w, h);
    }

    public void render(Graphics g, int camX, int camY) {
        int screenX = x - camX;
        int screenY = y - camY;

        // Base sombre moderne
        g.setColor(new Color(45, 48, 58));
        g.fillRect(screenX, screenY, w, h);

        // Bord supérieur néon (très moderne)
        g.setColor(new Color(120, 200, 255));
        g.fillRect(screenX, screenY, w, 4);

        // Ligne brillante en haut
        g.setColor(new Color(200, 240, 255, 180));
        g.fillRect(screenX + 2, screenY + 1, w - 4, 2);

        // Ombre subtile en bas
        g.setColor(new Color(20, 22, 30));
        g.fillRect(screenX, screenY + h - 5, w, 5);
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getW() { return w; }
    public int getH() { return h; }
} 
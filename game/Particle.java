package game;

import java.awt.*;

public class Particle {

    public float x, y;
    public float vx, vy;
    public float life;
    public float maxLife;
    public Color color;
    public float size;

    public Particle(float x, float y, float vx, float vy, float life, Color color, float size) {
        this.x = x;
        this.y = y;
        this.vx = vx;
        this.vy = vy;
        this.life = life;
        this.maxLife = life;
        this.color = color;
        this.size = size;
    }

    public void update() {
        x += vx;
        y += vy;
        life -= 1;
        vy += 0.08f; // légère gravité sur les particules
        vx *= 0.98f;
    }

    public void render(Graphics2D g, int camX, int camY) {
        if (life <= 0) return;
        float alpha = (life / maxLife) * 200f;
        Color c = new Color(color.getRed(), color.getGreen(), color.getBlue(), (int)alpha);
        g.setColor(c);
        g.fillOval((int)(x - camX), (int)(y - camY), (int)size, (int)size);
    }
}

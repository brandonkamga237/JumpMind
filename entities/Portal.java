package entities;

import java.awt.*;

public class Portal extends Entity {

    private float timer = 0;

    public Portal(float x, float y) {

        super(x, y, 42, 60);
    }

    @Override
    public void update() {

        timer += 0.08f;
    }

    @Override
    public void render(Graphics2D g, int camX, int camY) {

        Graphics2D g2 = (Graphics2D) g.create();

        float pulse = (float)Math.sin(timer) * 6f;

        int alpha = 150 + (int)(Math.sin(timer) * 70);

        g2.setColor(new Color(0,255,200,alpha));

        g2.fillOval(
                (int)(x - camX - pulse/2),
                (int)(y - camY - pulse/2),
                (int)(width + pulse),
                (int)(height + pulse)
        );

        g2.dispose();
    }
}

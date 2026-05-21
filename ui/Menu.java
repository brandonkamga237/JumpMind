package ui;

import java.awt.*;
import java.awt.event.KeyEvent;
import engine.InputHandler;

public class Menu {

    private boolean done = false;

    public void update(InputHandler input) {
        if (input.isPressed(KeyEvent.VK_ENTER)) {
            done = true;
        }
    }

    public boolean isDone() { return done; }

    public void render(Graphics g, int width, int height) {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, width, height);

        g.setColor(Color.CYAN);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        FontMetrics fm = g.getFontMetrics();
        String title = "JumpMind";
        g.drawString(title, (width - fm.stringWidth(title)) / 2, height / 2 - 40);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        fm = g.getFontMetrics();
        String sub = "Appuie sur ENTREE pour jouer";
        g.drawString(sub, (width - fm.stringWidth(sub)) / 2, height / 2 + 20);
    }
}
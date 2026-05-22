package ui;

import engine.InputHandler;

import java.awt.*;
import java.awt.event.KeyEvent;

public class GameOver {

    private boolean restart = false;

    public void update(InputHandler input) {
        if (input.isPressed(KeyEvent.VK_ENTER)) {
            restart = true;
        }
    }

    public boolean wantsRestart() { return restart; }
    public void reset() { restart = false; }

    public void render(Graphics g, int width, int height) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, width, height);

        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        FontMetrics fm = g.getFontMetrics();
        String title = "GAME OVER";
        g.drawString(title, (width - fm.stringWidth(title)) / 2, height / 2 - 30);

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.PLAIN, 20));
        fm = g.getFontMetrics();
        String sub = "ENTREE pour recommencer";
        g.drawString(sub, (width - fm.stringWidth(sub)) / 2, height / 2 + 20);
    }
}
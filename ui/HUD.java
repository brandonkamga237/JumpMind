package ui;

import java.awt.*;

public class HUD {

    private int level;
    private int deaths;

    public void setLevel(int level) { this.level = level; }
    public void setDeaths(int deaths) { this.deaths = deaths; }

    public void render(Graphics g) {
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("Niveau : " + level, 10, 24);
        g.drawString("Morts : " + deaths, 10, 44);
    }
}
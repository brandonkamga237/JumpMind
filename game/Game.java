package game;

import engine.GamePanel;
import engine.InputHandler;
import entities.Enemy;
import entities.Hero;
import entities.Portal;
import ui.HUD;
import world.Platform;
import world.Trap;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Game {

    public enum State { PLAYING, DEAD, WIN }

    private Hero hero;
    private Level level;
    private Camera camera;
    private InputHandler input;
    private HUD hud;

    private int currentLevel = 1;
    private static final int MAX_LEVELS = 5;
    private int deaths = 0;

    private State state = State.PLAYING;

    // Caméra qui suit le joueur (pour jeu vertical / grimpe)
    private boolean cameraFollow = true;

    // Mode normal de jeu (morts et collisions activées)
    private boolean testMode = false;

    // ===============================
    // JUICE MODERNE : PARTICULES + SHAKE + HITSTOP
    // ===============================
    private List<Particle> particles = new ArrayList<>();
    private int hitstop = 0;

    public Game(InputHandler input) {
        this.input = input;
        level = new Level();
        camera = new Camera();
        hud = new HUD();
        loadLevel(currentLevel);
    }

    private void loadLevel(int num) {
        String path = "levels/level" + num + ".txt";
        level.load(path);
        hero = new Hero(level.getHeroStartX(), level.getHeroStartY(), input);
        camera = new Camera();
        camera.setBounds(level.getMapWidth(), level.getMapHeight());
        particles.clear();
        state = State.PLAYING;
        hud.setLevel(currentLevel);
        hud.setDeaths(deaths);
    }

    public void update() {
        if (state != State.PLAYING) return;

        // HITSTOP (gel d'image moderne sur impacts)
        if (hitstop > 0) {
            hitstop--;
            return;
        }

        if (testMode) {
            hero.ignoreHorizontalCollisions = true;
        } else {
            hero.ignoreHorizontalCollisions = false;
        }
        hero.update(level.getPlatforms());

        for (Enemy e : level.getEnemies()) {
            e.update(level.getPlatforms());
        }

        if (cameraFollow) {
            camera.update(hero.getX(), hero.getY());
        } else {
            camera = new Camera(); // reset à 0 pour vue fixe
        }

        // Détection atterrissage dur → shake + particules
        if (hero.getVy() > 8f) {  // grosse chute
            camera.shake(4.5f, 9);
            spawnLandingDust(hero.getX() + hero.getWidth()/2, hero.getY() + hero.getHeight());
            hitstop = 3; // petit hitstop satisfaisant
        }

        // Morts désactivées en mode test (pour tester les mouvements librement)
        if (!testMode) {
            for (Trap t : level.getTraps()) {
                if (hero.getBounds().intersects(t.getBounds())) {
                    camera.shake(6f, 11);
                    die();
                    return;
                }
            }

            for (Enemy e : level.getEnemies()) {
                if (hero.getBounds().intersects(e.getBounds())) {
                    camera.shake(7f, 10);
                    die();
                    return;
                }
            }

            // Mort par chute : en dessous de la map + marge
            if (hero.getY() > level.getMapHeight() + 100) {
                die();
                return;
            }
        }

        Portal portal = level.getPortal();
        if (portal != null && hero.getBounds().intersects(portal.getBounds())) {
            nextLevel();
        }

        // Mise à jour particules
        for (int i = particles.size() - 1; i >= 0; i--) {
            Particle p = particles.get(i);
            p.update();
            if (p.life <= 0) particles.remove(i);
        }

        // Spawn dash trail
        if (hero.getState() == entities.PlayerState.DASH) {
            spawnDashTrail(hero.getX() + hero.getWidth()/2, hero.getY() + hero.getHeight() * 0.6f);
        }
    }

    private void die() {
        deaths++;
        hud.setDeaths(deaths);
        loadLevel(currentLevel);
    }

    private void nextLevel() {
        if (currentLevel < MAX_LEVELS) {
            currentLevel++;
            loadLevel(currentLevel);
        } else {
            state = State.WIN;
        }
    }

    public void render(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        int camX = (int) camera.getX();
        int camY = (int) camera.getY();

        for (Platform p : level.getPlatforms()) {
            p.render(g, camX, camY);
        }

        for (Trap t : level.getTraps()) {
            t.render(g, camX, camY);
        }

        for (Enemy e : level.getEnemies()) {
            e.render(g2, camX, camY);
        }

        Portal portal = level.getPortal();
        if (portal != null) portal.render(g2, camX, camY);

        hero.render(g2, camX, camY);

        // Particules
        for (Particle p : particles) {
            p.render(g2, camX, camY);
        }

        hud.render(g);

        // Indicateur MODE TEST
        if (testMode) {
            g.setColor(new Color(255, 200, 50));
            g.setFont(new Font("Arial", Font.BOLD, 14));
            g.drawString("TEST MODE - Pas de mort (mouvements libres)", 10, GamePanel.HEIGHT - 10);
        }

        if (state == State.WIN) renderWin(g);
    }

    private void renderWin(Graphics g) {
        g.setColor(new Color(0, 0, 0, 180));
        g.fillRect(0, 0, GamePanel.WIDTH, GamePanel.HEIGHT);
        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        FontMetrics fm = g.getFontMetrics();
        String msg = "VICTOIRE !";
        g.drawString(msg, (GamePanel.WIDTH - fm.stringWidth(msg)) / 2, GamePanel.HEIGHT / 2);
    }

    public State getState() { return state; }

    // ===============================
    // PARTICULES (JUICE)
    // ===============================
    private void spawnLandingDust(float x, float y) {
        for (int i = 0; i < 12; i++) {
            float vx = (float)(Math.random() - 0.5) * 3.2f;
            float vy = (float)(Math.random() * -1.8f - 0.5f);
            particles.add(new Particle(x, y, vx, vy, 18 + (int)(Math.random()*8), new Color(180, 180, 190), 3.5f + (float)Math.random()*1.5f));
        }
    }

    private void spawnDashTrail(float x, float y) {
        if (Math.random() < 0.65) {
            float vx = (float)(Math.random() - 0.5) * 1.2f;
            float vy = (float)(Math.random() - 0.5) * 0.8f;
            particles.add(new Particle(x, y, vx, vy, 9, new Color(255, 160, 60), 4.5f));
        }
    }
}
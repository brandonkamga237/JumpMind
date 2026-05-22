package game;

import entities.Enemy;
import entities.Portal;
import world.Platform;
import world.Trap;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Level {

    public static final int TILE_SIZE = 32;

    private List<Platform> platforms = new ArrayList<>();
    private List<Trap> traps = new ArrayList<>();
    private List<Enemy> enemies = new ArrayList<>();
    private Portal portal;
    private float heroStartX, heroStartY;
    private int mapWidth, mapHeight;

    public void load(String path) {
        platforms.clear();
        traps.clear();
        enemies.clear();
        portal = null;

        int maxCols = 0;
        int maxRows = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            int row = 0;
            while ((line = br.readLine()) != null) {
                if (line.length() > maxCols) maxCols = line.length();
                for (int col = 0; col < line.length(); col++) {
                    char c = line.charAt(col);
                    int px = col * TILE_SIZE;
                    int py = row * TILE_SIZE;
                    switch (c) {
                        case '#': platforms.add(new Platform(px, py, TILE_SIZE, TILE_SIZE)); break;
                        case '^': traps.add(new Trap(px, py, TILE_SIZE, TILE_SIZE)); break;
                        case 'E': enemies.add(new Enemy(px, py)); break;
                        case 'X': portal = new Portal(px, py); break;
                        case 'H': heroStartX = px; heroStartY = py; break;
                    }
                }
                row++;
            }
            maxRows = row;
        } catch (IOException e) {
            System.err.println("Erreur chargement niveau : " + path);
        }

        mapWidth = maxCols * TILE_SIZE;
        mapHeight = maxRows * TILE_SIZE;
    }

    public List<Platform> getPlatforms() { return platforms; }
    public List<Trap> getTraps() { return traps; }
    public List<Enemy> getEnemies() { return enemies; }
    public Portal getPortal() { return portal; }
    public float getHeroStartX() { return heroStartX; }
    public float getHeroStartY() { return heroStartY; }
    public int getMapWidth() { return mapWidth; }
    public int getMapHeight() { return mapHeight; }
}
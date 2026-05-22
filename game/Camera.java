package game;

import engine.GamePanel;

public class Camera {

    private float x, y;
    private float shakeX, shakeY;
    private int shakeTime;
    private float shakeIntensity;

    private static final float LERP = 0.12f;   // un peu plus smooth

    private float maxX, maxY;

    public void setBounds(int mapWidth, int mapHeight) {
        this.maxX = Math.max(0, mapWidth - GamePanel.WIDTH);
        this.maxY = Math.max(0, mapHeight - GamePanel.HEIGHT);
    }

    public void update(float heroX, float heroY) {
        // Suivi horizontal
        float targetX = heroX - GamePanel.WIDTH / 2f;
        x += (targetX - x) * LERP;
        if (x < 0) x = 0;
        if (x > maxX) x = maxX;

        // Suivi vertical (très important pour grimper)
        float targetY = heroY - GamePanel.HEIGHT / 2f;
        y += (targetY - y) * LERP;
        if (y < 0) y = 0;
        if (y > maxY) y = maxY;

        // Screen shake
        if (shakeTime > 0) {
            shakeTime--;
            float progress = shakeTime / 12f;
            shakeX = (float)(Math.random() - 0.5) * shakeIntensity * progress;
            shakeY = (float)(Math.random() - 0.5) * shakeIntensity * progress * 0.6f;
        } else {
            shakeX = 0;
            shakeY = 0;
        }
    }

    public void shake(float intensity, int duration) {
        this.shakeIntensity = intensity;
        this.shakeTime = duration;
    }

    public float getX() { return x + shakeX; }
    public float getY() { return y + shakeY; }

    public int toScreenX(float worldX) {
        return (int)(worldX - x);
    }

    public int toScreenY(float worldY) {
        return (int)(worldY - y);
    }
}
package game;

import engine.GamePanel;

public class Camera {

    private float x, y;
    private float shakeX, shakeY;
    private int shakeTime;
    private float shakeIntensity;

    private static final float LERP = 0.12f;   // un peu plus smooth

    public void update(float heroX, float heroY) {
        // Suivi horizontal
        float targetX = heroX - GamePanel.WIDTH / 2f;
        x += (targetX - x) * LERP;
        if (x < 0) x = 0;

        // Suivi vertical (très important pour grimper)
        float targetY = heroY - GamePanel.HEIGHT / 2f;
        y += (targetY - y) * LERP;
        if (y < 0) y = 0;

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
package engine;

public class GameLoop implements Runnable {

    private static final int FPS = 60;
    private static final long FRAME_TIME = 1_000_000_000 / FPS;

    private final GamePanel panel;
    private boolean running;

    public GameLoop(GamePanel panel) {
        this.panel = panel;
    }

    public void start() {
        running = true;
        Thread thread = new Thread(this);
        thread.start();
    }

    public void stop() {
        running = false;
    }

    @Override
    public void run() {
        long lastTime = System.nanoTime();

        while (running) {
            long now = System.nanoTime();
            long delta = now - lastTime;

            if (delta >= FRAME_TIME) {
                panel.update();
                panel.repaint();
                lastTime = now;
            }
        }
    }
}
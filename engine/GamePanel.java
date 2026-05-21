package engine;

import game.Game;
import ui.Menu;
import ui.GameOver;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel {

    public static final int WIDTH = 800;
    public static final int HEIGHT = 480;

    private GameLoop gameLoop;
    private InputHandler inputHandler;
    private Game game;
    private Menu menu;
    private GameOver gameOver;

    private enum Screen { MENU, GAME, GAMEOVER }
    private Screen screen = Screen.MENU;

    public GamePanel() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        setBackground(Color.BLACK);
        setFocusable(true);

        inputHandler = new InputHandler();
        addKeyListener(inputHandler);

        menu = new Menu();
        gameOver = new GameOver();
        game = new Game(inputHandler);

        JFrame frame = new JFrame("JumpMind");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(this);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        gameLoop = new GameLoop(this);
    }

    public void startGame() {
        requestFocusInWindow();
        gameLoop.start();
    }

    public void update() {
        switch (screen) {
            case MENU:
                menu.update(inputHandler);
                if (menu.isDone()) screen = Screen.GAME;
                break;
            case GAME:
                game.update();
                break;
            case GAMEOVER:
                gameOver.update(inputHandler);
                if (gameOver.wantsRestart()) {
                    game = new Game(inputHandler);
                    gameOver.reset();
                    screen = Screen.GAME;
                }
                break;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        switch (screen) {
            case MENU:
                menu.render(g, WIDTH, HEIGHT);
                break;
            case GAME:
                game.render(g);
                break;
            case GAMEOVER:
                game.render(g);
                gameOver.render(g, WIDTH, HEIGHT);
                break;
        }
    }
}
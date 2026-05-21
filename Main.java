
import javax.swing.SwingUtilities;
import engine.GamePanel;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GamePanel panel = new GamePanel();
            panel.startGame();
        });
    }
}
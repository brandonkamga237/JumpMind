package engine;

import java.awt.event.*;
import java.util.Set;
import java.util.HashSet;

public class InputHandler implements KeyListener{
    
    private final Set<Integer> keys = new HashSet<>();

    public boolean isPressed(int keyCode) {
        return keys.contains(keyCode);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        keys.add(e.getKeyCode());
    }

    @Override
    public void keyReleased(KeyEvent e) {
        keys.remove(e.getKeyCode());
    }

    @Override
    public void keyTyped(KeyEvent e) {}
}

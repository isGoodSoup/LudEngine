package org.lud.engine.input;

import com.badlogic.gdx.Gdx;

import java.util.HashMap;
import java.util.Map;

public class InputContext {
    private final String name;
    private final Map<Integer, Runnable> keyActions = new HashMap<>();
    private final Map<Integer, Runnable> comboActions = new HashMap<>();

    public InputContext(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public void bindKey(int key, Runnable action) {
        keyActions.put(key, action);
    }

    public void bindCombo(int key, Runnable action) {
        comboActions.put(key, action);
    }

    public void unbindKey(int key) { keyActions.remove(key); }
    public void unbindCombo(int key) { comboActions.remove(key); }

    public void handleInput(boolean wasCtrlPressed) {
        Map<Integer, Runnable> map = wasCtrlPressed ? comboActions : keyActions;
        map.forEach((key, action) -> {
            if (Gdx.input.isKeyJustPressed(key)) {
                action.run();
            }
        });
    }
}

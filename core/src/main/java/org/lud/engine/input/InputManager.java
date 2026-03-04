package org.lud.engine.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class InputManager {
    private static InputManager instance;
    private final List<InputContext> contexts = new ArrayList<>();
    private InputContext activeContext;
    private InputContext globalContext;

    private InputManager() {}

    public static InputManager get() {
        if(instance == null) { instance = new InputManager(); }
        return instance;
    }

    public void addContext(InputContext context) { contexts.add(context); }
    public void removeContext(InputContext context) { contexts.remove(context); }

    public void setActiveContext(InputContext context) { this.activeContext = context; }
    public void setGlobalContext(InputContext context) { this.globalContext = context; }

    public void update() {
        boolean controlPressed = Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT);
        if(globalContext != null) { globalContext.handleInput(controlPressed); }
        if(activeContext != null) { activeContext.handleInput(controlPressed); }
    }
}

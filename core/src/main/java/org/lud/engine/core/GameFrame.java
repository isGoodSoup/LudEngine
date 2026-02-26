package org.lud.engine.core;

import com.badlogic.gdx.Game;
import org.lud.engine.gui.Menu;

import java.util.function.Supplier;

public class GameFrame extends Game {
    private Supplier<Menu> menu;

    @Override
    public void create() {}

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        getScreen().dispose();
    }

    public Supplier<Menu> getMenu() {
        return menu;
    }

    public void setNextMenu(Supplier<Menu> menu) {
        this.menu = menu;
    }
}

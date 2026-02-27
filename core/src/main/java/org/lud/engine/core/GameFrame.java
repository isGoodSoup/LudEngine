package org.lud.engine.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import org.lud.game.input.Coordinator;

public class GameFrame extends Game {
    private Coordinator coordinator;

    @Override public void create() {
        this.coordinator = new Coordinator();
        Gdx.input.setInputProcessor(coordinator);

    }
    @Override public void render() {
        super.render();
    }
    @Override public void dispose() {
        getScreen().dispose();
    }
    @Override public void setScreen(Screen screen) {
        super.setScreen(screen);
    }
}

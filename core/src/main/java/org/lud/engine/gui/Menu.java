package org.lud.engine.gui;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import org.lud.engine.enums.MenuType;

import java.util.ArrayList;
import java.util.List;

public abstract class Menu implements Screen {
    protected final SpriteBatch batch;
    protected final List<Button> buttons;
    protected MenuType menu;
    private boolean isInit;

    public Menu() {
        this.batch = new SpriteBatch();
        this.buttons = new ArrayList<>();
    }

    public abstract void setup();
    public abstract void render(SpriteBatch batch);

    @Override public void show() {
        if(!isInit) {
            setup();
            isInit = true;
        }
    }
    @Override public void hide() {}

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.1f, 0.15f, 1f);
        batch.begin();
        render(batch);
        batch.end();
        for(Button b : buttons) { b.update(); }
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}

    @Override
    public void dispose() {
        batch.dispose();
        for(Button b : buttons) { b.dispose(); }
    }

    public void addButton(Button b) {
        buttons.add(b);
    }

    public MenuType getMenuType() {
        return menu;
    }

    public void setMenuType(MenuType menu) {
        this.menu = menu;
    }
}

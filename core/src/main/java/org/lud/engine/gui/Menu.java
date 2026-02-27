package org.lud.engine.gui;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import org.lud.engine.enums.Direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public abstract class Menu implements Screen {
    private final SpriteBatch batch;
    private final List<Menu> menus;
    private final List<Button> buttons;
    private boolean isInit;

    public Menu() {
        this.menus = new ArrayList<>();
        this.batch = new SpriteBatch();
        this.buttons = new ArrayList<>();
    }

    public void addMenu(Menu... menus) {
        this.menus.addAll(Arrays.asList(menus));
    }
    public List<Menu> getMenus() {
        return menus;
    }

    public void addButton(Button... buttons) {
        this.buttons.addAll(Arrays.asList(buttons));
    }
    public List<Button> getButtons() {
        return buttons;
    }

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

    public abstract void setup();
    public abstract void render(SpriteBatch batch);
    public abstract void checkInput();
    public abstract void cursor(Direction dir);
}

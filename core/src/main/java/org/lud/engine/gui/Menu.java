package org.lud.engine.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import org.lud.engine.enums.Direction;
import org.lud.engine.enums.LastInput;
import org.lud.game.input.Coordinator;
import org.lud.game.service.GameService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("ALL")
public abstract class Menu implements Screen {
    private final GameService gameService;
    private final SpriteBatch batch;
    private final List<Menu> menus;
    private final List<Button> buttons;
    private boolean isInit;

    private int selectionIndexY;
    private int selectionIndexX;

    public Menu(GameService gameService) {
        this.gameService = gameService;
        this.menus = new ArrayList<>();
        this.batch = new SpriteBatch();
        this.buttons = new ArrayList<>();
    }

    public Sound getFx(int index) {
        String defaultPath = "sounds/";
        return switch(index) {
            case 0 -> Gdx.audio.newSound(Gdx.files.internal(defaultPath + "piece-fx" + ".wav"));
            case 1 -> Gdx.audio.newSound(Gdx.files.internal(defaultPath + "menu-select" + ".wav"));
            default -> throw new IllegalStateException("Unexpected value: " + index);
        };
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
        ScreenUtils.clear(Colors.getBackground());
        for(Button b : getButtons()) { b.update(); }
        if(Coordinator.getLastInput() == LastInput.KEYBOARD) {
            for(int i = 0; i < getButtons().size(); i++) {
                Button b = getButtons().get(i);
                b.setHovered(i == selectionIndexY);
            }
        }

        batch.begin();
        for (Button b : getButtons()) {
            b.render(batch);
        }
        batch.end();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}

    public abstract void setup();
    public abstract void checkInput();

    public void globalInput() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)){
            gameService.showMainMenu();
            getFx(1);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)
            && Gdx.input.isKeyJustPressed(Input.Keys.T)) {
            Colors.nextTheme();
            getFx(0);
        }
        if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)
            && Gdx.input.isKeyPressed(Input.Keys.Q)) {
            Gdx.app.exit();
        }
    }

    public void cursor(Direction dir) {
        int maxIndex = getButtons().size() - 1;
        Coordinator.setLastInput(LastInput.KEYBOARD);

        switch(dir) {
            case UP -> {
                selectionIndexY = Math.max(0, selectionIndexY - 1);
                getFx(1).play();
            }
            case DOWN -> {
                selectionIndexY = Math.min(maxIndex, selectionIndexY + 1);
                getFx(1).play();
            }
            case LEFT -> {
                selectionIndexX = Math.max(0, selectionIndexX - 1);
                getFx(1).play();
            }
            case RIGHT -> {
                selectionIndexX = Math.min(maxIndex, selectionIndexX + 1);
                getFx(1).play();
            }
        }
    }

    public void activate() {
        List<Button> buttons = getButtons();
        if(buttons.isEmpty()) { return; }
        int index = Math.max(0, Math.min(selectionIndexY, buttons.size() - 1));
        Button selected = buttons.get(index);

        if(selected != null) {
            selected.getAction().run();
            getFx(0);
        }
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    @Override
    public void dispose() {
        batch.dispose();
        for(Button b : buttons) { b.dispose(); }
    }
}

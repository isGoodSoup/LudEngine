package org.lud.engine.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import org.lud.engine.enums.Direction;
import org.lud.engine.enums.LastInput;
import org.lud.game.data.ButtonData;
import org.lud.game.input.Coordinator;
import org.lud.engine.core.AudioService;
import org.lud.game.service.GameService;

import java.util.*;

@SuppressWarnings("ALL")
public abstract class Menu implements Screen {
    private Map<Integer, Runnable> actions;
    private Map<Integer, Runnable> combos;
    private final GameService gameService;
    private final AudioService audioService;
    private final SpriteBatch batch;
    private final ShapeRenderer shaper;
    private final List<Menu> menus;
    private final List<Button> buttons;
    private final BitmapFont font;
    private boolean isInit;

    private int selectionIndexY;
    private int selectionIndexX;

    public Menu(GameService gameService, AudioService audioService) {
        this.gameService = gameService;
        this.audioService = audioService;
        this.actions = new LinkedHashMap<>();
        this.combos = new LinkedHashMap<>();
        this.menus = new ArrayList<>();
        this.buttons = new ArrayList<>();
        this.font = new BitmapFont(Gdx.files.internal("fonts/BoldPixels.fnt"));
        this.batch = new SpriteBatch();
        this.shaper = new ShapeRenderer();
    }

    public Texture getButton(ButtonData data, boolean isHighlighted) {
        String iconPath = "";
        if(data.lang() != null) {
            iconPath = "buttons/button_" + data.type().getSuffix(data.lang()) + ".png";
        } else {
            if(isHighlighted) {
                iconPath = "buttons/button_" + data.type().getSuffix() + "_highlighted.png";
            } else {
                iconPath = "buttons/button_" + data.type().getSuffix() + ".png";
            }
        }
        return new Texture(iconPath);
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

    public BitmapFont getFont() {
        return font;
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

    public void loadKeys() {
        actions.put(Input.Keys.ESCAPE, () -> {
            gameService.showMainMenu();
            audioService.playFX(0);
        });
        actions.put(Input.Keys.UP, () -> cursor(Direction.UP));
        actions.put(Input.Keys.DOWN, () -> cursor(Direction.DOWN));
        actions.put(Input.Keys.M, () -> audioService.toggleMusic());

        combos.put(Input.Keys.W, () -> {
            audioService.setMusicVolume(Math.max(1,
                audioService.getMusicGain() + 0.1f));
        });

        combos.put(Input.Keys.S, () -> {
            audioService.setMusicVolume(Math.max(0,
                audioService.getMusicGain() - 0.1f));
        });
        combos.put(Input.Keys.T, () -> {
            Colors.nextTheme();
            audioService.playFX(1);
        });
        combos.put(Input.Keys.Q, Gdx.app::exit);
    }

    public void globalInput() {
        if(combos.isEmpty() || actions.isEmpty()) {
            loadKeys();
        }
        if(Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
            for(var entry : combos.entrySet()) {
                if(Gdx.input.isKeyJustPressed(entry.getKey())) {
                    entry.getValue().run();
                    return;
                }
            }
        } else {
            for(var entry : actions.entrySet()) {
                if(Gdx.input.isKeyJustPressed(entry.getKey())) {
                    entry.getValue().run();
                }
            }
        }
    }

    public void cursor(Direction dir) {
        int maxIndex = getButtons().size() - 1;
        Coordinator.setLastInput(LastInput.KEYBOARD);

        switch(dir) {
            case UP -> {
                selectionIndexY = Math.max(0, selectionIndexY - 1);
                audioService.playFX(1);
            }
            case DOWN -> {
                selectionIndexY = Math.min(maxIndex, selectionIndexY + 1);
                audioService.playFX(1);
            }
            case LEFT -> {
                selectionIndexX = Math.max(0, selectionIndexX - 1);
                audioService.playFX(1);
            }
            case RIGHT -> {
                selectionIndexX = Math.min(maxIndex, selectionIndexX + 1);
                audioService.playFX(1);
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
            audioService.playFX(0);
        }
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public ShapeRenderer getShaper() {
        return shaper;
    }

    @Override
    public void dispose() {
        batch.dispose();
        shaper.dispose();
        for(Button b : buttons) { b.dispose(); }
        font.dispose();
    }
}

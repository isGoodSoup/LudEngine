package org.lud.engine.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.lud.engine.core.AudioService;
import org.lud.engine.enums.Direction;
import org.lud.engine.enums.Lang;
import org.lud.engine.enums.LastInput;
import org.lud.engine.input.Coordinator;
import org.lud.game.data.ButtonData;
import org.lud.game.service.BoardService;
import org.lud.game.service.GameService;

import java.util.*;

@SuppressWarnings("ALL")
public abstract class Menu implements Screen {
    private Map<Integer, Runnable> actions;
    private Map<Integer, Runnable> combos;
    private final Stage stage;
    private final SpriteBatch batch;
    private final ShapeRenderer shaper;
    private final List<Menu> menus;
    private final List<Button> buttons;
    private final BitmapFont font;
    private final FreeTypeFontGenerator generator;
    private boolean isInit;
    private final GameService gameService;
    private final AudioService audioService;
    private final BoardService boardService;

    private int selectionIndexY;
    private int selectionIndexX;

    private int moveX;
    private int moveY;

    private Tooltip tooltip;
    private Texture texture;

    public Menu(GameService gameService, AudioService audioService,
                BoardService boardService) {
        this.gameService = gameService;
        this.audioService = audioService;
        this.boardService = boardService;
        this.actions = new LinkedHashMap<>();
        this.combos = new LinkedHashMap<>();
        this.menus = new ArrayList<>();
        this.buttons = new ArrayList<>();

        this.generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/BoldPixels.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = 32;
        params.characters = FreeTypeFontGenerator.DEFAULT_CHARS + Localization.lang.getAllStrings();
        font = generator.generateFont(params);
        generator.dispose();

        this.texture = new Texture(Gdx.files.internal("tooltip.png"));
        this.tooltip = new Tooltip("", font, texture, 16, 4);

        this.batch = new SpriteBatch();
        this.stage = new Stage(new ScreenViewport(), batch);
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

    public int getMoveY() { return moveY; }
    public void setMoveY(int moveY) { this.moveY = moveY; }

    public int getMoveX() { return moveX; }
    public void setMoveX(int moveX) { this.moveX = moveX; }

    public Tooltip getTooltip() {
        return tooltip;
    }

    @Override public void show() {
        Gdx.input.setInputProcessor(stage);
        if(!isInit) {
            setup();
            isInit = true;
        }
    }
    @Override public void hide() {}

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Colors.getBackground());

        stage.act(delta);
        stage.draw();

        if(Coordinator.getLastInput() == LastInput.KEYBOARD) {
            for(int i = 0; i < getButtons().size(); i++) {
                Button b = getButtons().get(i);
                b.setHovered(i == selectionIndexY);
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    @Override public void pause() {}
    @Override public void resume() {}

    public abstract void setup();
    public abstract void checkInput();

    private void loadKeys() {
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
        combos.put(Input.Keys.L, () -> {
            Lang.nextLang();
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
            case LEFT -> {
                selectionIndexX = Math.max(0, selectionIndexX - 1);
                audioService.playFX(1);
            }
            case DOWN -> {
                selectionIndexY = Math.min(maxIndex, selectionIndexY + 1);
                audioService.playFX(1);
            }
            case RIGHT -> {
                selectionIndexX = Math.min(maxIndex, selectionIndexX + 1);
                audioService.playFX(1);
            }
        }
    }

    public void cursor(Direction dir, boolean isBoard) {
        int maxIndex = getButtons().size() - 1;
        Coordinator.setLastInput(LastInput.KEYBOARD);

        switch(dir) {
            case UP -> {
                moveY = Math.min(7, moveY + 1);
                audioService.playFX(1);
            }
            case LEFT -> {
                moveX = Math.max(0, moveX - 1);
                audioService.playFX(1);
            }
            case DOWN -> {
                moveY = Math.max(0, moveY - 1);
                audioService.playFX(1);
            }
            case RIGHT -> {
                moveX = Math.min(7, moveX + 1);
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

    public void select() {
        // TODO
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public ShapeRenderer getShaper() {
        return shaper;
    }

    public Stage getStage() {
        return stage;
    }

    @Override
    public void dispose() {
        batch.dispose();
        shaper.dispose();
        stage.dispose();
        for(Button b : buttons) { b.dispose(); }
        font.dispose();
    }
}

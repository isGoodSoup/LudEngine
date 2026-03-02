package org.lud.engine.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.lud.engine.core.AudioService;
import org.lud.engine.core.Cursor;
import org.lud.engine.enums.*;
import org.lud.engine.input.Coordinator;
import org.lud.game.data.ButtonData;
import org.lud.game.service.BoardService;
import org.lud.game.service.GameService;

import java.util.*;

@SuppressWarnings("ALL")
public abstract class Menu implements Screen {
    private Map<Integer, Runnable> actions;
    private Map<Integer, Runnable> combos;
    private final Cursor cursor;
    private final Stage stage;
    private final SpriteBatch batch;
    private final ShapeRenderer shaper;
    private final List<Menu> menus;
    private final List<Toast> toasts;
    private final List<Button> buttons;

    private final BitmapFont small;
    private final BitmapFont medium;
    private final BitmapFont large;

    private final FreeTypeFontGenerator generator;
    private boolean isInit;
    private final GameService gameService;
    private final AudioService audioService;
    private final BoardService boardService;

    private int selectionIndexY;
    private int selectionIndexX;

    private int moveX;
    private int moveY;

    private Group toastGroup;

    private Tooltip tooltip;
    private Texture texture;

    public Menu(GameService gameService, AudioService audioService,
                BoardService boardService) {
        this.gameService = gameService;
        this.audioService = audioService;
        this.boardService = boardService;
        this.cursor = new Cursor();
        this.actions = new LinkedHashMap<>();
        this.combos = new LinkedHashMap<>();
        this.menus = new ArrayList<>();
        this.buttons = new ArrayList<>();
        this.toasts = new ArrayList<>();
        this.toastGroup = new Group();

        this.generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/BoldPixels.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();
        params.size = 32;
        params.characters = FreeTypeFontGenerator.DEFAULT_CHARS + Localization.lang.getAllStrings();
        small = generator.generateFont(params);

        params.size = 40;
        params.characters = FreeTypeFontGenerator.DEFAULT_CHARS + Localization.lang.getAllStrings();
        medium = generator.generateFont(params);

        params.size = 48;
        params.characters = FreeTypeFontGenerator.DEFAULT_CHARS + Localization.lang.getAllStrings();
        large = generator.generateFont(params);
        generator.dispose();

        this.texture = new Texture(Gdx.files.internal("tooltip.png"));
        this.tooltip = new Tooltip("", small, texture, 16, 4);

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

    public Cursor getCursor() {
        return cursor;
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

    public BitmapFont getSmallFont() {
        return small;
    }
    public BitmapFont getMediumFont() {
        return medium;
    }
    public BitmapFont getLargeFont() {
        return large;
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
        cursor.render(batch);

        updateCursor(delta);
        updateSelection();

        if(Coordinator.getLastInput() == LastInput.KEYBOARD) {
            for(int i = 0; i < getButtons().size(); i++) {
                Button b = getButtons().get(i);
                b.setSelected(i == selectionIndexY);
            }
        } else {
            for(Button b : getButtons()) {
                b.setSelected(false);
            }
        }
    }

    public void updateCursor(float delta) {
        if(Coordinator.getLastInput() == LastInput.KEYBOARD) {
            int index = Math.max(0, Math.min(selectionIndexY, buttons.size() - 1));
            Button selected = buttons.get(index);
            cursor.setPosition(selected.getX() + selected.getWidth()/2f,
                selected.getY() + selected.getHeight()/2f);
        }

        Vector2 stageCoords = stage.screenToStageCoordinates(
            new Vector2(Gdx.input.getX(), Gdx.input.getY()));

        if (stageCoords.x != cursor.x || stageCoords.y != cursor.y) {
            cursor.setPosition(stageCoords.x, stageCoords.y);
            Coordinator.setLastInput(LastInput.MOUSE);
        }
    }

    public void updateSelection() {
        for(int i = 0; i < buttons.size(); i++) {
            Button b = buttons.get(i);
            boolean isOver = cursor.x >= b.getX() && cursor.x <= b.getX() + b.getWidth()
                && cursor.y >= b.getY() && cursor.y <= b.getY() + b.getHeight();
            b.setSelected(isOver);
            if (isOver && Coordinator.getLastInput() == LastInput.KEYBOARD) {
                stage.setKeyboardFocus(b);
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

    public Toast createToast(Difficulty difficulty) {
        float toastWidth = Gdx.graphics.getWidth()/3f;
        float centerX = (Gdx.graphics.getWidth() - toastWidth)/2f;

        Toast toast = new Toast(difficulty.getLabelKey(), difficulty.name(), getMediumFont(),
                        centerX, 75f);

        toasts.add(toast);
        toastGroup.addActor(toast);
        toastGroup.toFront();
        return toast;
    }

    private void loadKeys() {
        actions.put(Input.Keys.ESCAPE, () -> {
            gameService.showMainMenu();
            audioService.playFX(0);
        });
        actions.put(Input.Keys.UP, () -> cursor(Direction.UP));
        actions.put(Input.Keys.DOWN, () -> cursor(Direction.DOWN));
        actions.put(Input.Keys.M, () -> audioService.toggleMusic());
        actions.put(Input.Keys.NUMPAD_ADD, () -> audioService.setMusicVolume(0.1f));
        actions.put(Input.Keys.NUMPAD_SUBTRACT, () -> audioService.setMusicVolume(-0.1f));

        combos.put(Input.Keys.T, () -> {
            if(gameService.getGameState() == GameState.BOARD) { return; }
            Colors.nextTheme();
            audioService.playFX(1);
        });
        combos.put(Input.Keys.L, () -> {
            Lang.nextLang();
        });
        combos.put(Input.Keys.D, () -> {
            Difficulty next = boardService.getDifficulty().nextDifficulty();
            boardService.switchDifficulties(next);
            createToast(next);
            System.out.println(boardService.getDifficulty() + " to " + next.name());
            audioService.playFX(2);
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
        List<Button> buttons = getButtons();
        if(buttons.isEmpty()) return;
        Coordinator.setLastInput(LastInput.KEYBOARD);

        switch(dir) {
            case UP -> {
                selectionIndexY = Math.max(0, selectionIndexY - 1);
                audioService.playFX(1);
            }
            case DOWN -> {
                selectionIndexY = Math.min(buttons.size() - 1, selectionIndexY + 1);
                audioService.playFX(1);
            }
        }

        for(int i = 0; i < buttons.size(); i++){
            Button b = buttons.get(i);
            b.setSelected(i == selectionIndexY);
            if(i == selectionIndexY) {
                stage.setKeyboardFocus(b);
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
            selected.onClick();
        }
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

    public Group getToastGroup() {
        return toastGroup;
    }

    public List<Toast> getToasts() {
        return toasts;
    }

    @Override
    public void dispose() {
        batch.dispose();
        shaper.dispose();
        stage.dispose();
        for(Button b : buttons) { b.dispose(); }
        small.dispose();
        medium.dispose();
        large.dispose();
    }
}

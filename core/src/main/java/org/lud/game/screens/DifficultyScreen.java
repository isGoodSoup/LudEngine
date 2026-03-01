package org.lud.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import org.lud.engine.core.AudioService;
import org.lud.engine.enums.Difficulty;
import org.lud.engine.enums.Direction;
import org.lud.engine.gui.Button;
import org.lud.engine.gui.Menu;
import org.lud.engine.gui.Slicer;
import org.lud.engine.gui.Window;
import org.lud.game.data.ButtonData;
import org.lud.game.enums.UIButton;
import org.lud.game.service.BoardService;
import org.lud.game.service.GameService;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class DifficultyScreen extends Menu {
    private static final float DURATION = 1f;
    private final GameService gameService;
    private final AudioService audioService;
    private final BoardService boardService;
    private final List<ButtonData> data;
    private final List<Window> windows;

    private Group group;
    private Slicer slicer;
    private Texture window;
    private Texture windowH;
    private Texture baseButton;
    private Texture frame;

    public DifficultyScreen(GameService gameService, AudioService audioService,
                            BoardService boardService) {
        super(gameService, audioService, boardService);
        this.gameService = gameService;
        this.audioService = audioService;
        this.boardService = boardService;
        this.data = new ArrayList<>();
        this.windows = new ArrayList<>();
        addMenu(this);
        loadSprites();
    }

    public void loadSprites() {
        String defaultPath = "buttons/";
        this.baseButton = new Texture(defaultPath + "button_small.png");
        this.frame = new Texture(defaultPath + "button_small_highlighted.png");

        this.window = new Texture("window/window.png");
        this.windowH = new Texture("window/window_highlighted.png");
        this.slicer = new Slicer(4, window, windowH);

        data.add(new ButtonData(UIButton.PREVIOUS_PAGE, this::slideOut,
            () -> audioService.playFX(0)));
    }

    @Override
    public void setup() {

        float spacing = 1f;
        float startX = 25f;
        float y = 25f;

        group = new Group();

        // ---- Back Button ----
        for (ButtonData data : data) {
            Texture icon = getButton(data, false);
            Texture highlighted = getButton(data, true);

            Button b = new Button(
                startX, y,
                baseButton.getWidth(),
                baseButton.getHeight(),
                baseButton,
                icon,
                frame,
                highlighted,
                data.soundPath(),
                data.action()
            );

            group.addActor(b);
            addButton(b);
            startX += baseButton.getWidth() + spacing;
        }

        float windowWidth = 200f;
        float windowHeight = 800f;
        float spacingX = 8f;
        int count = 5;

        float totalWidth = count * windowWidth + (count - 1) * spacingX;
        float startY = (Gdx.graphics.getHeight() - windowHeight)/2f;
        startX = (Gdx.graphics.getWidth() - totalWidth)/2f;

        createWindow(startX, startY, windowWidth, windowHeight, Difficulty.ALPHA);
        startX += windowWidth + spacingX;

        createWindow(startX, startY, windowWidth, windowHeight, Difficulty.BETA);
        startX += windowWidth + spacingX;

        createWindow(startX, startY, windowWidth, windowHeight, Difficulty.CORONEL);
        startX += windowWidth + spacingX;

        createWindow(startX, startY, windowWidth, windowHeight, Difficulty.DELTA);
        startX += windowWidth + spacingX;

        createWindow(startX, startY, windowWidth, windowHeight, Difficulty.SIGMA);
    }

    @Override
    public void show() {
        super.show();
        getStage().addActor(group);
        group.addAction(Actions.moveTo(25f, 25f, DURATION, Interpolation.pow5Out));
    }

    @Override
    public void render(float delta) {
        super.render(delta);

        globalInput();
        checkInput();
    }

    @Override
    public void checkInput() {
        if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            activate();
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
            cursor(Direction.UP);
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
            cursor(Direction.DOWN);
        }
    }

    private void createWindow(float x, float y, float width, float height,
                              Difficulty difficulty) {
        Window w = new Window(x, y, width, height, slicer,
            4f, () -> audioService.playFX(1), () -> Difficulty.setDifficulty(difficulty));
        group.addActor(w);
        windows.add(w);
    }

    public void slideOut() {
        group.addAction(Actions.sequence(
            Actions.moveTo(0, -Gdx.graphics.getHeight(), DURATION, Interpolation.pow5Out),
            Actions.run(gameService::showMainMenu)
        ));
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}

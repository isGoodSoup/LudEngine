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
import org.lud.engine.gui.Slider;
import org.lud.game.data.ButtonData;
import org.lud.game.enums.UIButton;
import org.lud.game.service.BoardService;
import org.lud.game.service.GameService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class SettingsMenu extends Menu {
    private static final float DURATION = 1f;
    private Map<Slider, Supplier<String>> tooltips;
    private final GameService gameService;
    private final AudioService audioService;
    private final BoardService boardService;
    private final List<ButtonData> data;
    private final List<Slider> sliders;
    private final List<Runnable> runnables;
    private Group group;
    private Texture baseButton;
    private Texture frame;

    private Slider difficulty;
    private Slider volume;

    public SettingsMenu(GameService gameService, AudioService audioService,
                        BoardService boardService) {
        super(gameService, audioService, boardService);
        this.gameService = gameService;
        this.audioService = audioService;
        this.boardService = boardService;
        this.tooltips = new LinkedHashMap<>();
        this.data = new ArrayList<>();
        this.sliders = new ArrayList<>();
        this.runnables = new ArrayList<>();
        addMenu(this);
        loadSprites();
    }

    public void loadSprites() {
        String defaultPath = "buttons/";
        this.baseButton = new Texture(defaultPath + "button_small.png");
        this.frame = new Texture(defaultPath + "button_small_highlighted.png");
        data.add(new ButtonData(UIButton.PREVIOUS_PAGE, this::slideOut,
            () -> audioService.playFX(0)));

        float centerX = (Gdx.graphics.getWidth() - Slider.getBodyWidth())/2f;
        float posY = Gdx.graphics.getHeight()/2f * 0.65f;

        difficulty = new Slider(() -> {}, centerX, posY);
        difficulty.addValueAction(0.20f, () -> Difficulty.setDiff(Difficulty.ALPHA));
        difficulty.addValueAction(0.40f, () -> Difficulty.setDiff(Difficulty.BETA));
        difficulty.addValueAction(0.60f, () -> Difficulty.setDiff(Difficulty.CORONEL));
        difficulty.addValueAction(0.80f, () -> Difficulty.setDiff(Difficulty.DELTA));
        difficulty.addValueAction(1.00f, () -> Difficulty.setDiff(Difficulty.SIGMA));

        volume = new Slider(() -> {}, centerX, posY - 75);
        List<Float> floats = new ArrayList<>(List.of(0.1f, 0.2f, 0.3f, 0.4f, 0.5f,
            0.6f, 0.7f, 0.8f, 0.9f, 1.0f));
        for(Float f : floats) {
            final float volumeLevel = f;
            volume.addValueAction(volumeLevel, () -> audioService.setMusicVolume(volumeLevel));
        }

        sliders.add(difficulty);
        sliders.add(volume);

    }

    @Override
    public void setup() {
        float spacing = 1f;
        float startX = 25f;
        float y = 25f;

        group = new Group();

        for(ButtonData data : data) {
            Texture icon = getButton(data, false);
            Texture highlighted = getButton(data, true);

            Button b = new Button(startX, y, baseButton.getWidth(), baseButton.getHeight(),
                baseButton, icon, frame, highlighted, data.soundPath(), data.action());

            group.addActor(b);
            addButton(b);
            startX += baseButton.getWidth() + spacing;
        }

        for(Slider s : sliders) {
            if(s.equals(difficulty)) {
                tooltips.put(s, () -> {
                    float v = s.getValue();
                    if(v <= 0.30f) return Difficulty.ALPHA.getLabelKey();
                    else if(v <= 0.50f) return Difficulty.BETA.getLabelKey();
                    else if(v <= 0.70f) return Difficulty.CORONEL.getLabelKey();
                    else if(v <= 0.90f) return Difficulty.DELTA.getLabelKey();
                    else return Difficulty.SIGMA.getLabelKey();
                });
            }
            group.addActor(s);
        }
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

        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
        boolean hovering = false;

        for(Slider s : sliders) {
            if(s.isHovered() && s.equals(difficulty)) {
                Supplier<String> supplier = tooltips.get(s);
                getTooltip().setText(supplier.get());
                hovering = true;
                break;
            }
        }

        getTooltip().update(delta, hovering, mouseX, mouseY);
        getTooltip().render(getBatch());
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

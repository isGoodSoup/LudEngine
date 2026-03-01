package org.lud.game.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Group;
import org.lud.engine.core.AudioService;
import org.lud.engine.enums.Direction;
import org.lud.engine.gui.Button;
import org.lud.engine.gui.Menu;
import org.lud.game.data.ButtonData;
import org.lud.game.enums.UIButton;
import org.lud.game.service.GameService;

import java.util.ArrayList;
import java.util.List;

public class SettingsMenu extends Menu {
    private final GameService gameService;
    private final AudioService audioService;
    private final List<ButtonData> data;
    private Group group;
    private Texture baseButton;
    private Texture frame;

    public SettingsMenu(GameService gameService, AudioService audioService) {
        super(gameService, audioService);
        this.gameService = gameService;
        this.audioService = audioService;
        this.data = new ArrayList<>();
        addMenu(this);
        loadSprites();
    }

    public void loadSprites() {
        String defaultPath = "buttons/";
        this.baseButton = new Texture(defaultPath + "button_small.png");
        this.frame = new Texture(defaultPath + "button_small_highlighted.png");
        data.add(new ButtonData(UIButton.PREVIOUS_PAGE, gameService::showMainMenu,
            () -> audioService.playFX(0)));
    }

    @Override
    public void setup() {
        float spacing = 1f;
        float startX = 50f;
        float y = 50f;

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
        getStage().addActor(group);

        // TODO toggles
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

    @Override
    public void dispose() {
        super.dispose();
    }
}

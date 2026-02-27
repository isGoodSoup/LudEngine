package org.lud.game.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import org.lud.engine.enums.Direction;
import org.lud.engine.gui.Button;
import org.lud.engine.gui.Menu;
import org.lud.game.data.ButtonData;
import org.lud.game.enums.UIButton;
import org.lud.game.service.GameService;

import java.util.ArrayList;
import java.util.List;

public class LangMenu extends Menu {
    private final GameService gameService;
    private final List<ButtonData> data;
    private Texture baseButton;
    private Texture frame;

    public LangMenu(GameService gameService) {
        super(gameService);
        this.gameService = gameService;
        this.data = new ArrayList<>();
        addMenu(this);
        loadSprites();
    }

    public void loadSprites() {
        this.baseButton = new Texture("button_small.png");
        this.frame = new Texture("button_small_highlighted.png");
        data.add(new ButtonData(UIButton.PREVIOUS_PAGE, gameService::showMainMenu, getFx(0)));
    }

    @Override
    public void setup() {
        float spacing = 1f;
        float startX = 50f;
        float y = 50f;

        for(ButtonData data : data) {
            Texture icon = new Texture("button_" + data.type().getSuffix() + ".png");
            Texture highlighted = new Texture("button_" + data.type().getSuffix() + "_highlighted.png");

            Button b = new Button(startX, y,
                baseButton.getWidth(), baseButton.getHeight(),
                baseButton, icon, frame, highlighted, data.soundPath(), data.action());

            addButton(b);
            startX += baseButton.getWidth() + spacing;
        }
        // TODO Lang menu
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

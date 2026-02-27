package org.lud.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.lud.engine.gui.Button;
import org.lud.engine.gui.Colors;
import org.lud.engine.gui.Menu;
import org.lud.game.data.ButtonData;
import org.lud.game.enums.UIButton;
import org.lud.game.service.AudioService;
import org.lud.game.service.GameService;

import java.util.ArrayList;
import java.util.List;

public class BoardScreen extends Menu {
    private final GameService gameService;
    private final AudioService audioService;
    private ShapeRenderer shaper;
    private final List<ButtonData> data;
    private Texture baseButton;
    private Texture frame;

    public BoardScreen(GameService gameService, AudioService audioService) {
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
        data.add(new ButtonData(UIButton.PREVIOUS_PAGE, gameService::showMainMenu, () -> audioService.playFX(0)));
        data.add(new ButtonData(UIButton.RESET, gameService::newGame, () -> audioService.playFX(0)));
    }

    @Override
    public void setup() {
        float spacing = 1f;
        float startX = 50f;
        float y = 50f;

        for(ButtonData data : data) {
            Texture icon = getButton(data, false);
            Texture highlighted = getButton(data, true);

            Button b = new Button(startX, y, baseButton.getWidth(), baseButton.getHeight(),
                baseButton, icon, frame, highlighted, data.soundPath(), data.action());

            addButton(b);
            startX += baseButton.getWidth() + spacing;
        }
        // TODO Board buttons
    }

    @Override
    public void show() {
        this.shaper = new ShapeRenderer();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shaper.begin(ShapeRenderer.ShapeType.Filled);

        int tileSize = 64;
        int boardSize = tileSize * 8;
        int padding = 20;
        float startX = (Gdx.graphics.getWidth() - boardSize)/2f;
        float startY = (Gdx.graphics.getHeight() - boardSize)/2f;

        for(int row = 0; row < 8; row++) {
            for(int col = 0; col < 8; col++) {
                if ((row + col) % 2 == 0) {
                    shaper.setColor(Colors.getBackground());
                } else {
                    shaper.setColor(Colors.getForeground());
                }

                shaper.rect(startX - padding,startY - padding,
                    boardSize + padding, boardSize + padding);

                shaper.rect(startX + col * tileSize,
                    startY + row * tileSize,
                    tileSize, tileSize);
            }
        }
        shaper.end();

        super.render(delta);

        globalInput();
        checkInput();
    }

    @Override
    public void checkInput() {

    }

    @Override
    public void dispose() {
        shaper.dispose();
    }

}

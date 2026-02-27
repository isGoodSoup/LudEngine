package org.lud.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.lud.engine.core.GameFrame;
import org.lud.engine.gui.Button;
import org.lud.engine.gui.Menu;
import org.lud.game.data.ButtonData;
import org.lud.game.enums.UIButton;
import org.lud.game.screens.BoardScreen;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class MainMenu extends Menu {
    private static boolean isFadeShown;

    private final GameFrame gameFrame;
    private List<ButtonData> data;
    private Texture logo;
    private Texture baseButton;
    private Texture altButton;
    private Texture frame;

    private float fadeAlpha = 1f;
    private final float FADE_SPEED = 0.5f;
    private boolean isPlayButton;

    public MainMenu(GameFrame gameFrame) {
        super();
        this.gameFrame = gameFrame;
        this.data = new ArrayList<>();
        addMenu(this);
        loadSprites();
    }

    public void loadSprites() {
        this.logo = new Texture("logo_chess.png");
        this.altButton = new Texture("button.png");
        this.baseButton = new Texture("button_small.png");
        this.frame = new Texture("button_small_highlighted.png");

        data.add(new ButtonData(UIButton.PLAY, () -> gameFrame.setScreen
            (new BoardScreen(gameFrame)), "sounds/piece-fx.wav"));
        data.add(new ButtonData(UIButton.SETTINGS, () -> gameFrame.setScreen
            (new SettingsMenu(gameFrame)), "sounds/piece-fx.wav"));
        data.add(new ButtonData(UIButton.ACHIEVEMENTS, () -> gameFrame.setScreen
            (new AchievementsMenu(gameFrame)), "sounds/piece-fx.wav"));
        data.add(new ButtonData(UIButton.EXIT, Gdx.app::exit, "sounds/piece-fx.wav"));
    }

    @Override
    public void setup() {
        float spacing = 1f;
        float totalWidth = 0;

        for(ButtonData data : data) {
            totalWidth += baseButton.getWidth();
        }
        totalWidth += spacing * (data.size() - 1);

        float startX = (Gdx.graphics.getWidth() - totalWidth)/2f;
        float y = 200f;

        for(ButtonData data : data) {
            Texture icon = new Texture("button_" + data.type().getSuffix() + ".png");
            Texture highlighted = new Texture("button_" + data.type().getSuffix() + "_highlighted.png");

            if(!isPlayButton && data.type().getSuffix().equals("play")) {
                isPlayButton = true;
            } else {
                isPlayButton = false;
            }

            Button b = new Button(startX, y - baseButton.getHeight()/2f,
                baseButton.getWidth(), baseButton.getHeight(),
                isPlayButton ? altButton : baseButton, icon, frame, highlighted, data.sound(), data.action());

            addButton(b);
            startX += baseButton.getWidth() + spacing;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.draw(logo, Gdx.graphics.getWidth()/2 - logo.getWidth()/2, Gdx.graphics.getHeight()/2);
        for(Button b : getButtons()) { b.render(batch); }

        checkInput();

        if(!isFadeShown) {
            if(fadeAlpha > 0f) {
                fadeAlpha -= FADE_SPEED * Gdx.graphics.getDeltaTime();
                if(fadeAlpha <= 0f) {
                    fadeAlpha = 0f;
                    isFadeShown = true;
                }

                batch.end();
                ShapeRenderer shape = new ShapeRenderer();
                shape.begin(ShapeRenderer.ShapeType.Filled);
                shape.setColor(0, 0, 0, fadeAlpha);
                shape.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                shape.end();
                shape.dispose();
                batch.begin();
            }
        }
    }

    @Override
    public void checkInput() {

    }

    @Override
    public void dispose() {
        super.dispose();
    }
}

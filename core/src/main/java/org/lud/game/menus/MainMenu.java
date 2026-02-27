package org.lud.game.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.lud.engine.enums.Direction;
import org.lud.engine.gui.Button;
import org.lud.engine.gui.Localization;
import org.lud.engine.gui.Menu;
import org.lud.game.data.ButtonData;
import org.lud.game.data.Tooltip;
import org.lud.game.enums.UIButton;
import org.lud.game.service.GameService;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("ALL")
public class MainMenu extends Menu {
    private static boolean isFadeShown;

    private Map<Button, String> tooltips;
    private final GameService gameService;
    private List<ButtonData> data;
    private Texture logo;
    private Texture baseButton;
    private Texture altButton;
    private Texture frame;

    private float fadeAlpha = 1f;
    private final float FADE_SPEED = 0.5f;
    private boolean isPlayButton;
    private Tooltip tooltip;

    public MainMenu(GameService gameService) {
        super(gameService);
        this.tooltips = new LinkedHashMap<>();
        this.gameService = gameService;
        this.data = new ArrayList<>();

        this.tooltip = new Tooltip("", getFont());

        addMenu(this);
        loadSprites();
    }

    public void loadSprites() {
        String defaultPath = "buttons/";
        this.logo = new Texture("logo_chess.png");
        this.altButton = new Texture(defaultPath + "button.png");
        this.baseButton = new Texture(defaultPath + "button_small.png");
        this.frame = new Texture(defaultPath + "button_small_highlighted.png");

        data.add(new ButtonData(UIButton.PLAY, gameService::newGame, getFx(0)));
        data.add(new ButtonData(UIButton.SETTINGS, gameService::showSettings, getFx(0)));
        data.add(new ButtonData(UIButton.ACHIEVEMENTS, gameService::showAchievements, getFx(0)));
        data.add(new ButtonData(UIButton.LANG, gameService::showLang, getFx(0)));
        data.add(new ButtonData(UIButton.EXIT, gameService::exit, getFx(0)));
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
            String defaultPath = "buttons/";
            Texture icon = new Texture(defaultPath + "button_" + data.type().getSuffix() + ".png");
            Texture highlighted = new Texture(defaultPath + "button_" + data.type().getSuffix() + "_highlighted.png");

            if(!isPlayButton && data.type().getSuffix().equals("play")) {
                isPlayButton = true;
            } else {
                isPlayButton = false;
            }

            Button b = new Button(startX, y - baseButton.getHeight()/2f,
                baseButton.getWidth(), baseButton.getHeight(),
                isPlayButton ? altButton : baseButton, icon, frame, highlighted, data.soundPath(), data.action());

            tooltips.put(b, Localization.lang.t("tooltip." + data.type().getSuffix()));
            addButton(b);
            startX += baseButton.getWidth() + spacing;
        }
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        getBatch().begin();
        getBatch().draw(logo, Gdx.graphics.getWidth()/2 - logo.getWidth()/2, Gdx.graphics.getHeight()/2);
        getBatch().end();

        globalInput();
        checkInput();

        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
        boolean hovering = false;

        for (Button b : getButtons()) {
            if (b.isHovered()) {
                tooltip.setText(tooltips.get(b));
                hovering = true;
                break;
            }
        }

        tooltip.update(delta, hovering, mouseX, mouseY);
        tooltip.render(getBatch(), getShaper());

        if(!isFadeShown) {
            if(fadeAlpha > 0f) {
                fadeAlpha -= FADE_SPEED * Gdx.graphics.getDeltaTime();
                if(fadeAlpha <= 0f) {
                    fadeAlpha = 0f;
                    isFadeShown = true;
                }

                getShaper().begin(ShapeRenderer.ShapeType.Filled);
                getShaper().setColor(0, 0, 0, fadeAlpha);
                getShaper().rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
                getShaper().end();
            }
        }
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
        tooltip.getFont().dispose();
    }
}

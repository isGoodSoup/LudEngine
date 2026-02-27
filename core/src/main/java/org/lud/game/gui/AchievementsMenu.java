package org.lud.game.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.lud.engine.core.GameFrame;
import org.lud.engine.gui.Button;
import org.lud.engine.gui.Menu;
import org.lud.game.data.ButtonData;
import org.lud.game.enums.UIButton;

import java.util.ArrayList;
import java.util.List;

public class AchievementsMenu extends Menu {
    private final GameFrame gameFrame;
    private final List<ButtonData> data;
    private Texture baseButton;
    private Texture frame;

    public AchievementsMenu(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        this.data = new ArrayList<>();
        addMenu(this);
        loadSprites();
    }

    public void loadSprites() {
        this.baseButton = new Texture("button_small.png");
        this.frame = new Texture("button_small_highlighted.png");

        data.add(new ButtonData(UIButton.PREVIOUS_PAGE, () -> gameFrame.setScreen
            (new MainMenu(gameFrame)), "sounds/piece-fx.wav"));
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
                baseButton, icon, frame, highlighted, data.sound(), data.action());

            addButton(b);
            startX += baseButton.getWidth() + spacing;
        }

        // TODO Achievements menu
    }

    @Override
    public void render(SpriteBatch batch) {
        for(Button b : getButtons()) { b.render(batch); }
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}

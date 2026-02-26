package org.lud.game.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.lud.engine.core.GameFrame;
import org.lud.engine.enums.MenuType;
import org.lud.engine.gui.Button;
import org.lud.engine.gui.Menu;

@SuppressWarnings("ALL")
public class MainMenu extends Menu {
    private Texture baseButton;
    private Texture frame;

    public MainMenu(GameFrame gameFrame) {
        super();
        this.baseButton = new Texture("button_small.png");
        this.frame = new Texture("button_small_highlighted.png");
    }

    @Override
    public void setup() {
        addButton(new Button(100f, 100f, baseButton.getWidth(),
            baseButton.getHeight(), baseButton, frame, () -> {
                setMenuType(MenuType.TABLE);
        }));

        addButton(new Button(100f, 100f - baseButton.getHeight(), baseButton.getWidth(),
            baseButton.getHeight(), baseButton, frame, () -> {
                setMenuType(MenuType.EXIT);
                System.exit(0);
        }));
    }

    @Override
    public void render(SpriteBatch batch) {
        for(Button b : buttons) { b.render(batch); }
    }

    @Override
    public void dispose() {
        super.dispose();
    }
}

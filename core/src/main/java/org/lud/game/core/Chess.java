package org.lud.game.core;

import org.lud.engine.core.GameFrame;
import org.lud.engine.screen.IntroScreen;
import org.lud.game.gui.MainMenu;

public class Chess extends GameFrame {
    @Override
    public void create() {
        setNextMenu(() -> new MainMenu(this));
        setScreen(new IntroScreen(this));


    }
}

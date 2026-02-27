package org.lud.game.core;

import org.lud.engine.core.GameFrame;
import org.lud.engine.screen.IntroScreen;
import org.lud.game.service.ServiceFactory;

public class Chess extends GameFrame {
    private ServiceFactory service;

    @Override
    public void create() {
        this.service = new ServiceFactory();
        setScreen(new IntroScreen(this));
    }
}

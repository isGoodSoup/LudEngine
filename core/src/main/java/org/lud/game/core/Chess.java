package org.lud.game.core;

import org.lud.engine.core.GameFrame;
import org.lud.engine.enums.Theme;
import org.lud.engine.gui.Colors;
import org.lud.engine.screen.IntroScreen;
import org.lud.game.service.ServiceFactory;

public class Chess extends GameFrame {
    private ServiceFactory service;

    @Override
    public void create() {
        this.service = new ServiceFactory(this);
        service.getAudioService().playMusic();
        Colors.setTheme(Theme.LEGACY);
        setScreen(new IntroScreen(this,
            service.getGameService(), service.getAudioService()));
    }

    @Override
    public void render() {
        service.getAudioService().setMusicVolume(0.5f);
        super.render();
    }
}

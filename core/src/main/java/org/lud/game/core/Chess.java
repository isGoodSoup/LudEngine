package org.lud.game.core;

import org.lud.engine.core.GameFrame;
import org.lud.engine.enums.Theme;
import org.lud.engine.gui.Colors;
import org.lud.engine.gui.Localization;
import org.lud.engine.screen.IntroScreen;
import org.lud.game.service.ServiceFactory;

import java.util.Locale;

public class Chess extends GameFrame {
    private ServiceFactory service;

    @Override
    public void create() {
        this.service = new ServiceFactory(this);
        service.getAudioService().playMusic();
        Localization.lang.setLocale(Locale.forLanguageTag("en"));
        Colors.setTheme(Theme.LEGACY);
        setScreen(new IntroScreen(this,
            service.getGameService(), service.getAudioService()));
    }

    @Override
    public void render() {
        service.getAudioService().setMusicVolume(0.6f);
        super.render();
    }
}

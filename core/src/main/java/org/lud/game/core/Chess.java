package org.lud.game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import org.lud.engine.core.GameFrame;
import org.lud.engine.enums.Theme;
import org.lud.engine.gui.Colors;
import org.lud.engine.gui.Localization;
import org.lud.engine.screen.IntroScreen;
import org.lud.game.service.ServiceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public class Chess extends GameFrame {
    private static final Logger log = LoggerFactory.getLogger(Chess.class);
    private ServiceFactory service;
    private OrthographicCamera camera;

    @Override
    public void create() {
        log.info("SoS (Start of Session)");
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight());
        camera.update();
        service = new ServiceFactory(this, camera);
        service.getAudioService().playMusic();
        service.getAudioService().setMusicVolume(0.6f);
        Localization.lang.setLocale(Locale.forLanguageTag("en"));
        Colors.setTheme(Theme.LEGACY);
        setScreen(new IntroScreen(this, service.getGameService(),
            service.getAudioService(), service.getBoardService()));
    }

    @Override
    public void render() {
        camera.update();
        super.render();
    }
}

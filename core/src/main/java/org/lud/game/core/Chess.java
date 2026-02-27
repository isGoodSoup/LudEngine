package org.lud.game.core;

import com.badlogic.gdx.Gdx;
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
        setScreen(new IntroScreen(this, service.getGameService()));
    }

    @Override
    public void render() {
        float delta = Gdx.graphics.getDeltaTime();
        service.getAudioService().update(delta);
        super.render();
    }
}

package org.lud.game.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import org.lud.engine.core.GameFrame;
import org.lud.engine.enums.Theme;
import org.lud.engine.gui.Colors;
import org.lud.engine.gui.Localization;
import org.lud.engine.input.BoardInput;
import org.lud.engine.screen.IntroScreen;
import org.lud.game.input.Coordinator;
import org.lud.game.service.ServiceFactory;

import java.util.Locale;

public class Chess extends GameFrame {
    private ServiceFactory service;
    private OrthographicCamera camera;
    private Stage stage;

    private Coordinator coordinator;
    private BoardInput boardInput;
    private InputMultiplexer multiplexer;

    @Override
    public void create() {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight());
        camera.update();
        service = new ServiceFactory(this, camera);
        stage = new Stage();
        coordinator = new Coordinator();
        boardInput = new BoardInput(service.getBoardService(), stage,
            service.getPieceService(), service.getAudioService(),
            service.getGameService());
        multiplexer = new InputMultiplexer(boardInput, coordinator, stage);
        Gdx.input.setInputProcessor(multiplexer);
        service.getAudioService().playMusic();
        Localization.lang.setLocale(Locale.forLanguageTag("en"));
        Colors.setTheme(Theme.LEGACY);
        setScreen(new IntroScreen(this, service.getGameService(),
            service.getAudioService()));
    }

    @Override
    public void render() {
        service.getAudioService().setMusicVolume(0.6f);
        camera.update();
        super.render();
    }

    public Stage getStage() {
        return stage;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }
}

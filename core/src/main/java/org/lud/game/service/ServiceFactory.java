package org.lud.game.service;

import com.badlogic.gdx.graphics.OrthographicCamera;
import org.lud.engine.bots.*;
import org.lud.engine.core.AudioService;
import org.lud.engine.core.GameFrame;

public class ServiceFactory {
    private final GameFrame gameFrame;
    private final AudioService audioService;
    private final BoardService boardService;
    private final PieceService pieceService;
    private final GameService gameService;
    private final OrthographicCamera camera;

    private final Alpha alpha;
    private final Beta beta;
    private final Coronel coronel;
    private final Delta delta;
    private final Sigma sigma;

    public ServiceFactory(GameFrame gameFrame, OrthographicCamera camera) {
        this.gameFrame = gameFrame;
        this.camera = camera;
        this.audioService = new AudioService();
        this.boardService = new BoardService(this, camera);
        this.pieceService = new PieceService(this);
        this.gameService = new GameService(gameFrame, this);

        this.alpha = new Alpha();
        this.beta = new Beta();
        this.coronel = new Coronel();
        this.delta = new Delta();
        this.sigma = new Sigma();
    }

    public AudioService getAudioService() {
        return audioService;
    }
    public BoardService getBoardService() {
        return boardService;
    }
    public PieceService getPieceService() {
        return pieceService;
    }
    public GameService getGameService() {
        return gameService;
    }
}

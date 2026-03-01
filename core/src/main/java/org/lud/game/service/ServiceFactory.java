package org.lud.game.service;

import com.badlogic.gdx.graphics.OrthographicCamera;
import org.lud.engine.bots.Alpha;
import org.lud.engine.core.AudioService;
import org.lud.engine.core.GameFrame;

public class ServiceFactory {
    private final GameFrame gameFrame;
    private final AudioService audioService;
    private final BoardService boardService;
    private final PieceService pieceService;
    private final GameService gameService;
    private final OrthographicCamera camera;

    private final Alpha alphaAI;

    public ServiceFactory(GameFrame gameFrame, OrthographicCamera camera) {
        this.gameFrame = gameFrame;
        this.camera = camera;
        this.audioService = new AudioService();
        this.boardService = new BoardService(this, camera);
        this.pieceService = new PieceService(this);
        this.gameService = new GameService(gameFrame, this);

        this.alphaAI = new Alpha();
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

    public Alpha getAlphaAI() {
        return alphaAI;
    }
}

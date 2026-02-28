package org.lud.game.service;

import org.lud.engine.core.AudioService;
import org.lud.engine.core.GameFrame;

public class ServiceFactory {
    private final GameFrame gameFrame;
    private final AudioService audioService;
    private final BoardService boardService;
    private final GameService gameService;
    private final PieceService pieceService;

    public ServiceFactory(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        this.audioService = new AudioService();
        this.boardService = new BoardService(this);
        this.gameService = new GameService(gameFrame, this);
        this.pieceService = new PieceService(this);
    }

    public AudioService getAudioService() {
        return audioService;
    }
    public BoardService getBoardService() {
        return boardService;
    }
    public GameService getGameService() {
        return gameService;
    }
    public PieceService getPieceService() {
        return pieceService;
    }
}

package org.lud.game.service;

import org.lud.engine.core.GameFrame;

public class ServiceFactory {
    private final GameFrame gameFrame;
    private final BoardService boardService;
    private final GameService gameService;
    private final PieceService pieceService;

    public ServiceFactory(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        this.boardService = new BoardService(this);
        this.gameService = new GameService(gameFrame, this);
        this.pieceService = new PieceService(this);
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

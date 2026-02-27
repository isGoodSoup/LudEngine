package org.lud.game.service;

public class ServiceFactory {
    private final BoardService boardService;
    private final GameService gameService;
    private final PieceService pieceService;

    public ServiceFactory() {
        this.boardService = new BoardService(this);
        this.gameService = new GameService(this);
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

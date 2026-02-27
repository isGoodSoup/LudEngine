package org.lud.game.service;

import org.lud.engine.enums.Turn;
import org.lud.game.entities.Piece;

public class GameService {
    private Turn turn;
    private final ServiceFactory service;

    private boolean isLegal;

    public GameService(ServiceFactory service) {
        this.service = service;
    }

    public Turn getTurn() {
        return turn;
    }
    public void setTurn(Turn turn) {
        this.turn = turn;
    }

    public boolean isLegal() {
        return isLegal;
    }
    public void setLegal(boolean legal) {
        isLegal = legal;
    }

    public boolean isCheckmate(Piece piece) {
        return false;
    }
}

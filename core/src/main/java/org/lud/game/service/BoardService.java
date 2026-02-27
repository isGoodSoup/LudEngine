package org.lud.game.service;

import org.lud.game.entities.Board;
import org.lud.game.entities.Piece;

public class BoardService {
    private final Board board;
    private final ServiceFactory service;

    public BoardService(ServiceFactory service) {
        this.board = new Board();
        this.service = service;
    }

    public void setPieces() {
        for(Piece[] pieces : board.getPieces()) {
            for(Piece p : pieces) {
                addPiece(p);
            }
        }
    }

    public void addPiece(Piece p) {
        if(isWithinBoard(p.col(), p.row())) {
            board.getPieces()[p.row()][p.col()] = p;
        }
    }

    public void removePiece(Piece p) {
        if(isWithinBoard(p.col(), p.row())) {
            board.getPieces()[p.row()][p.col()] = null;
        }
    }

    public Piece getPieceAt(int col, int row) {
        if(isWithinBoard(col, row)) {
            return board.getPieces()[row][col];
        }
        return null;
    }

    private boolean isWithinBoard(int col, int row) {
        return col >= 0 && col < Board.getSIZE() && row >= 0 && row < Board.getSIZE();
    }
}

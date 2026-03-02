package org.lud.game.moves;

import org.lud.engine.enums.Turn;
import org.lud.engine.interfaces.Moves;
import org.lud.game.actors.Piece;
import org.lud.game.service.BoardService;

public record MovePiece(Piece piece, int fromCol, int fromRow, int targetCol, int targetRow,
                        Turn color, Piece captured) implements Moves {
    @Override
    public void apply() {
        BoardService.getBoard().getPieces()[fromRow][fromCol] = null;
        if (captured != null) {
            BoardService.getBoard().getPieces()[captured.getRow()][captured.getCol()] = null;
        }

        piece.setCol(targetCol);
        piece.setRow(targetRow);
        piece.setHasMoved(true);
        BoardService.getBoard().getPieces()[targetRow][targetCol] = piece;
    }

    @Override
    public void undo() {
        BoardService.getBoard().getPieces()[targetRow][targetCol] = null;
        if(captured != null) {
            BoardService.getBoard().getPieces()[captured.getRow()][captured.getCol()] = captured;
        }
        piece.setCol(fromCol);
        piece.setRow(fromRow);
        piece.setHasMoved(false);
        BoardService.getBoard().getPieces()[fromRow][fromCol] = piece;
    }

    @Override
    public int getScoreImpact() {
        return piece.getPieceValue(piece);
    }

    @Override
    public int isCapture() {
        if(captured != null) {
            return 5;
        }
        return 0;
    }

    @Override
    public int isUnique() {
        return 10;
    }

    @Override
    public Turn getTurn() {
        return Turn.getTurn();
    }

    @Override
    public boolean isTurn() {
        return Turn.getTurn() == Turn.DARK;
    }
}

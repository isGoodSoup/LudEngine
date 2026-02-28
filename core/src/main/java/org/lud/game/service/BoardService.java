package org.lud.game.service;

import com.badlogic.gdx.graphics.OrthographicCamera;
import org.lud.engine.enums.Turn;
import org.lud.game.actors.PieceActor;
import org.lud.game.data.Piece;
import org.lud.game.entities.Board;
import org.lud.game.enums.TypeID;
import org.lud.game.moves.Move;

import java.util.ArrayList;
import java.util.List;

public class BoardService {
    private final Board board;
    private final ServiceFactory service;
    private final OrthographicCamera camera;

    private final List<Move> moves;

    public BoardService(ServiceFactory service, OrthographicCamera camera) {
        this.camera = camera;
        this.service = service;
        this.board = new Board();
        this.moves = new ArrayList<>();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public Board getBoard() {
        return board;
    }

    public void setPieces() {
        for(PieceActor[] pieces : board.getPieces()) {
            for(PieceActor p : pieces) {
                addPiece(p);
            }
        }
    }

    public void addPiece(PieceActor p) {
        if(isWithinBoard(p.getPiece().col(), p.getPiece().row())) {
            board.getPieces()[p.getPiece().row()][p.getPiece().col()] = p;
        }
    }

    public void removePiece(PieceActor p) {
        if(isWithinBoard(p.getPiece().col(), p.getPiece().row())) {
            board.getPieces()[p.getPiece().row()][p.getPiece().col()] = null;
        }
    }

    public PieceActor getPieceAt(float col, float row) {
        if(isWithinBoard(col, row)) {
            return board.getPieces()[(int) row][(int) col];
        }
        return null;
    }

    public void attemptMove(PieceActor pa, int targetCol, int targetRow) {


        pa.setHasMoved(true);
    }

    private boolean isEnPassantMove(PieceActor pa, int targetCol, int targetRow) {
        if(pa.getPiece().type() != TypeID.PAWN) return false;

        int colDiff = Math.abs(targetCol - pa.getPiece().col());
        if(colDiff != 1 || targetRow == pa.getPiece().row()) { return false; }

        for(Piece p : service.getPieceService().getPieces()) {
            if(p.type() == TypeID.PAWN &&
                p.color() != pa.getPiece().color() &&
                p.col() == targetCol &&
                p.row() == pa.getPiece().row() &&
                pa.isTwoStepsAhead()) {
                return true;
            }
        }

        return false;
    }

    private void executeCastling(PieceActor kingActor, int targetCol) {

    }

    private void executeEnPassant(PieceActor pa, int targetCol, int targetRow) {

        int oldRow = pa.getPreRow();
        int movedSquares = Math.abs(targetRow - oldRow);

        if(Math.abs(targetCol - pa.getPreCol()) == 1) {
            int dir = (pa.getPiece().color() == Turn.LIGHT) ? -1 : 1;

            if(targetRow - oldRow == dir) {
                PieceActor captured = service.getBoardService()
                    .getPieceAt(targetCol, oldRow);

                if(captured != null &&
                    captured.getPiece().type() == TypeID.PAWN &&
                    captured.getPiece().color() != pa.getPiece().color() &&
                    captured.isTwoStepsAhead()) {

                    service.getBoardService().removePiece(captured);
                    Move newMove = getMoveEvent();
                    moves.add(newMove);
                }
            }
        }
        pa.setTwoStepsAhead(movedSquares == 2);
    }

    private Move getMoveEvent() {
        Move l = moves.getLast();

        return new Move(
            l.piece(),
            l.fromRow(),
            l.fromCol(),
            l.targetCol(),
            l.targetRow(),
            l.color(),
            l.captured()
        );
    }

    private boolean isWithinBoard(float col, float row) {
        return col >= 0 && col < Board.getSIZE() && row >= 0 && row < Board.getSIZE();
    }
}

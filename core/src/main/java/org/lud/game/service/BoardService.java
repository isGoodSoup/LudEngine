package org.lud.game.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import org.lud.game.entities.Board;
import org.lud.game.entities.Piece;
import org.lud.game.enums.TypeID;
import org.lud.game.moves.Move;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class BoardService {
    private static final Logger log = LoggerFactory.getLogger(BoardService.class);
    private static Board board;
    private final ServiceFactory service;
    private final OrthographicCamera camera;
    private final List<Move> moves;

    public BoardService(ServiceFactory service, OrthographicCamera camera) {
        this.camera = camera;
        this.service = service;
        board = new Board();
        this.moves = new ArrayList<>();
    }

    public OrthographicCamera getCamera() { return camera; }
    public Board getBoard() { return board; }

    public void setPieces() {
        for(Piece[] row : board.getPieces()) {
            for(Piece p : row) {
                if(p != null) addPiece(p);
            }
        }
    }

    public void addPiece(Piece p) {
        if(isWithinBoard(p.getCol(), p.getRow())) {
            board.getPieces()[p.getRow()][p.getCol()] = p;
        }
    }

    public void removePiece(Piece p) {
        if(isWithinBoard(p.getCol(), p.getRow())) {
            board.getPieces()[p.getRow()][p.getCol()] = null;
        }
    }

    public static Piece getPieceAt(int col, int row) {
        if(isWithinBoard(col, row)) {
            return board.getPieces()[row][col];
        }
        return null;
    }

    public boolean attemptMove(Piece piece, int targetCol, int targetRow) {
        if(!isWithinBoard(targetCol, targetRow)) { return false; }

        // TODO: implement special logic for pawns, kings, castling, en-passant

        if(isValidSquare(piece, targetCol, targetRow, service.getPieceService().getPieces()) &&
            service.getGameService().canMove(piece, targetCol, targetRow)) {

            Piece captured = getPieceAt(targetCol, targetRow);
            if(captured != null) {
                service.getPieceService().removePiece(captured);
            }

            moves.add(new Move(piece, piece.getCol(), piece.getRow(),
                targetCol, targetRow, piece.getColor(), captured));

            piece.setCol(targetCol);
            piece.setRow(targetRow);
            piece.setHasMoved(true);

            log.info("{} {}: {} to {}", piece.getColor(), piece.getTypeID(),
                board.getSquareNameAt(piece.getCol(), piece.getRow()),
                board.getSquareNameAt(targetCol, targetRow));

            return true;
        }

        return false;
    }

    public static boolean isWithinBoard(int col, int row) {
        return col >= 0 && col < 8 && row >= 0 && row < 8;
    }

    public static boolean isValidSquare(Piece piece, int targetCol, int targetRow, List<Piece> board) {
        for (Piece p : board) {
            if (p.getCol() == targetCol && p.getRow() == targetRow) {
                return p.getColor() != piece.getColor();
            }
        }
        return true;
    }

    public static boolean isPathClear(Piece piece, int targetCol, int targetRow) {
        int colDiff = targetCol - piece.getCol();
        int rowDiff = targetRow - piece.getRow();

        switch(piece.getTypeID()) {
            case KNIGHT:
                return true;
            default:
                break;
        }

        int colStep = Integer.signum(colDiff);
        int rowStep = Integer.signum(rowDiff);

        if(colStep == 0 && rowStep == 0) {
            return false;
        }

        if(colStep != 0 && rowStep != 0 && Math.abs(colDiff) != Math.abs(rowDiff)) {
            return false;
        }

        int currentCol = piece.getCol() + colStep;
        int currentRow = piece.getRow() + rowStep;

        while(currentCol != targetCol || currentRow != targetRow) {
            if(getPieceAt(currentCol, currentRow) != null) {
                return false;
            }
        }
        return true;
    }

    public boolean canEnPassant(Piece piece, int targetCol, int targetRow, List<Piece> board) {
        for(Piece p : board) {
            if(p.getTypeID() == TypeID.PAWN && p.getColor() != piece.getColor()
                && p.getCol() == targetCol && p.getRow() == piece.getRow() && p.isTwoStepsAhead()) {
                p.setOther(p);
                return true;
            }
        }
        return false;
    }

    private Move addMove(Move move) {
        moves.add(move);
        return move;
    }

    private boolean isEnPassantMove(Piece pa, int targetCol, int targetRow) {
        return false;
    }

    private void executeCastling(Piece king, int targetCol) {}

    private void executeEnPassant(Piece pa, int targetCol, int targetRow) {}

    public float getBoardStartX() {
        return (Gdx.graphics.getWidth() - Board.getSIZE())/2f;
    }

    public float getBoardStartY() {
        return (Gdx.graphics.getHeight() - Board.getSIZE()) / 2f;
    }
}

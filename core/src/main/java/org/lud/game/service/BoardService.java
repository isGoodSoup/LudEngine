package org.lud.game.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import org.lud.game.entities.Board;
import org.lud.game.entities.Piece;
import org.lud.game.moves.Move;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
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

    public Piece getPieceAt(int col, int row) {
        if(isWithinBoard(col, row)) {
            return board.getPieces()[row][col];
        }
        return null;
    }

    public boolean attemptMove(Piece piece, int targetCol, int targetRow) {
        if(!isWithinBoard(targetCol, targetRow)) return false;

        // TODO: implement special logic for pawns, kings, castling, en-passant

        if(isValidSquare(piece, targetCol, targetRow, service.getPieceService().getPieces())) {
            Piece captured = getPieceAt(targetCol, targetRow);
            if(captured != null) {
                service.getPieceService().removePiece(captured);
            }

            moves.add(new Move(piece, piece.getCol(), piece.getRow(),
                targetCol, targetRow, piece.getColor(), captured));

            piece.setCol(targetCol);
            piece.setRow(targetRow);

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
        return (Gdx.graphics.getWidth() - Board.getSIZE()) / 2f;
    }

    public float getBoardStartY() {
        return (Gdx.graphics.getHeight() - Board.getSIZE()) / 2f;
    }
}

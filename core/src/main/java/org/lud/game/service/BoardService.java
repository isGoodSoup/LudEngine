package org.lud.game.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import org.lud.engine.enums.Difficulty;
import org.lud.engine.enums.Turn;
import org.lud.engine.interfaces.AI;
import org.lud.engine.interfaces.Moves;
import org.lud.game.actors.Piece;
import org.lud.game.entities.Board;
import org.lud.game.enums.TypeID;
import org.lud.game.moves.MovePiece;
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

    private final List<Moves> movePieces;
    private final List<Moves> moveAIPieces;

    private Difficulty difficulty;
    private AI currentAI;
    private boolean canUndo;

    public BoardService(ServiceFactory service, OrthographicCamera camera) {
        this.camera = camera;
        this.service = service;
        board = new Board();
        this.movePieces = new ArrayList<>();
        this.moveAIPieces = new ArrayList<>();
        this.difficulty = Difficulty.ALPHA;
        this.currentAI = Difficulty.setDiff(this.difficulty);
        canUndo = true;
    }

    public OrthographicCamera getCamera() { return camera; }
    public static Board getBoard() { return board; }

    public void setPieces() {
        for(Piece[] col : board.getPieces()) {
            for(Piece p : col) {
                if(p != null) { addPiece(p); }
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
        try {
            if(isWithinBoard(col, row)) {
                return board.getPieces()[row][col];
            }
        } catch(NullPointerException e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public AI switchDifficulties(Difficulty d) {
        this.difficulty = d;
        this.currentAI = Difficulty.setDiff(d);
        return this.currentAI;
    }

    public Difficulty getDifficulty() {
        return difficulty;
    }

    public boolean attemptMove(Piece piece, int targetCol, int targetRow) {
        if(!isWithinBoard(targetCol, targetRow)) { return false; }

        // TODO: implement special logic for pawns, kings, castling, en-passant

        if(isValidSquare(piece, targetCol, targetRow, service.getPieceService().getPieces()) &&
            service.getGameService().canMove(piece, targetCol, targetRow)) {

            Piece captured = getPieceAt(targetCol, targetRow);
            if(captured != null) {
                if(captured.getParent() != null) { captured.remove(); }
                service.getPieceService().removePiece(captured);
                log.debug("{} {} > {} {}", captured.getTurn(), captured.getTypeID(),
                    piece.getTurn(), piece.getTypeID());
            }

            MovePiece move = new MovePiece(piece, piece.getCol(), piece.getRow(),
                            targetCol, targetRow, piece.getTurn(), captured);

            move.apply();
            movePieces.add(move);

            logMove(piece, targetCol, targetRow);

            AI ai = currentAI;
            ai.switchTurns();

            if(Turn.getTurn() == Turn.DARK) {
                List<Moves> legalMoves = service.getGameService().newLegalMoves(Turn.DARK);
                Moves n = ai.chooseMove(legalMoves);
                if(n instanceof MovePiece) {
                    service.getGameService().setInputLocked(true);
                    moveAIPieces.add(n);
                    logMove(((MovePiece) n).piece(),
                        ((MovePiece) n).targetCol(),
                        ((MovePiece)n).targetRow());
                }
                service.getAudioService().playFX(0);
                canUndo = !canUndo;
                ai.switchTurns();
            }
            return true;
        }

        return false;
    }

    public void undoMove() {
        if(movePieces.isEmpty()) { return; }
        if(!canUndo) { return; }
        List<Moves> moves = movePieces;
        MovePiece move = (MovePiece) moves.getLast();
        move.undo();
        canUndo = false;
    }

    private void logMove(Piece p, int targetCol, int targetRow) {
        log.info("{} {}: {} to {}", p.getTurn(), p.getTypeID(),
            board.getSquareNameAt(p.getPreCol(), p.getPreRow()),
            board.getSquareNameAt(targetCol, targetRow));
    }

    public List<Moves> getMovePieces() {
        return movePieces;
    }
    public List<Moves> getMoveAIPieces() {
        return moveAIPieces;
    }

    public static boolean isWithinBoard(int col, int row) {
        return col >= 0 && col < 8 && row >= 0 && row < 8;
    }

    public static boolean isValidSquare(Piece piece, int targetCol, int targetRow, List<Piece> board) {
        for (Piece p : board) {
            if (p.getCol() == targetCol && p.getRow() == targetRow) {
                return p.getTurn() != piece.getTurn();
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

        if(colStep == 0 && rowStep == 0) { return false; }
        if(colStep != 0 && rowStep != 0 && Math.abs(colDiff) != Math.abs(rowDiff)) { return false; }

        int currentCol = piece.getCol() + colStep;
        int currentRow = piece.getRow() + rowStep;

        while(currentCol != targetCol || currentRow != targetRow) {
            if(getPieceAt(currentCol, currentRow) != null) { return false; }
            currentCol += colStep;
            currentRow += rowStep;
        }
        return true;
    }

    public boolean canEnPassant(Piece piece, int targetCol, int targetRow, List<Piece> board) {
        for(Piece p : board) {
            if(p.getTypeID() == TypeID.PAWN && p.getTurn() != piece.getTurn()
                && p.getCol() == targetCol && p.getRow() == piece.getRow() && p.isTwoStepsAhead()) {
                p.setOther(p);
                return true;
            }
        }
        return false;
    }

    private MovePiece addMove(MovePiece movePiece) {
        movePieces.add(movePiece);
        return movePiece;
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

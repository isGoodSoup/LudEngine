package org.lud.game.service;

import com.badlogic.gdx.Gdx;
import org.lud.engine.core.AudioService;
import org.lud.engine.core.GameFrame;
import org.lud.engine.enums.Turn;
import org.lud.engine.gui.Menu;
import org.lud.engine.interfaces.Moves;
import org.lud.game.actors.Piece;
import org.lud.game.entities.Board;
import org.lud.game.enums.TypeID;
import org.lud.game.moves.MovePiece;
import org.lud.game.screens.AchievementsMenu;
import org.lud.game.screens.BoardScreen;
import org.lud.game.screens.MainMenu;
import org.lud.game.screens.SettingsMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class GameService {
    private static final Logger log = LoggerFactory.getLogger(GameService.class);
    private final GameFrame gameFrame;
    private final ServiceFactory service;

    private Menu activeMenu;
    private final Menu mainMenu;
    private final Menu settingsMenu;
    private final Menu achievementsMenu;
    private final BoardScreen boardScreen;

    private Piece checkingPiece;

    private boolean isLegal;
    private boolean isFirstBoardEntry = true;
    private boolean isInputLocked;

    public GameService(GameFrame gameFrame, ServiceFactory service) {
        this.gameFrame = gameFrame;
        this.service = service;

        AudioService audio = service.getAudioService();
        PieceService piece = service.getPieceService();
        BoardService board = service.getBoardService();

        this.mainMenu = new MainMenu(this, audio, board);
        this.settingsMenu = new SettingsMenu(this, audio, board);
        this.achievementsMenu = new AchievementsMenu(this, audio, board);
        this.boardScreen = new BoardScreen(board, this, piece, audio);

        activeMenu = mainMenu;
    }

    public boolean isFirstBoardEntry() { return isFirstBoardEntry; }
    public void resetFirstBoardEntry() { isFirstBoardEntry = !isFirstBoardEntry; }

    public void showMainMenu() {
        activeMenu = mainMenu;
        gameFrame.setScreen(activeMenu);
    }
    public void showBoard() {
        activeMenu = boardScreen;
        gameFrame.setScreen(activeMenu);
    }
    public void resetBoard() {
        service.getPieceService().clearBoard();
        showBoard();
        Turn.setTurn(Turn.LIGHT);
    }
    public void newGame() {
        service.getPieceService().clearBoard();
        showBoard();
        Turn.setTurn(Turn.LIGHT);
    }
    public void showSettings() {
        activeMenu = settingsMenu;
        gameFrame.setScreen(activeMenu);
    }
    public void showAchievements() {
        activeMenu = achievementsMenu;
        gameFrame.setScreen(activeMenu);
    }
    public void exit() {
        log.info("EoS (End of session)");
        Gdx.app.exit();
    }

    public void getActiveMenu(int index) {
        switch(index) {
            case 0 -> newGame();
            case 1 -> showSettings();
            case 2 -> showAchievements();
            case 3 -> exit();
        }
    }

    public List<Moves> newLegalMoves(Turn turn) {
        List<Moves> legalMoves = new ArrayList<>();
        List<Piece> pieces = service.getPieceService().getPieces();
        for(Piece piece : pieces) {
            if(piece.getTurn() != turn) continue;

            for(int row = 0; row < 8; row++) {
                for(int col = 0; col < 8; col++) {
                    if(piece.getRow() == row && piece.getCol() == col) { continue; }
                    if(!BoardService.isWithinBoard(col, row)) { continue; }
                    if(!BoardService.isPathClear(piece, col, row)) { continue; }
                    Piece targetPiece = BoardService.getPieceAt(col, row);
                    if(targetPiece != null && targetPiece.getTurn() == turn) { continue; }

                    MovePiece move = new MovePiece(piece, piece.getCol(), piece.getRow(), col, row,
                        piece.getTurn(), targetPiece);

                    if (canMove(piece, col, row) && !wouldLeaveKingInCheck(piece, col, row)) {
                        legalMoves.add(move);
                    }
                }
            }
        }
        return legalMoves;
    }

    public boolean wouldLeaveKingInCheck(Piece piece, int targetCol, int targetRow) {
        int originalCol = piece.getCol();
        int originalRow = piece.getRow();
        Piece captured = BoardService.getPieceAt(targetCol, targetRow);
        piece.setCol(targetCol);
        piece.setRow(targetRow);
        if(captured != null) {
            service.getPieceService().removePiece(captured);
        }

        Piece king = service.getPieceService().getPieces().stream()
            .filter(p -> p.getTypeID() == TypeID.KING && p.getTurn() == piece.getTurn())
            .findFirst()
            .orElse(null);

        boolean inCheck = false;
        if(king != null) {
            for (Piece enemy : service.getPieceService().getPieces()) {
                if (enemy.getTurn() != piece.getTurn() && canMove(enemy, king.getCol(), king.getRow())) {
                    inCheck = true;
                    break;
                }
            }
        }

        piece.setCol(originalCol);
        piece.setRow(originalRow);
        if(captured != null) {
            service.getPieceService().getPieces().add(captured);
        }

        return inCheck;
    }

    public boolean isKingInCheck(Turn kingColor) {
        Piece king = service.getPieceService().getKing(kingColor);
        if(king == null) { return false; }

        for(Piece p : service.getPieceService().getPieces()) {
            if(p.getTurn() != kingColor) {
                if(canMove(p, king.getCol(), king.getRow())) {
                    checkingPiece = p;
                    return true;
                }
            }
        }
        checkingPiece = null;
        return false;
    }

    public boolean isCheckmate() {
        Turn currentTurn = Turn.getTurn();
        Turn opponent = Turn.DARK;
        Piece king = service.getPieceService().getKing(opponent);
        if(king == null) { return false; }
        if(!isKingInCheck(opponent)) { return false; }

        for(Piece piece : service.getPieceService().getPieces()) {
            if(piece.getTurn() != opponent) { continue; }
            for(int col = 0; col < Board.getSIZE(); col++) {
                for(int row = 0; row < Board.getSIZE(); row++) {
                    if(canMove(piece, col, row) &&
                        !wouldLeaveKingInCheck(piece, col, row)) {
                        return false;
                    }
                }
            }
        }

        log.debug("Checkmate for {}", opponent);
        service.getAudioService().playFX(3);
        return true;
    }

    public boolean canMove(Piece p, int targetCol, int targetRow) {
        switch(p.getTypeID()) {
            case PAWN -> {
                int direction = (p.getTurn() == Turn.LIGHT) ? 1 : -1;
                Piece pieceAtTarget = BoardService.getPieceAt(targetCol, targetRow);

                if(targetCol == p.getCol() && targetRow == p.getRow() + direction) {
                    return pieceAtTarget == null;
                }
                if(targetCol == p.getCol() && targetRow == p.getRow() + 2 * direction
                    && !p.hasMoved() && BoardService.isPathClear(p, targetCol, targetRow)) {
                    return pieceAtTarget == null;
                }
                if(Math.abs(targetCol - p.getCol()) == 1 && targetRow == p.getRow() + direction) {
                    if(pieceAtTarget != null && pieceAtTarget.getTurn() != p.getTurn()) {
                        return true;
                    }
                    return service.getBoardService().canEnPassant(p, targetCol, targetRow,
                        service.getPieceService().getPieces());
                }
            }
            case KNIGHT -> {
                int colDiff = Math.abs(targetCol - p.getCol());
                int rowDiff = Math.abs(targetRow - p.getRow());

                if((colDiff == 2 && rowDiff == 1) || (colDiff == 1 && rowDiff == 2)) {
                    return BoardService.isValidSquare(p, targetCol, targetRow,
                        service.getPieceService().getPieces());
                }
            }
            case BISHOP -> {
                int colDiff = targetCol - p.getCol();
                int rowDiff = targetRow - p.getRow();

                if(Math.abs(colDiff) != Math.abs(rowDiff)) {
                    return false;
                }

                if(!BoardService.isPathClear(p, targetCol, targetRow)) {
                    return false;
                }

                Piece target = null;
                for(Piece piece : service.getPieceService().getPieces()) {
                    if(piece.getCol() == targetCol && piece.getRow() == targetRow) {
                        target = piece;
                        break;
                    }
                }
                return target == null || target.getTurn() != p.getTurn();
            }
            case ROOK -> {
                if(targetCol == p.getCol() || targetRow == p.getRow()) {
                    return BoardService.isValidSquare(p, targetCol, targetRow,
                        service.getPieceService().getPieces())
                        && BoardService.isPathClear(p, targetCol, targetRow);
                }
            }
            case QUEEN -> {
                if(targetCol == p.getCol() || targetRow == p.getRow()) {
                    return BoardService.isValidSquare(p, targetCol, targetRow,
                        service.getPieceService().getPieces())
                        && BoardService.isPathClear(p, targetCol, targetRow);
                }

                int colDiff = targetCol - p.getCol();
                int rowDiff = targetRow - p.getRow();

                if(Math.abs(colDiff) != Math.abs(rowDiff)) {
                    return false;
                }

                if(!BoardService.isPathClear(p, targetCol, targetRow)) {
                    return false;
                }

                Piece target = null;
                for(Piece piece : service.getPieceService().getPieces()) {
                    if(piece.getCol() == targetCol && piece.getRow() == targetRow) {
                        target = piece;
                        break;
                    }
                }
                return target == null || target.getTurn() != p.getTurn();
            }
            case KING -> {
                int colDiff = Math.abs(targetCol - p.getCol());
                int rowDiff = Math.abs(targetRow - p.getRow());

                if((colDiff + rowDiff == 1) || (colDiff * rowDiff == 1)) {
                    return BoardService.isValidSquare(p, targetCol, targetRow,
                        service.getPieceService().getPieces());
                }
            }
        }
        return false;
    }

    public boolean isInputLocked() { return isInputLocked; }
    public void setInputLocked(boolean inputLocked) { this.isInputLocked = inputLocked; }
}

package org.lud.game.service;

import com.badlogic.gdx.Gdx;
import org.lud.engine.core.AudioService;
import org.lud.engine.core.GameFrame;
import org.lud.engine.enums.Turn;
import org.lud.engine.gui.Menu;
import org.lud.game.actors.Piece;
import org.lud.game.menus.AchievementsMenu;
import org.lud.game.menus.MainMenu;
import org.lud.game.menus.SettingsMenu;
import org.lud.game.screens.BoardScreen;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GameService {
    private static final Logger log = LoggerFactory.getLogger(GameService.class);
    private final GameFrame gameFrame;
    private final ServiceFactory service;
    private Turn turn;

    private Menu activeMenu;
    private final Menu mainMenu;
    private final Menu settingsMenu;
    private final Menu achievementsMenu;

    private final BoardScreen boardScreen;

    private boolean isLegal;
    private boolean isFirstBoardEntry = true;

    public GameService(GameFrame gameFrame, ServiceFactory service) {
        this.gameFrame = gameFrame;
        this.service = service;
        AudioService audio = service.getAudioService();
        PieceService piece = service.getPieceService();

        this.mainMenu = new MainMenu(this, audio);
        this.settingsMenu = new SettingsMenu(this, audio);
        this.achievementsMenu = new AchievementsMenu(this, audio);
        this.boardScreen = new BoardScreen(service.getBoardService(), this, piece, audio);

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
        setTurn(Turn.LIGHT);
        showBoard();
    }
    public void newGame() {
        service.getPieceService().clearBoard();
        showBoard();
        setTurn(Turn.LIGHT);
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
                if(targetCol == p.getPreCol() || targetRow == p.getPreRow()) {
                    return BoardService.isValidSquare(p, targetCol, targetRow,
                        service.getPieceService().getPieces())
                        && BoardService.isPathClear(p, targetCol, targetRow);
                }
            }
            case QUEEN -> {
                if(targetCol == p.getPreCol() || targetRow == p.getPreRow()) {
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

    private boolean isCheckmate() {
        return false;
    }
}

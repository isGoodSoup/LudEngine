package org.lud.game.service;

import com.badlogic.gdx.Gdx;
import org.lud.engine.core.AudioService;
import org.lud.engine.core.GameFrame;
import org.lud.engine.enums.Turn;
import org.lud.engine.gui.Menu;
import org.lud.game.entities.Piece;
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

    public void showMainMenu() {
        activeMenu = mainMenu;
        gameFrame.setScreen(activeMenu);
    }
    public void showBoard() {
        activeMenu = boardScreen;
        gameFrame.setScreen(activeMenu);
    }
    public void newGame() {
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

    public Menu getActiveMenu() {
        return activeMenu;
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
                int direction = (p.getColor() == Turn.LIGHT) ? 1 : -1;
                Piece pieceAtTarget = service.getBoardService().getPieceAt(targetCol, targetRow);

                if(targetCol == p.getCol() && targetRow == p.getRow() + direction) {
                    return pieceAtTarget == null;
                }
                if(targetCol == p.getCol() && targetRow == p.getRow() + 2 * direction
                    && !p.hasMoved() && service.getBoardService().isPathClear(p, targetCol, targetRow)) {
                    return pieceAtTarget == null;
                }
                if(Math.abs(targetCol - p.getCol()) == 1 && targetRow == p.getRow() + direction) {
                    if(pieceAtTarget != null && pieceAtTarget.getColor() != p.getColor()) {
                        return true;
                    }
                    return service.getBoardService().canEnPassant(p, targetCol, targetRow,
                        service.getPieceService().getPieces());
                }
            }
            case KNIGHT -> {

            }
            case BISHOP -> {

            }
            case ROOK -> {

            }
            case QUEEN -> {

            }
            case KING -> {

            }
        }
        return false;
    }

    private boolean isCheckmate() {
        return false;
    }
}

package org.lud.game.service;

import com.badlogic.gdx.Gdx;
import org.lud.engine.core.AudioService;
import org.lud.engine.core.GameFrame;
import org.lud.engine.enums.Turn;
import org.lud.engine.gui.Menu;
import org.lud.game.menus.AchievementsMenu;
import org.lud.game.menus.MainMenu;
import org.lud.game.menus.SettingsMenu;
import org.lud.game.screens.BoardScreen;

public class GameService {
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

    private boolean isCheckmate() {
        return false;
    }
}

package org.lud.game.service;

import com.badlogic.gdx.Gdx;
import org.lud.engine.core.GameFrame;
import org.lud.engine.enums.Turn;
import org.lud.engine.gui.Menu;
import org.lud.game.entities.Piece;
import org.lud.game.menus.AchievementsMenu;
import org.lud.game.menus.LangMenu;
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
    private final Menu langMenu;

    private final BoardScreen boardScreen;

    private boolean isLegal;

    public GameService(GameFrame gameFrame, ServiceFactory service) {
        this.gameFrame = gameFrame;
        this.service = service;

        this.mainMenu = new MainMenu(this);
        this.settingsMenu = new SettingsMenu(this);
        this.achievementsMenu = new AchievementsMenu(this);
        this.langMenu = new LangMenu(this);
        this.boardScreen = new BoardScreen(this);

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

    }
    public void showSettings() {
        activeMenu = settingsMenu;
        gameFrame.setScreen(activeMenu);
    }
    public void showAchievements() {
        activeMenu = achievementsMenu;
        gameFrame.setScreen(activeMenu);
    }
    public void showLang() {
        activeMenu = langMenu;
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

    public boolean isCheckmate(Piece piece) {
        return false;
    }
}

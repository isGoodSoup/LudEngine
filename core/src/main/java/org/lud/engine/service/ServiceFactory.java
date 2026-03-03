package org.lud.engine.service;

import com.badlogic.gdx.graphics.OrthographicCamera;
import org.lud.engine.core.AudioService;
import org.lud.engine.core.GameFrame;
import org.lud.engine.data.EventBus;

public class ServiceFactory {
    private final GameFrame gameFrame;
    private final AudioService audioService;
    private final BoardService boardService;
    private final PieceService pieceService;
    private final GameService gameService;
    private final AchievementService achievementService;
    private final AchievementPersistence ap;
    private final OrthographicCamera camera;
    private final EventBus eventBus;

    public ServiceFactory(GameFrame gameFrame, OrthographicCamera camera) {
        this.gameFrame = gameFrame;
        this.camera = camera;
        this.eventBus = new EventBus();
        this.audioService = new AudioService();
        this.boardService = new BoardService(this, camera);
        this.pieceService = new PieceService(this);
        this.gameService = new GameService(gameFrame, this);
        this.ap = new AchievementPersistence();
        this.achievementService = new AchievementService(eventBus, this, ap);
    }

    public EventBus getEventBus() { return eventBus; }
    public AudioService getAudioService() { return audioService; }
    public BoardService getBoardService() { return boardService; }
    public PieceService getPieceService() { return pieceService; }
    public GameService getGameService() {
        return gameService;
    }
    public AchievementService getAchievementService() { return achievementService; }
}

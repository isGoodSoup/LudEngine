package org.lud.engine.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import org.lud.engine.core.AudioService;
import org.lud.game.entities.Board;
import org.lud.game.entities.Piece;
import org.lud.game.service.BoardService;
import org.lud.game.service.GameService;
import org.lud.game.service.PieceService;

public class BoardInput extends InputAdapter {
    private final BoardService board;
    private final PieceService pieceService;
    private final AudioService audioService;
    private final GameService gameService;
    private final Stage stage;

    private final Vector3 mousePos;
    private Piece piece;
    private float offsetX, offsetY;

    private final float startX;
    private final float startY;

    public BoardInput(BoardService board, Stage stage, PieceService pieceService,
                      AudioService audioService, GameService gameService) {
        this.board = board;
        this.stage = stage;
        this.pieceService = pieceService;
        this.audioService = audioService;
        this.gameService = gameService;
        this.startX = board.getBoardStartX();
        this.startY = board.getBoardStartY();
        this.mousePos = new Vector3();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(button != Input.Buttons.LEFT) { return false; }

        mousePos.set(screenX, screenY, 0);
        board.getCamera().unproject(mousePos);

        int col = (int)((mousePos.x - startX) / Board.getSQUARE());
        int row = (int)((mousePos.y - startY) / Board.getSQUARE());
        col = Math.max(0, Math.min(7, col));
        row = Math.max(0, Math.min(7, row));
        row = 7 - row;

        for(Piece p : pieceService.getPieces()) {
            if(p.getColor() == gameService.getTurn() && p.getCol() == col && p.getRow() == row) {
                piece = p;
                offsetX = mousePos.x - p.getX();
                offsetY = mousePos.y - p.getY();
                break;
            }
        }
        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        // TODO
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        // TODO
        return false;
    }
}

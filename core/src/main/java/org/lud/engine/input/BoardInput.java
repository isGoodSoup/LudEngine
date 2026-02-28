package org.lud.engine.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import org.lud.engine.core.AudioService;
import org.lud.game.actors.PieceActor;
import org.lud.game.entities.Board;
import org.lud.game.service.BoardService;

public class BoardInput extends InputAdapter {
    private final BoardService board;
    private final Stage stage;
    private final AudioService audioService;

    private final Vector3 mousePos;
    private PieceActor piece;
    private float offsetX, offsetY;

    public BoardInput(BoardService board, Stage stage, AudioService audioService) {
        this.board = board;
        this.stage = stage;
        this.audioService = audioService;
        this.mousePos = new Vector3();
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if(button != Input.Buttons.LEFT) { return false; }

        mousePos.set(screenX, screenY, 0);
        board.getCamera().unproject(mousePos);

        int col = (int)(mousePos.x / Board.getSIZE());
        int row = (int)(mousePos.y / Board.getSIZE());

        piece = board.getPieceAt(col, row);
        if(piece != null) {
            offsetX = mousePos.x - piece.getX();
            offsetY = mousePos.y - piece.getY();
            return true;
        }

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(piece != null) {
            mousePos.set(screenX, screenY, 0);
            board.getCamera().unproject(mousePos);
            piece.setPosition(mousePos.x - offsetX, mousePos.y - offsetY);
            return true;
        }
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(button != Input.Buttons.LEFT) { return false; }

        if(piece != null) {
            mousePos.set(screenX, screenY, 0);
            board.getCamera().unproject(mousePos);

            int targetCol = (int) (mousePos.x/Board.getSIZE());
            int targetRow = (int) (mousePos.y/Board.getSIZE());
            board.attemptMove(piece, targetCol, targetRow);
            audioService.playFX(0);
            piece = null;
            return true;
        }

        stage.touchUp(screenX, screenY, pointer, button);
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        stage.mouseMoved(screenX, screenY);
        return false;
    }
}

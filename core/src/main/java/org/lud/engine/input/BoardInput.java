package org.lud.engine.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import org.lud.engine.core.AudioService;
import org.lud.game.actors.Piece;
import org.lud.game.service.BoardService;
import org.lud.game.service.GameService;
import org.lud.game.service.PieceService;

public class BoardInput {
    private final PieceService pieceService;
    private final GameService gameService;
    private final BoardService boardService;
    private final AudioService audioService;
    private final float startX, startY, tileSize;
    private Piece piece;
    private float offsetX, offsetY;

    public BoardInput(PieceService pieceService, GameService gameService, BoardService boardService,
                      AudioService audioService, float startX, float startY, float tileSize) {
        this.pieceService = pieceService;
        this.gameService = gameService;
        this.boardService = boardService;
        this.audioService = audioService;
        this.startX = startX;
        this.startY = startY;
        this.tileSize = tileSize;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public void update(Group boardGroup) {
        Vector2 local = boardGroup.screenToLocalCoordinates(new Vector2(Gdx.input.getX(), Gdx.input.getY()));

        if(piece == null && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            for(Piece p : pieceService.getPieces()) {
                float margin = tileSize * 0.25f;
                if(local.x >= p.getX() && local.x <= p.getX() + tileSize - margin &&
                    local.y >= p.getY() && local.y <= p.getY() + tileSize - margin &&
                    p.getTurn() == gameService.getTurn()) {
                    piece = p;
                    offsetX = local.x - p.getX();
                    offsetY = local.y - p.getY();
                    break;
                }
            }
        }

        if(piece != null && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            piece.setPosition(local.x - offsetX, local.y - offsetY);
        }

        if(piece != null && !Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            int col = (int)(piece.getX() / tileSize);
            int row = (int)(piece.getY() / tileSize);
            col = Math.max(0, Math.min(7, col));
            row = Math.max(0, Math.min(7, row));

            boardService.attemptMove(piece, col, row);
            audioService.playFX(0);
            piece = null;
        }
    }
}

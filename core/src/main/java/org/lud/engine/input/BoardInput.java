package org.lud.engine.input;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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

    public void update() {
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

        if(piece == null && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            for(Piece p : pieceService.getPieces()) {
                float px = startX + p.getCol() * tileSize;
                float py = startY + p.getRow() * tileSize;

                float margin = tileSize * 0.25f;
                if(mouseX >= px && mouseX <= px + tileSize - margin &&
                    mouseY >= py && mouseY <= py + tileSize - margin &&
                    p.getTurn() == gameService.getTurn()) {
                    piece = p;
                    offsetX = mouseX - px;
                    offsetY = mouseY - py;
                    break;
                }
            }
        }

        if(piece != null && Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            piece.setX((int)(mouseX - offsetX));
            piece.setY((int)(mouseY - offsetY));
        }

        if(piece != null && !Gdx.input.isButtonPressed(Input.Buttons.LEFT)) {
            int col = (int)((mouseX - startX)/tileSize);
            int row = (int)((mouseY - startY)/tileSize);

            col = Math.max(0, Math.min(7, col));
            row = Math.max(0, Math.min(7, row));

            boardService.attemptMove(piece, col, row);
            audioService.playFX(0);
            piece = null;
        }
    }
}

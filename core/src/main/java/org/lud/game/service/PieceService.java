package org.lud.game.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import org.lud.engine.enums.Theme;
import org.lud.engine.enums.Turn;
import org.lud.engine.gui.Colors;
import org.lud.game.actors.PieceActor;
import org.lud.game.data.Piece;
import org.lud.game.entities.Board;
import org.lud.game.enums.TypeID;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class PieceService {
    private final ServiceFactory service;
    private final List<Piece> pieces;

    public PieceService(ServiceFactory service) {
        this.pieces = new ArrayList<>();
        this.service = service;
        loadSprites();
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public void setPieces() {
        pieces.clear();
        for(int col = 0; col < 8; col++) {
            addPiece(new PieceActor(new Piece(TypeID.PAWN, col, 6, Turn.DARK, true), col, 6));
            addPiece(new PieceActor(new Piece(TypeID.PAWN, col, 1, Turn.LIGHT, true), col, 1));
        }

        addPiece(new PieceActor(new Piece(TypeID.ROOK, 0, 7, Turn.DARK, true), 0, 7));
        addPiece(new PieceActor(new Piece(TypeID.ROOK, 7, 7, Turn.DARK, true), 7, 7));
        addPiece(new PieceActor(new Piece(TypeID.KNIGHT, 1, 7, Turn.DARK, true), 1, 7));
        addPiece(new PieceActor(new Piece(TypeID.KNIGHT, 6, 7, Turn.DARK, true), 6, 7));
        addPiece(new PieceActor(new Piece(TypeID.BISHOP, 2, 7, Turn.DARK, true), 2, 7));
        addPiece(new PieceActor(new Piece(TypeID.BISHOP, 5, 7, Turn.DARK, true), 5, 7));
        addPiece(new PieceActor(new Piece(TypeID.QUEEN, 3, 7, Turn.DARK, true), 3, 7));
        addPiece(new PieceActor(new Piece(TypeID.KING, 4, 7, Turn.DARK, true), 4, 7));

        addPiece(new PieceActor(new Piece(TypeID.ROOK, 0, 0, Turn.LIGHT, true), 0, 0));
        addPiece(new PieceActor(new Piece(TypeID.ROOK, 7, 0, Turn.LIGHT, true), 7, 0));
        addPiece(new PieceActor(new Piece(TypeID.KNIGHT, 1, 0, Turn.LIGHT, true), 1, 0));
        addPiece(new PieceActor(new Piece(TypeID.KNIGHT, 6, 0, Turn.LIGHT, true), 6, 0));
        addPiece(new PieceActor(new Piece(TypeID.BISHOP, 2, 0, Turn.LIGHT, true), 2, 0));
        addPiece(new PieceActor(new Piece(TypeID.BISHOP, 5, 0, Turn.LIGHT, true), 5, 0));
        addPiece(new PieceActor(new Piece(TypeID.QUEEN, 3, 0, Turn.LIGHT, true), 3, 0));
        addPiece(new PieceActor(new Piece(TypeID.KING, 4, 0, Turn.LIGHT, true), 4, 0));
    }

    public void loadSprites() {
        for(Piece p : pieces) {
            getSprite(p);
        }
    }

    public Texture getSprite(Piece p) {
        String defaultPath = "pieces/";
        String name = p.type().name().toLowerCase();
        Theme theme = Colors.getTheme();
        String suffix = theme.getColor(p.color());
        defaultPath += name + "/";
        defaultPath += name + "_" + suffix + ".png";
        return new Texture(Gdx.files.internal(defaultPath));
    }

    public void addPiece(PieceActor p) {
        pieces.add(p.getPiece());
        service.getBoardService().addPiece(p);
    }

    public static void updatePos(PieceActor pa, boolean isOldPos) {
        if(isOldPos) {
            pa.setPreCol(pa.getPiece().col());
            pa.setPreRow(pa.getPiece().row());
        }

        if(pa.getPiece().type() == TypeID.PAWN) {
            int rowDiff = Math.abs(pa.getPiece().row() - pa.getPreRow());
            if (rowDiff == 2) {
                pa.setTwoStepsAhead(true);
            }
        }

        int square = Board.getSIZE();
        pa.setX(pa.getPiece().col() * square);
        pa.setY(pa.getPiece().row() * square);
        pa.setHasMoved(true);
    }

    public void removePiece(PieceActor p) {
        pieces.remove(p.getPiece());
        service.getBoardService().removePiece(p);
    }
}

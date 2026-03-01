package org.lud.game.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import org.lud.engine.enums.Theme;
import org.lud.engine.enums.Turn;
import org.lud.engine.gui.Colors;
import org.lud.game.entities.Board;
import org.lud.game.actors.Piece;
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
    }

    public List<Piece> getPieces() {
        return pieces;
    }

    public void setPieces() {
        pieces.clear();
        for (int col = 0; col < 8; col++) {
            addPiece(new Piece(TypeID.PAWN, Turn.DARK, col, 6));
            addPiece(new Piece(TypeID.PAWN, Turn.LIGHT, col, 1));
        }

        addPiece(new Piece(TypeID.ROOK, Turn.DARK, 0, 7));
        addPiece(new Piece(TypeID.ROOK, Turn.DARK, 7, 7));
        addPiece(new Piece(TypeID.ROOK, Turn.LIGHT, 0, 0));
        addPiece(new Piece(TypeID.ROOK, Turn.LIGHT, 7, 0));

        addPiece(new Piece(TypeID.KNIGHT, Turn.DARK, 1, 7));
        addPiece(new Piece(TypeID.KNIGHT, Turn.DARK, 6, 7));
        addPiece(new Piece(TypeID.KNIGHT, Turn.LIGHT, 1, 0));
        addPiece(new Piece(TypeID.KNIGHT, Turn.LIGHT, 6, 0));

        addPiece(new Piece(TypeID.BISHOP, Turn.DARK, 2, 7));
        addPiece(new Piece(TypeID.BISHOP, Turn.DARK, 5, 7));
        addPiece(new Piece(TypeID.BISHOP, Turn.LIGHT, 2, 0));
        addPiece(new Piece(TypeID.BISHOP, Turn.LIGHT, 5, 0));

        addPiece(new Piece(TypeID.QUEEN, Turn.DARK, 3, 7));
        addPiece(new Piece(TypeID.QUEEN, Turn.LIGHT, 3, 0));

        addPiece(new Piece(TypeID.KING, Turn.DARK, 4, 7));
        addPiece(new Piece(TypeID.KING, Turn.LIGHT, 4, 0));
    }

    public Texture getSprite(Piece p) {
        String path = "pieces/";
        String name = p.getTypeID().name().toLowerCase();
        Theme theme = Colors.getTheme();
        String suffix = theme.getColor(p.getTurn());
        path += name + "/" + name + "_" + suffix + ".png";
        return new Texture(Gdx.files.internal(path));
    }

    public void addPiece(Piece p) {
        pieces.add(p);
        service.getBoardService().addPiece(p);
        getSprite(p);
    }

    public void replacePiece(Piece pold, Piece pnew) {
        int index = pieces.indexOf(pold);
        if (index != -1) {
            pieces.set(index, pnew);
        }
        service.getBoardService().removePiece(pold);
        service.getBoardService().addPiece(pnew);
        getSprite(pnew);
    }

    public void removePiece(Piece p) {
        pieces.remove(p);
        service.getBoardService().removePiece(p);
    }

    public static void updatePos(Piece p, boolean isOldPos) {
        p.setX(p.getCol() * Board.getSQUARE());
        p.setY(p.getRow() * Board.getSQUARE());
        p.setHasMoved(true);
    }

    public static float[] toPixels(int col, int row) {
        float square = Board.getSQUARE();
        return new float[]{col * square, row * square};
    }
}

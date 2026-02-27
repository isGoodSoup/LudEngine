package org.lud.game.service;

import org.lud.game.entities.Piece;

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

    public void addPiece(Piece p) {
        pieces.add(p);
        service.getBoardService().addPiece(p);
    }

    public void removePiece(Piece p) {
        pieces.remove(p);
        service.getBoardService().removePiece(p);
    }
}

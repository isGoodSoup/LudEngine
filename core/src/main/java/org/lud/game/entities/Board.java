package org.lud.game.entities;

import org.lud.game.actors.PieceActor;

public class Board {
    private static final int SIZE = 8;
    private final PieceActor[][] pieces;

    public Board() {
        this.pieces = new PieceActor[SIZE][SIZE];
    }

    public static int getSIZE() {
        return SIZE;
    }

    public PieceActor[][] getPieces() {
        return pieces;
    }
}

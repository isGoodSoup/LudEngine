package org.lud.game.entities;

import org.lud.game.data.Piece;

public class Board {
    private static final int SIZE = 8;
    private final Piece[][] pieces;

    public Board() {
        this.pieces = new Piece[SIZE][SIZE];
    }

    public static int getSIZE() {
        return SIZE;
    }

    public Piece[][] getPieces() {
        return pieces;
    }
}

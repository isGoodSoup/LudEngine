package org.lud.game.entities;

public class Board {
    private static final int SIZE = 8;
    private Piece[][] pieces;

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

package org.lud.game.entities;

public class Board {
    private static final int SIZE = 8;
    private static final int SQUARE = SIZE * 8;
    private final Piece[][] pieces;

    public Board() {
        this.pieces = new Piece[SIZE][SIZE];
    }

    public static int getSQUARE() {
        return SQUARE;
    }

    public static int getSIZE() {
        return SIZE;
    }

    public Piece[][] getPieces() {
        return pieces;
    }

    public String getSquareNameAt(int col, int row) {
        int rankIndex = SIZE - 1 - row;
        char fileChar = (char) ('a' + col);
        return "" + fileChar + (rankIndex + 1);
    }
}

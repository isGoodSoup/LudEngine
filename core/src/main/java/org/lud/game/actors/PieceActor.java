package org.lud.game.actors;

import org.lud.game.data.Piece;

public class PieceActor {
    private final Piece piece;
    private float x, y;
    private int preCol, preRow;

    private boolean hasMoved;
    private boolean isTwoStepsAhead;
    private boolean isEnPassant;

    public PieceActor(Piece piece, float x, float y) {
        this.piece = piece;
        this.x = x;
        this.y = y;
    }

    public PieceActor(Piece piece) {
        this.piece = piece;
        this.preCol = piece.col();
        this.preRow = piece.row();
    }

    public Piece getPiece() { return piece; }
    public float getX() { return x; }
    public float getY() { return y; }
    public void setX(float x) {
        this.x = x;
    }
    public void setY(float y) {
        this.y = y;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public int getPreCol() {
        return preCol;
    }
    public int getPreRow() {
        return preRow;
    }
    public void setPreCol(int preCol) {
        this.preCol = preCol;
    }
    public void setPreRow(int preRow) {
        this.preRow = preRow;
    }

    public boolean hasMoved() {
        return hasMoved;
    }
    public void setHasMoved(boolean hasMoved) {
        this.hasMoved = hasMoved;
    }

    public boolean isTwoStepsAhead() {
        return isTwoStepsAhead;
    }
    public void setTwoStepsAhead(boolean twoStepsAhead) {
        isTwoStepsAhead = twoStepsAhead;
    }

    public boolean isEnPassant() {
        return isEnPassant;
    }
    public void setEnPassant(boolean enPassant) {
        isEnPassant = enPassant;
    }
}

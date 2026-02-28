package org.lud.game.entities;

import org.lud.engine.enums.Turn;
import org.lud.game.enums.TypeID;

public class Piece {
    private Piece other;
    private TypeID typeID;
    private Turn color;
    private int x, y;
    private int preCol, preRow;
    private int col, row;

    private boolean hasMoved;
    private boolean isTwoStepsAhead;

    public Piece(TypeID typeID, Turn color, int col, int row) {
        this.typeID = typeID;
        this.color = color;
        this.col = col;
        this.row = row;
        this.preCol = col;
        this.preRow = row;
        this.x = col * Board.getSQUARE();
        this.y = row * Board.getSQUARE();
    }

    public TypeID getTypeID() { return typeID; }
    public void setTypeID(TypeID typeID) { this.typeID = typeID; }

    public Turn getColor() { return color; }
    public void setColor(Turn color) { this.color = color; }

    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    public int getCol() { return col; }
    public void setCol(int col) {
        this.col = col;
        this.x = col * Board.getSQUARE();
    }

    public int getRow() { return row; }
    public void setRow(int row) {
        this.row = row;
        this.y = row * Board.getSQUARE();
    }

    public int getPreCol() { return preCol; }
    public void setPreCol(int preCol) { this.preCol = preCol; }

    public int getPreRow() { return preRow; }
    public void setPreRow(int preRow) { this.preRow = preRow; }

    public boolean hasMoved() { return hasMoved; }
    public void setHasMoved(boolean hasMoved) { this.hasMoved = hasMoved; }

    public Piece getOther() { return other; }
    public void setOther(Piece other) { this.other = other; }

    public boolean isTwoStepsAhead() { return isTwoStepsAhead; }
    public void setTwoStepsAhead(boolean twoStepsAhead) { isTwoStepsAhead = twoStepsAhead; }
}

package org.lud.game.data;

import org.lud.engine.enums.Turn;
import org.lud.game.enums.TypeID;

public record Piece(long id, TypeID type, int col, int row, int preCol, int preRow, Turn color) {}

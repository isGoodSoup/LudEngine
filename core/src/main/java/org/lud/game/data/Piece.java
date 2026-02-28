package org.lud.game.data;

import org.lud.engine.enums.Turn;
import org.lud.game.enums.TypeID;

public record Piece(TypeID type, int col, int row, Turn color, boolean canBeHovered) {}

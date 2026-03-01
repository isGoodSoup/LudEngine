package org.lud.game.moves;

import org.lud.engine.enums.Turn;
import org.lud.game.actors.Piece;

public record Move(Piece piece, int fromCol, int fromRow, int targetCol, int targetRow,
                   Turn color, Piece captured){}

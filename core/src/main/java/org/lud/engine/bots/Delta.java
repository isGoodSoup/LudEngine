package org.lud.engine.bots;

import org.lud.engine.enums.Turn;
import org.lud.engine.interfaces.AI;
import org.lud.engine.interfaces.Moves;

import java.util.List;

public class Delta implements AI {
    @Override
    public Moves chooseMove(List<Moves> legalMoves) {
        return null;
    }

    @Override
    public void switchTurns() {
        Turn.nextTurn();
    }
}

package org.lud.engine.bots;

import org.lud.engine.enums.Turn;
import org.lud.engine.interfaces.AI;
import org.lud.engine.interfaces.Moves;
import org.lud.game.service.GameService;

import java.util.List;

public class Alpha implements AI {
    @Override
    public Moves chooseMove(List<Moves> legalMoves) {
        if(legalMoves.isEmpty()) { return null; }
        int index = (int) (Math.random() * legalMoves.size());
        return legalMoves.get(index);
    }

    @Override
    public void switchTurns(GameService gameService) {
        gameService.setTurn(Turn.LIGHT);
    }
}

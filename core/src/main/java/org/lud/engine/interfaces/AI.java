package org.lud.engine.interfaces;

import org.lud.game.service.GameService;

import java.util.List;

public interface AI {
    Moves chooseMove(List<Moves> legalMoves);
    void switchTurns(GameService gameService);
}

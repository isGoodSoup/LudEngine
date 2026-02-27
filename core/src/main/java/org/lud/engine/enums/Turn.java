package org.lud.engine.enums;

import org.lud.game.service.GameService;

public enum Turn {
    LIGHT, DARK;

    public static Turn nextTurn(GameService gameService) {
        return gameService.getTurn() == Turn.LIGHT ? Turn.DARK : Turn.LIGHT;
    }
}

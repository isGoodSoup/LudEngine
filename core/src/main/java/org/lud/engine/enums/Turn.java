package org.lud.engine.enums;

public enum Turn {
    LIGHT, DARK;

    private static Turn currentTurn = LIGHT;

    public static Turn nextTurn() {
        currentTurn = (currentTurn == LIGHT) ? DARK : LIGHT;
        return currentTurn;
    }

    public static Turn getTurn() {
        return currentTurn;
    }

    public static void setTurn(Turn turn) {
        currentTurn = turn;
    }
}

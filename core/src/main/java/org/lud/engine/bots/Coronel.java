package org.lud.engine.bots;

import org.lud.engine.enums.Turn;
import org.lud.engine.interfaces.AI;
import org.lud.engine.interfaces.Moves;

import java.util.*;

public class Coronel implements AI {
    private final Set<String> history = new HashSet<>();

    @Override
    public Moves chooseMove(List<Moves> legalMoves) {
        if(legalMoves.isEmpty()) { return null; }

        Moves bestMove = null;
        int bestScore = Integer.MIN_VALUE;

        for(Moves move : legalMoves) {
            int score = move.getScoreImpact();
            score += move.isCapture();
            score += isUniqueBonus(move);

            if(score > bestScore) {
                bestScore = score;
                bestMove = move;
            }
        }

        if(bestMove != null) {
            history.add(moveKey(bestMove));
        }

        return bestMove;
    }

    @Override
    public void switchTurns() {
        Turn.nextTurn();
    }

    private int isUniqueBonus(Moves move) {
        return history.contains(moveKey(move)) ? 0 : 10;
    }

    private String moveKey(Moves move) {
        return move.getScoreImpact() + ":" + move.isCapture();
    }
}

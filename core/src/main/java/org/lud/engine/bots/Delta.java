package org.lud.engine.bots;

import org.lud.engine.enums.Turn;
import org.lud.engine.interfaces.AI;
import org.lud.engine.interfaces.Moves;

import java.util.*;

public class Delta implements AI {
    private final Set<String> history = new HashSet<>();
    private final Random random = new Random();

    @Override
    public Moves chooseMove(List<Moves> legalMoves) {
        if(legalMoves.isEmpty()) return null;
        Moves bestMove = null;
        int bestScore = Integer.MIN_VALUE;
        List<Moves> candidateMoves = new ArrayList<>();

        for(Moves move : legalMoves) {
            int score = move.getScoreImpact();
            score += move.isCapture() * 2;
            score += isUniqueBonus(move);
            score -= estimateOpponentResponse(move, legalMoves);

            if(score > bestScore) {
                bestScore = score;
                candidateMoves.clear();
                candidateMoves.add(move);
            } else if(score == bestScore) {
                candidateMoves.add(move);
            }
        }

        bestMove = candidateMoves.get(random.nextInt(candidateMoves.size()));
        history.add(moveKey(bestMove));

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

    private int estimateOpponentResponse(Moves move, List<Moves> legalMoves) {
        int maxOpponentScore = 0;
        for(Moves opponentMove : legalMoves) {
            if(opponentMove == move) { continue; }
            int score = opponentMove.getScoreImpact() + opponentMove.isCapture();
            if(score > maxOpponentScore) maxOpponentScore = score;
        }
        return maxOpponentScore;
    }
}

package org.lud.engine.interfaces;

public interface Moves {
    void apply();
    void undo();
    int getScoreImpact();
}

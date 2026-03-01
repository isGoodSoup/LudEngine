package org.lud.engine.enums;

import org.lud.engine.bots.*;
import org.lud.engine.gui.Localization;
import org.lud.engine.interfaces.AI;

public enum Difficulty {
    ALPHA("difficulty.alpha"),
    BETA("difficulty.beta"),
    CORONEL("difficulty.coronel"),
    DELTA("difficulty.delta"),
    SIGMA("difficulty.sigma");

    private final String labelKey;

    Difficulty(String labelKey) {
        this.labelKey = labelKey;
    }

    public String getLabelKey() {
        return Localization.lang.t(this.labelKey);
    }

    public static AI setDifficulty(Difficulty difficulty) {
        return switch(difficulty) {
            case ALPHA -> new Alpha();
            case BETA -> new Beta();
            case CORONEL -> new Coronel();
            case DELTA -> new Delta();
            case SIGMA -> new Sigma();
        };
    }
}

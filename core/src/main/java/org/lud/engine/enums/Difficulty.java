package org.lud.engine.enums;

import org.lud.engine.bots.*;
import org.lud.engine.gui.Localization;
import org.lud.engine.interfaces.AI;

public enum Difficulty {
    ALPHA("difficulty.alpha", new Alpha()),
    BETA("difficulty.beta", new Beta()),
    CORONEL("difficulty.coronel", new Coronel()),
    DELTA("difficulty.delta", new Delta()),
    SIGMA("difficulty.sigma", new Sigma());

    private final AI ai;
    private final String labelKey;

    Difficulty(String labelKey, AI ai) {
        this.ai = ai;
        this.labelKey = labelKey;
    }

    public String getLabelKey() {
        return Localization.lang.t(this.labelKey);
    }

    public static AI setDiff(Difficulty difficulty) {
        return difficulty.ai;
    }
}

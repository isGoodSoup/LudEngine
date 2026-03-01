package org.lud.engine.enums;

import org.lud.engine.gui.Localization;

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
}

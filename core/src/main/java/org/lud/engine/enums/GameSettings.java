package org.lud.engine.enums;

public enum GameSettings {
    MUSIC_ENABLED("settings.music", true),
    SFX_ENABLED("settings.sfx", true),
    FULLSCREEN("settings.fullscreen", false),
    SHOW_FPS("settings.show_fps", false),
    AUTO_SAVE("settings.autosave", true);

    private final String labelKey;
    private boolean valueKey;

    GameSettings(String labelKey, boolean valueKey) {
        this.labelKey = labelKey;
        this.valueKey = valueKey;
    }

    public void toggle() {
        this.valueKey ^= true;
    }

    public boolean get() {
        return valueKey;
    }

    public String getLabelKey() {
        return labelKey;
    }
}

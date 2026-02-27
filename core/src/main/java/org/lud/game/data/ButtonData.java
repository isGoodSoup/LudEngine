package org.lud.game.data;

import org.lud.game.enums.UIButton;

public record ButtonData(UIButton type, Runnable action, Runnable soundPath) {}

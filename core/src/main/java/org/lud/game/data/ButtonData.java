package org.lud.game.data;

import com.badlogic.gdx.audio.Sound;
import org.lud.game.enums.UIButton;

public record ButtonData(UIButton type, Runnable action, Sound soundPath) {}

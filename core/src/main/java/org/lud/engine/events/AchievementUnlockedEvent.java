package org.lud.engine.events;

import org.lud.engine.interfaces.Achieveable;
import org.lud.engine.interfaces.GameEvent;

public record AchievementUnlockedEvent(Achieveable id) implements GameEvent {}

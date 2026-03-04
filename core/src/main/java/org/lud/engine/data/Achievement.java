package org.lud.engine.data;

import org.lud.engine.interfaces.Achieveable;

public record Achievement(Achieveable id, boolean unlocked) {
    public Achievement(Achieveable id) {
        this(id, false);
    }

    public Achievement unlock() {
        return new Achievement(id, true);
    }
}

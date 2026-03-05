package org.lud.engine.gui;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class WindowActor extends Actor {
    private final NinePatch patch;

    public WindowActor(NinePatch patch, float x, float y, float width, float height) {
        this.patch = patch;
        setBounds(x, y, width, height);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        patch.draw(batch, getX(), getY(), getWidth(), getHeight());
    }
}

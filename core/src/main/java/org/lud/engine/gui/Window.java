package org.lud.engine.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Window extends Actor {
    private float timer = 0f;
    private final float delay = 0.25f;
    private final float padding = 8f;
    private final Texture tex;
    private final int cs;
    private final float scale;

    public Window(float x, float y, float scale, int cs, Texture tex) {
        this.scale = scale;
        this.cs = cs;
        this.tex = tex;
        setPosition(x, y);
    }
}

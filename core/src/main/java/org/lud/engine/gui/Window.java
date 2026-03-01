package org.lud.engine.gui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import org.lud.engine.interfaces.Clickable;

public class Window extends Actor implements Clickable {
    private final Slicer background;
    private final float scale;
    private Runnable action;
    private Runnable sound;

    private boolean isHovered = false;

    public Window(float x, float y, float width, float height, Slicer background,
                  float scale, Runnable action, Runnable sound) {

        this.background = background;
        this.scale = scale;
        this.action = action;
        this.sound = sound;

        setBounds(x, y, width, height);
        addInteraction();
    }

    private void addInteraction() {
        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(button == Input.Buttons.LEFT) {
                    onClick();
                    return true;
                }
                return false;
            }

            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                isHovered = true;
            }

            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                isHovered = false;
            }
        });
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        background.draw(batch, getX(), getY(), getWidth(), getHeight(), scale, isHovered);
    }

    @Override
    public void onClick() {
        if(action != null) { action.run(); }
        if(sound != null) { sound.run(); }
    }
}

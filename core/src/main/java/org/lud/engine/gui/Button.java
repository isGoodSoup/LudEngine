package org.lud.engine.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.lud.engine.enums.LastInput;
import org.lud.engine.input.Coordinator;
import org.lud.engine.interfaces.Clickable;

@SuppressWarnings("ALL")
public class Button extends Actor implements Clickable {
    private final Runnable action;
    private Texture texture;
    private Texture icon;
    private Texture iconHighlighted;
    private Texture frame;
    private Runnable sound;
    private boolean isHovered = false;

    public Button(float x, float y, float width, float height,
                  Texture texture, Texture icon, Texture frame,
                  Texture iconHighlighted, Runnable sound, Runnable action) {
        this.texture = texture;
        this.icon = icon;
        this.frame = frame;
        this.iconHighlighted = iconHighlighted;
        this.action = action;

        if(sound != null) {
            this.sound = sound;
        }

        setSize(width, height);
        setPosition(x, y);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(texture, getX(), getY(), getWidth(), getHeight());
        batch.draw(icon, getX(), getY(), getWidth(), getHeight());
        if(isHovered && frame != null) {
            batch.draw(iconHighlighted, getX(), getY(), getWidth(), getHeight());
            batch.draw(frame, getX(), getY(), getWidth(), getHeight());
        }
    }

    @Override
    public void onClick() {
        action.run();
        if(sound != null) {
            sound.run();
        }
    }

    public void update() {
        boolean currentlyHovered = false;

        if(Gdx.input.isTouched() || Gdx.input.getX() >= 0) {
            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();

            float localX = mouseX;
            float localY = mouseY;
            if(getParent() != null) {
                Vector2 v = getParent().screenToLocalCoordinates(new Vector2(mouseX, mouseY));
                localX = v.x;
                localY = v.y;
            }

            currentlyHovered = localX >= getX() && localX <= getX() + getWidth()
                && localY >= getY() && localY <= getY() + getHeight();

            if(currentlyHovered) {
                Coordinator.setLastInput(LastInput.MOUSE);
            }
        }

        isHovered = currentlyHovered ||
            (Coordinator.getLastInput() == LastInput.KEYBOARD && isHovered);
        if(isHovered && Gdx.input.isButtonJustPressed(Buttons.LEFT)) {
            onClick();
        }
    }

    public Runnable getAction() {
        return action;
    }

    public boolean isHovered() {
        return isHovered;
    }
    public void setHovered(boolean hovered) {
        isHovered = hovered;
    }


    public void dispose() {
        texture.dispose();
    }
}

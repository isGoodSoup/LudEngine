package org.lud.engine.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.lud.engine.interfaces.Clickable;

@SuppressWarnings("ALL")
public class Button implements Clickable {
    private float x;
    private float y;
    private float width;
    private float height;
    private final Runnable action;
    private Texture texture;
    private Texture icon;
    private Texture iconHighlighted;
    private Texture frame;
    private Sound sound;
    private boolean isHovered = false;

    public Button(float x, float y, float width, float height,
                  Texture texture, Texture icon, Texture frame,
                  Texture iconHighlighted, String sound, Runnable action) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.texture = texture;
        this.icon = icon;
        this.frame = frame;
        this.iconHighlighted = iconHighlighted;
        this.action = action;

        if(sound != null && !sound.isEmpty()) {
            this.sound = Gdx.audio.newSound(Gdx.files.internal(sound));
        }
    }

    public float getX() { return x; }
    public void setX(float x) { this.x = x; }

    public float getY() { return y; }
    public void setY(float y) { this.y = y; }

    public float getWidth() { return width; }
    public void setWidth(float width) { this.width = width; }

    public float getHeight() { return height; }
    public void setHeight(float height) { this.height = height; }

    @Override
    public void onClick() {
        action.run();
        if(sound != null) {
            sound.play();
        }
    }

    public void render(SpriteBatch batch) {
        if(isHovered && frame != null) {
            batch.draw(texture, x, y, width, height);
            batch.draw(iconHighlighted, x, y, width, height);
            batch.draw(frame, x, y, width, height);
        } else {
            batch.draw(texture, x, y, width, height);
            batch.draw(icon, x, y, width, height);
        }
    }

    public void update() {
        float mouseX = Gdx.input.getX();
        float mouseY = Gdx.graphics.getHeight() - Gdx.input.getY();
        isHovered = mouseX >= x && mouseX <= x + width &&
            mouseY >= y && mouseY <= y + height;

        if(isHovered && Gdx.input.justTouched()
            && Gdx.input.isButtonPressed(Buttons.LEFT)) {
            onClick();
        }
    }

    public boolean isHovered() {
        return isHovered;
    }

    public void dispose() {
        texture.dispose();
        if(sound != null) { sound.dispose(); }
    }
}

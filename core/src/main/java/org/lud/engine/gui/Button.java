package org.lud.engine.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import org.lud.engine.interfaces.Clickable;

@SuppressWarnings("ALL")
public class Button implements Clickable {
    private float x;
    private float y;
    private float width;
    private float height;
    private final Runnable action;
    private Texture texture;
    private Texture frame;
    private boolean isHovered = false;

    public Button(float x, float y, float width, float height,
                  Texture texture, Texture frame, Runnable action) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.texture = texture;
        this.frame = frame;
        this.action = action;
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
    }

    public void render(SpriteBatch batch) {
        if(isHovered && frame != null) {
            batch.draw(frame, x, y, width, height);
        } else {
            batch.draw(texture, x, y, width, height);
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
    }
}

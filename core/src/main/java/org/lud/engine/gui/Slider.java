package org.lud.engine.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import org.lud.engine.interfaces.Clickable;

public class Slider extends Actor implements Clickable {
    private Texture head;
    private Texture body;
    private Runnable action;

    private float headX;
    private float minX, maxX;
    private float width;
    private float height;
    private float animationSpeed = 300f;
    private float targetHeadX;
    private float value;
    private boolean isAnimating = true;

    public Slider(Runnable action, Texture head, Texture body,
                  float x, float y, float width) {
        this.action = action;
        this.head = head;
        this.body = body;
        this.minX = x;
        this.maxX = x + width - head.getWidth();
        this.headX = minX - head.getWidth();
        this.targetHeadX = minX;
        this.value = (headX - minX)/(maxX - minX);
        setPosition(x, y);
        setSize(body.getWidth(), head.getHeight());
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if(isAnimating) {
            headX += animationSpeed * delta;
            if(headX >= targetHeadX) {
                headX = targetHeadX;
                isAnimating = false;
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
    }

    @Override
    public void onClick() {
        if(action != null) {
            action.run();
        }
    }

    public void moveHeadTo(float mouseX) {
        headX = Math.max(minX, Math.min(maxX, mouseX - head.getWidth()/2f));
    }
}

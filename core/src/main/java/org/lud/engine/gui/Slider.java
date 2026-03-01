package org.lud.engine.gui;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import org.lud.engine.interfaces.Clickable;

import java.util.NavigableMap;
import java.util.TreeMap;

public class Slider extends Actor implements Clickable {
    private NavigableMap<Float, Runnable> valueActions;
    private Runnable action;
    private final Texture head;
    private final Texture body;
    private final Texture frame;

    private float headX;
    private float minX, maxX;
    private float width, height;

    private float animationSpeed = 400f;
    private boolean isAnimating = true;
    private boolean dragging = false;
    private float targetX;

    private boolean isHovered = false;
    private float value = 0f;

    public Slider(Runnable action, float x, float y) {
        this.valueActions = new TreeMap<>();
        this.head = new Texture("slider/slider_head.png");
        this.body = new Texture("slider/slider_body.png");
        this.frame = new Texture("slider/slider_frame.png");

        this.width = body.getWidth();
        this.minX = x;
        this.maxX = x + width - head.getWidth();
        this.height = head.getHeight();

        this.headX = minX - head.getWidth();
        this.targetX = minX;

        setPosition(x, y);
        setSize(width, height);

        addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                if(button == Input.Buttons.LEFT) {
                    dragging = true;
                    onClick();
                    moveHeadTo(event.getStageX());
                    return true;
                }
                return false;
            }

            @Override
            public void touchDragged(InputEvent event, float x, float y, int pointer) {
                if(dragging) {
                    moveHeadTo(event.getStageX());
                }
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                dragging = false;
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

    public void addValueAction(float threshold, Runnable action) {
        valueActions.put(threshold, action);
    }

    public void runForValue() {
        if(valueActions.isEmpty()) { return; }
        Float key = valueActions.floorKey(value);
        if(key != null) {
            Runnable r = valueActions.get(key);
            if(r != null) { r.run(); }
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        if(isAnimating) {
            float dir = Math.signum(targetX - headX);
            headX += animationSpeed * delta * dir;
            if ((dir > 0 && headX >= targetX) || (dir < 0 && headX <= targetX)) {
                headX = targetX;
                isAnimating = false;
            }
        }

        value = (headX - minX) / (maxX - minX);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        batch.draw(body, getX(), getY(), width, head.getHeight());
        batch.draw(head, headX, getY());
        if(isHovered) {
            batch.draw(frame, headX, getY());
        }
    }

    @Override
    public void onClick() {
        runForValue();
    }

    private void moveHeadTo(float mouseX) {
        headX = Math.max(minX, Math.min(maxX, mouseX - head.getWidth()/2f));
        value = (headX - minX) / (maxX - minX);
        runForValue();
    }

    public static float getBodyWidth() {
        return new Texture("slider/slider_body.png").getWidth();
    }

    public float getValue() {
        return value;
    }

    public void setValue(float normalized) {
        normalized = Math.max(0f, Math.min(1f, normalized));
        headX = minX + normalized * (maxX - minX);
        targetX = headX;
        isAnimating = false;
    }

    public boolean isHovered() {
        return isHovered;
    }
}

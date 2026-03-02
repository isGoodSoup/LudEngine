package org.lud.engine.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

public class Toast extends Actor {
    private final BitmapFont font;
    private final String text;
    private final String description;
    private final Texture tex;

    private float x, y;
    private float width, height;
    private final float padding = 16f;
    private final float duration = 3f;
    private float elapsedTime;
    private final int cell = 4;
    private final float scale = 4f;

    private boolean isFinished;

    public Toast(String description, String text, BitmapFont font, float targetX, float targetY) {
        this.description = description;
        this.text = text;
        this.font = font;
        this.tex = new Texture("tooltip.png");
        this.width = 200;
        this.height = 50;
        setPosition(targetX, -height);
        setSize(width, height);

        float slideTime = 0.5f;
        float fadeTime = 1f;

        addAction(
            Actions.sequence(
                Actions.moveTo(targetX, targetY, slideTime),
                Actions.delay(duration),
                Actions.fadeOut(fadeTime),
                Actions.run(() -> isFinished = true)
            )
        );
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        float l = getX();
        float b = getY();
        float r = l + width;
        float t = b + height;

        TextureRegion tl = new TextureRegion(tex, 0, tex.getHeight() - cell, cell, cell);
        TextureRegion tr = new TextureRegion(tex, tex.getWidth() - cell, tex.getHeight() - cell, cell, cell);
        TextureRegion bl = new TextureRegion(tex, 0, 0, cell, cell);
        TextureRegion br = new TextureRegion(tex, tex.getWidth() - cell, 0, cell, cell);

        TextureRegion top = new TextureRegion(tex, cell, tex.getHeight() - cell, tex.getWidth() - 2*cell, cell);
        TextureRegion bottom = new TextureRegion(tex, cell, 0, tex.getWidth() - 2*cell, cell);
        TextureRegion left = new TextureRegion(tex, 0, cell, cell, tex.getHeight() - 2*cell);
        TextureRegion right = new TextureRegion(tex, tex.getWidth() - cell, cell, cell, tex.getHeight() - 2*cell);
        TextureRegion center = new TextureRegion(tex, cell, cell, tex.getWidth() - 2*cell, tex.getHeight() - 2*cell);

        batch.draw(tl, l, t - cell, cell, cell);
        batch.draw(tr, r - cell, t - cell, cell, cell);
        batch.draw(bl, l, b, cell, cell);
        batch.draw(br, r - cell, b, cell, cell);

        batch.draw(top, l + cell, t - cell, width - 2*cell, cell);
        batch.draw(bottom, l + cell, b, width - 2*cell, cell);
        batch.draw(left, l, b + cell, cell, height - 2*cell);
        batch.draw(right, r - cell, b + cell, cell, height - 2*cell);
        batch.draw(center, l + cell, b + cell, width - 2*cell, height - 2*cell);
    }

    public boolean isFinished() {
        return isFinished;
    }
}

package org.lud.game.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Tile extends Actor {
    private final ShapeRenderer shaper;
    private final Color color;
    private final float size;

    public Tile(ShapeRenderer shaper, Color color, float x, float y, float size) {
        this.shaper = shaper;
        this.color = color;
        this.size = size;
        setPosition(x, y);
        setSize(size, size);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.end();
        shaper.begin(ShapeRenderer.ShapeType.Filled);
        shaper.setColor(color);
        shaper.rect(getX(), getY(), getWidth(), getHeight());
        shaper.end();
        batch.begin();
    }
}

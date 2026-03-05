package org.lud.engine.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Window extends Actor {
    private final Texture texture;
    private TextureRegion tl, tr, bl, br;
    private TextureRegion top, bottom, left, right, center;

    private float width, height;
    private final float padding = 16f;
    private final int cell = 4;
    private final float scale = 4f;
    private final float scaledCell = cell * scale;

    public Window(float x, float y, float width, float height) {
        this.texture = new Texture("tooltip.png");
        this.tl = new TextureRegion(texture, 0, texture.getHeight() - cell, cell, cell);
        this.tr = new TextureRegion(texture, texture.getWidth() - cell, texture.getHeight() - cell, cell, cell);
        this.bl = new TextureRegion(texture, 0, 0, cell, cell);
        this.br = new TextureRegion(texture, texture.getWidth() - cell, 0, cell, cell);
        this.top = new TextureRegion(texture, cell, texture.getHeight() - cell, texture.getWidth() - 2 * cell, cell);
        this.bottom = new TextureRegion(texture, cell, 0, texture.getWidth() - 2 * cell, cell);
        this.left = new TextureRegion(texture, 0, cell, cell, texture.getHeight() - 2 * cell);
        this.right = new TextureRegion(texture, texture.getWidth() - cell, cell, cell, texture.getHeight() - 2 * cell);
        this.center = new TextureRegion(texture, cell, cell, texture.getWidth() - 2 * cell, texture.getHeight() - 2 * cell);

        top.flip(false, true);
        bottom.flip(false, true);
        left.flip(false, true);
        right.flip(false, true);
        center.flip(false, true);
        tl.flip(false, true);
        tr.flip(false, true);
        bl.flip(false, true);
        br.flip(false, true);

        setPosition(x, y);
        setSize(width, height);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        float l = getX();
        float b = getY();
        float r = l + width;
        float t = b + height;

        float innerWidth  = width  - 2 * scaledCell;
        float innerHeight = height - 2 * scaledCell;

        batch.draw(tl, l, t - scaledCell, scaledCell, scaledCell);
        batch.draw(tr, r - scaledCell, t - scaledCell, scaledCell, scaledCell);
        batch.draw(bl, l, b, scaledCell, scaledCell);
        batch.draw(br, r - scaledCell, b, scaledCell, scaledCell);

        batch.draw(top, l + scaledCell, t - scaledCell, innerWidth, scaledCell);
        batch.draw(bottom, l + scaledCell, b, innerWidth, scaledCell);
        batch.draw(left, l, b + scaledCell, scaledCell, innerHeight);
        batch.draw(right, r - scaledCell, b + scaledCell, scaledCell, innerHeight);

        batch.draw(center, l + scaledCell, b + scaledCell, innerWidth, innerHeight);
    }
}

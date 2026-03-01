package org.lud.engine.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Slicer {
    private static class SliceSet {
        TextureRegion tl, tr, bl, br;
        TextureRegion top, bottom, left, right, center;
    }

    private final float cell;
    private final SliceSet normal;
    private final SliceSet highlighted;

    public Slicer(int cell, Texture normalTex, Texture highlightedTex) {
        this.cell = cell;

        this.normal = buildSliceSet(normalTex);
        this.highlighted = buildSliceSet(highlightedTex);
    }

    private SliceSet buildSliceSet(Texture texture) {
        SliceSet set = new SliceSet();

        int texW = texture.getWidth();
        int texH = texture.getHeight();
        int centerW = texW - 2 * (int) cell;
        int centerH = texH - 2 * (int) cell;

        set.tl = new TextureRegion(texture, 0, texH - (int)cell, (int)cell, (int)cell);
        set.tr = new TextureRegion(texture, texW - (int)cell, texH - (int)cell, (int)cell, (int)cell);
        set.bl = new TextureRegion(texture, 0, 0, (int)cell, (int)cell);
        set.br = new TextureRegion(texture, texW - (int)cell, 0, (int)cell, (int)cell);

        set.top = new TextureRegion(texture, (int)cell, texH - (int)cell, centerW, (int)cell);
        set.bottom = new TextureRegion(texture, (int)cell, 0, centerW, (int)cell);
        set.left = new TextureRegion(texture, 0, (int)cell, (int)cell, centerH);
        set.right = new TextureRegion(texture, texW - (int)cell, (int)cell, (int)cell, centerH);
        set.center = new TextureRegion(texture, (int)cell, (int)cell, centerW, centerH);

        flipAll(set);
        return set;
    }

    private void flipAll(SliceSet set) {
        set.tl.flip(false, true);
        set.tr.flip(false, true);
        set.bl.flip(false, true);
        set.br.flip(false, true);
        set.top.flip(false, true);
        set.bottom.flip(false, true);
        set.left.flip(false, true);
        set.right.flip(false, true);
        set.center.flip(false, true);
    }

    public void draw(Batch batch,
                     float x, float y,
                     float width, float height,
                     float scale,
                     boolean hovered) {

        SliceSet set = hovered ? highlighted : normal;

        float l = x;
        float r = x + width;
        float t = y + height;
        float b = y;

        float c = cell * scale;

        batch.draw(set.tl, l, t - c, c, c);
        batch.draw(set.tr, r - c, t - c, c, c);
        batch.draw(set.bl, l, b, c, c);
        batch.draw(set.br, r - c, b, c, c);

        batch.draw(set.top, l + c, t - c, width - 2 * c, c);
        batch.draw(set.bottom, l + c, b, width - 2 * c, c);
        batch.draw(set.left, l, b + c, c, height - 2 * c);
        batch.draw(set.right, r - c, b + c, c, height - 2 * c);
        batch.draw(set.center, l + c, b + c, width - 2 * c, height - 2 * c);
    }
}

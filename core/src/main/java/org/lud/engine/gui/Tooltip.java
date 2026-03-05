package org.lud.engine.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Tooltip {
    private String text;
    private final BitmapFont font;
    private final NinePatch patch;

    private float x, y;
    private boolean isVisible = false;
    private float timer = 0f;
    private final float delay = 0.25f;

    private final float cursorOffsetX = 80f;
    private final float cursorOffsetY = 32f;
    private float padding = 16f;

    public Tooltip(String text, BitmapFont font, Texture texture, int patchBorder) {
        this.text = text;
        this.font = font;
        this.patch = new NinePatch(texture, patchBorder, patchBorder, patchBorder, patchBorder);
    }

    public void setText(String text) {
        this.text = text;
    }

    public void update(float delta, boolean isHovering, float mouseX, float mouseY) {
        if(isHovering) {
            timer += delta;
            if(timer >= delay) {
                isVisible = true;
                x = mouseX + cursorOffsetX;
                y = mouseY + cursorOffsetY;
            }
        } else {
            timer = 0f;
            isVisible = false;
        }
    }

    public void render(SpriteBatch batch) {
        if(!isVisible) return;

        String localizedText = Localization.lang.t(text);
        String[] lines = localizedText.split("\n");

        GlyphLayout layout = new GlyphLayout();
        float maxLineWidth = 0;
        for(String line : lines) {
            layout.setText(font, line);
            if(layout.width > maxLineWidth) maxLineWidth = layout.width;
        }

        float textHeight = layout.height * lines.length + (lines.length - 1) * 2;
        float w = Math.max(maxLineWidth + 2 * padding, patch.getTotalWidth());
        float h = Math.max(textHeight + 2 * padding, patch.getTotalHeight());

        float drawX = x;
        float drawY = y;
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        if (drawX + w > screenWidth) drawX = screenWidth - w;
        if (drawY - h < 0) drawY = h;

        batch.begin();
        patch.draw(batch, drawX, drawY - h, w, h);

        float textX = drawX + padding;
        float textY = drawY - padding;

        if(lines.length == 1) textY -= layout.height/2f;

        font.setColor(Color.WHITE);
        for(String line : lines) {
            layout.setText(font, line);
            font.draw(batch, layout, textX, textY);
            textY -= layout.height + 8;
        }
        batch.end();
    }
}

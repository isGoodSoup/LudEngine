package org.lud.game.data;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.lud.engine.gui.Localization;

public class Tooltip {
    private String text;
    private final BitmapFont font;
    private final Color bgColor;
    private final float padding = 15f;
    private float x, y;
    private boolean visible = false;
    private float timer = 0f;
    private final float delay = 0.5f;

    public Tooltip(String text, BitmapFont font) {
        this.text = text;
        this.font = font;
        this.bgColor = new Color(0,0,0,0.7f);
    }

    public void setText(String text) {
        this.text = text;
    }

    public BitmapFont getFont() {
        return font;
    }

    public void update(float delta, boolean isHovering, float mouseX, float mouseY) {
        if(isHovering) {
            timer += delta;
            if(timer >= delay) {
                visible = true;
                x = mouseX;
                y = mouseY;
            }
        } else {
            timer = 0f;
            visible = false;
        }
    }

    public void render(SpriteBatch batch, ShapeRenderer shaper) {
        if(!visible) { return; }

        GlyphLayout layout = new GlyphLayout(font, Localization.lang.t(text));
        float width = layout.width + 2 * padding;
        float height = layout.height + 2 * padding;

        shaper.begin(ShapeRenderer.ShapeType.Filled);
        shaper.setColor(bgColor);
        shaper.rect(x, y - height, width, height);
        shaper.end();

        batch.begin();
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        font.draw(batch, layout, x + padding, y - padding);
        batch.end();
    }
}

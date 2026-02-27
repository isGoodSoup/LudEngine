package org.lud.game.data;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

public class Tooltip {
    private String text;
    private final BitmapFont font;
    private final Color bgColor;
    private final float padding = 5f;
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

    public void render(SpriteBatch batch, ShapeRenderer shapeRenderer) {
        if(!visible) { return; }

        GlyphLayout layout = new GlyphLayout(font, text);
        float width = layout.width + 2*padding;
        float height = layout.height + 2*padding;

        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(bgColor);
        shapeRenderer.rect(x, y - height, width, height);
        shapeRenderer.end();

        batch.begin();
        font.draw(batch, layout, x + padding, y - padding);
        batch.end();
    }
}

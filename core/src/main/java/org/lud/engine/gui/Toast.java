package org.lud.engine.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.utils.Align;

public class Toast extends Actor {
    private final BitmapFont font;
    private String title;
    private String description;
    private final NinePatch patch;
    private final GlyphLayout layout = new GlyphLayout();

    private float width, height;
    private final float padding = 16f;
    private final float duration = 2f;
    private boolean isFinished = false;

    public Toast(String title, String description, BitmapFont font, float targetX, float targetY,
                 Texture background, int patchBorder) {
        this.title = title;
        this.description = description;
        this.font = font;
        this.patch = new NinePatch(background, patchBorder, patchBorder, patchBorder, patchBorder);

        this.width = Gdx.graphics.getWidth() / 3f;
        this.height = 150f;

        setPosition(targetX, -height);
        setSize(width, height);

        float slideTime = 0.5f;

        addAction(Actions.sequence(
            Actions.moveTo(targetX, targetY, slideTime),
            Actions.delay(duration),
            Actions.moveTo(targetX, -height, slideTime),
            Actions.run(() -> isFinished = true)
        ));
    }

    @Override
    public void draw(com.badlogic.gdx.graphics.g2d.Batch batch, float parentAlpha) {
        batch.begin();
        patch.draw(batch, getX(), getY(), getWidth(), getHeight());
        float textX = getX() + padding;
        float textY = getY() + getHeight() - padding;
        layout.setText(font, title);
        font.setColor(Color.YELLOW);
        font.draw(batch, layout, textX, textY);
        layout.setText(font, description, Color.WHITE, getWidth() - 2 * padding, Align.left, true);
        font.draw(batch, layout, textX, textY - layout.height - 4f);
        batch.end();
    }

    public boolean isFinished() {
        return isFinished;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }
}

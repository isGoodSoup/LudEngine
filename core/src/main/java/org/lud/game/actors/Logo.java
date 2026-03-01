package org.lud.game.actors;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

public class Logo extends Actor {
    private final Texture logo;

    public Logo(Texture logo, float x, float y) {
        this.logo = logo;
        setPosition(x, y);
        setSize(logo.getWidth(), logo.getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        batch.draw(logo, getX(), getY(), getWidth(), getHeight());
    }
}

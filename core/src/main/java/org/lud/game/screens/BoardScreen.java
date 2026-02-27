package org.lud.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import org.lud.engine.core.GameFrame;

public class BoardScreen implements Screen {
    private final GameFrame gameFrame;
    private ShapeRenderer shaper;

    public BoardScreen(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
    }

    @Override
    public void show() {
        this.shaper = new ShapeRenderer();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        shaper.begin(ShapeRenderer.ShapeType.Filled);

        int tileSize = 64;
        for(int row = 0; row < 8; row++) {
            for(int col = 0; col < 8; col++) {
                if ((row + col) % 2 == 0) {
                    shaper.setColor(Color.CORAL);
                } else {
                    shaper.setColor(Color.BROWN);
                }

                shaper.rect(col * tileSize, row * tileSize,
                    tileSize, tileSize);
            }
        }

        shaper.end();
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        shaper.dispose();
    }
}

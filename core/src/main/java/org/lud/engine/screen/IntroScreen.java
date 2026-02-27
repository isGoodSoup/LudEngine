package org.lud.engine.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.lud.engine.core.GameFrame;
import org.lud.engine.core.Intro;
import org.lud.game.gui.MainMenu;

public class IntroScreen implements Screen {
    private final GameFrame gameFrame;
    private final Intro intro;
    private SpriteBatch batch;

    public IntroScreen(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        this.intro = new Intro();
    }

    @Override
    public void show() {
        this.batch = new SpriteBatch();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        intro.draw(batch, delta, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.end();

        if(intro.isFinished()) {
            gameFrame.setScreen(new MainMenu(gameFrame));
            dispose();
        }
    }

    @Override public void resize(int width, int height) {}
    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        intro.dispose();
    }
}

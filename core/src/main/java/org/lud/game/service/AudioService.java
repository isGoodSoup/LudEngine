package org.lud.game.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

public class AudioService {
    private Music music;
    private float volume = 0f;

    public void playMusic() {
        if(music == null) {
            music = Gdx.audio.newMusic(Gdx.files.internal("sounds/main-theme.ogg"));
            music.setLooping(true);
            music.setVolume(0.5f);
        }
        music.play();
    }

    public void stopMusic() {
        if(music != null) {
            music.stop();
        }
    }

    public void update(float delta) {
        if(volume < 0.5f) {
            volume += delta * 0.5f;
            music.setVolume(volume);
        }
    }

    public void dispose() {
        if(music != null) {
            music.dispose();
        }
    }
}

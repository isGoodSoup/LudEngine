package org.lud.game.service;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

public class AudioService {
    private Sound[] fx;
    private Music music;
    private float masterGain = 1f;
    private float musicGain  = 1f;
    private float sfxGain    = 1f;

    public AudioService() {
        loadFx();
    }

    public void loadFx() {
        this.fx = new Sound[2];
        fx[0] = loadSound("piece-fx", ".wav");
        fx[1] = loadSound("menu-select", ".wav");
    }

    public Sound loadSound(String file, String format) {
        return Gdx.audio.newSound(Gdx.files.internal("sounds/" + file + format));
    }

    public Music loadMusic(String file, String format) {
        return Gdx.audio.newMusic(Gdx.files.internal("sounds/" + file + format));
    }

    public void toggleMusic() {
        if(music.isPlaying()) {
            stopMusic();
        } else {
            playMusic();
        }
    }

    public void playMusic() {
        if(music == null) {
            music = loadMusic("main-theme", ".ogg");
            music.setLooping(true);
            music.setVolume(0.5f);
        }
        music.play();
    }

    public Sound playFX(int i) {
        Sound sound = fx[i];
        sound.play(masterGain * sfxGain);
        return sound;
    }

    private void apply() {
        if (music != null) {
            music.setVolume(effectiveMusicVolume());
        }
    }

    private float effectiveMusicVolume() {
        return masterGain * musicGain;
    }

    private float sliderToDb(float slider) {
        float minDb = -40f;
        float maxDb = 0f;
        return minDb + (maxDb - minDb) * slider;
    }

    private float dbToLinear(float db) {
        return (float) Math.pow(10f, db/20f);
    }

    public void setMasterVolume(float slider) {
        masterGain = dbToLinear(sliderToDb(slider));
    }

    public void setMusicVolume(float slider) {
        musicGain = dbToLinear(sliderToDb(slider));
        apply();
    }

    public void setSfxVolume(float slider) {
        sfxGain = dbToLinear(sliderToDb(slider));
    }

    public void stopMusic() {
        if(music != null) {
            music.stop();
        }
    }

    public void dispose() {
        if(music != null) {
            music.dispose();
        }
    }
}

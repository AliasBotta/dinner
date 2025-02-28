package io.github.dinner.controller;

import com.badlogic.gdx.audio.Music;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

class AudioControllerTest {

    private Music mockMusic;
    private Music mockSound;

    @BeforeEach
    void setup() {
        mockMusic = Mockito.mock(Music.class);
        mockSound = Mockito.mock(Music.class);

        // Inizializza la musicMap con un mock
        Map<String, Music> musicMap = new HashMap<>();
        musicMap.put("MainMenu", mockMusic);
        musicMap.put("garden_ambience", mockMusic);
        musicMap.put("home", mockMusic);
        AudioController.musicMap = musicMap;

        // Inizializza la soundMap con un mock
        Map<String, Music> soundMap = new HashMap<>();
        soundMap.put("woodFS1", mockSound);
        soundMap.put("click_down", mockSound);
        AudioController.soundMap = soundMap;
    }

    @Test
    void testPlayMusic() {
        AudioController.playMusic("MainMenu", true);

        verify(mockMusic).setLooping(true);
        verify(mockMusic).play();
    }

    @Test
    void testPlaySound() {
        AudioController.playSound("woodFS1", false);

        verify(mockSound).setLooping(false);
        verify(mockSound).play();
    }

    @Test
    void testSetMusicVolume() {
        float expectedVolume = 0.5f;
        AudioController.setMusicVolume(expectedVolume);

        verify(mockMusic, atLeastOnce()).setVolume(expectedVolume);
    }

    @Test
    void testSetSoundVolume() {
        float expectedVolume = 0.8f;
        AudioController.setSoundVolume(expectedVolume);

        verify(mockSound, atLeastOnce()).setVolume(expectedVolume);
    }

    @Test
    void testPlayMusicOfLevel() {
        AudioController.playMusicOfLevel("gardenSFLevel");

        verify(mockMusic).setLooping(true);
        verify(mockMusic).play();
    }

    @Test
    void testPauseMusic() {
        AudioController.playMusic("MainMenu", false);
        AudioController.pauseMusic();

        verify(mockMusic).pause();
    }

    @Test
    void testStopMusic() {
        AudioController.playMusic("MainMenu", false);
        AudioController.stopMusic();

        verify(mockMusic).stop();
    }

    @Test
    void testStopAllSounds() {
        AudioController.playSound("woodFS1", false);
        AudioController.stopAllSounds();

        verify(mockSound).stop();
    }

    @Test
    void testStopSound() {
        AudioController.playSound("woodFS1", false);
        AudioController.stopSound("woodFS1");

        verify(mockSound).stop();
    }

    @Test
    void testPlayNonExistentMusic() {
        AudioController.playMusic("nonExistent", true);

        verify(mockMusic, never()).play();
    }

    @Test
    void testPlayNonExistentSound() {
        AudioController.playSound("nonExistent", false);

        verify(mockSound, never()).play();
    }
}

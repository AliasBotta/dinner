package io.github.dinner.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.Preferences;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SettingsControllerTest {

    private Preferences mockPreferences;
    private SettingsController settingsController;

    @BeforeEach
    void setup() {
        mockPreferences = Mockito.mock(Preferences.class);
        SettingsController.setPreferencesForTesting(mockPreferences);

        Graphics mockGraphics = Mockito.mock(Graphics.class);
        Gdx.graphics = mockGraphics; // Mock di Gdx.graphics
    }

    @Test
    void testLoadSettings() {
        when(mockPreferences.getFloat("musicVolume", 0.8f)).thenReturn(0.5f);
        when(mockPreferences.getInteger("screenWidth", 640)).thenReturn(800);
        when(mockPreferences.getInteger("screenHeight", 480)).thenReturn(600);
        when(mockPreferences.getBoolean("fullscreen", true)).thenReturn(false);
        when(mockPreferences.getBoolean("vsync", false)).thenReturn(true);

        settingsController.load();

        assertEquals(0.5f, settingsController.getMusicVolume(), 0.01f, "Music volume should be loaded correctly.");
        assertEquals(800, settingsController.getScreenWidth(), "Screen width should be loaded correctly.");
        assertEquals(600, settingsController.getScreenHeight(), "Screen height should be loaded correctly.");
        assertFalse(settingsController.isFullscreen(), "Fullscreen setting should be loaded correctly.");
        assertTrue(settingsController.isVsync(), "VSync setting should be loaded correctly.");
    }

    @Test
    void testSaveMusicVolume() {
        float newVolume = 0.7f;
        settingsController.saveMusicVolume(newVolume);

        verify(mockPreferences).putFloat("musicVolume", newVolume);
        verify(mockPreferences).flush();
        assertEquals(newVolume, settingsController.getMusicVolume(), 0.01f, "Music volume should be saved and retrieved correctly.");
    }

    @Test
    void testSaveScreenSize() {
        int newWidth = 1024;
        int newHeight = 768;

        settingsController.saveScreenSize(newWidth, newHeight);

        verify(mockPreferences).putInteger("screenWidth", newWidth);
        verify(mockPreferences).putInteger("screenHeight", newHeight);
        verify(mockPreferences).flush();
        assertEquals(newWidth, settingsController.getScreenWidth(), "Screen width should be saved correctly.");
        assertEquals(newHeight, settingsController.getScreenHeight(), "Screen height should be saved correctly.");
    }

    @Test
    void testSaveFullscreen() {
        settingsController.saveFullscreen(true);
        verify(mockPreferences).putBoolean("fullscreen", true);
        verify(mockPreferences).flush();
        assertTrue(settingsController.isFullscreen(), "Fullscreen setting should be saved and retrieved correctly.");

        settingsController.saveFullscreen(false);
        verify(mockPreferences).putBoolean("fullscreen", false);
        verify(mockPreferences, times(2)).flush(); // Second flush for second call
        assertFalse(settingsController.isFullscreen(), "Fullscreen setting should be saved and retrieved correctly.");
    }

    @Test
    void testSaveVsync() {
        settingsController.saveVsync(true);
        verify(mockPreferences).putBoolean("vsync", true);
        verify(mockPreferences).flush();
        assertTrue(settingsController.isVsync(), "VSync setting should be saved and retrieved correctly.");

        settingsController.saveVsync(false);
        verify(mockPreferences).putBoolean("vsync", false);
        verify(mockPreferences, times(2)).flush(); // Second flush for second call
        assertFalse(settingsController.isVsync(), "VSync setting should be saved and retrieved correctly.");
    }

    @Test
    void testSetMusicVolume() {
        float volume = 0.6f;
        settingsController.saveMusicVolume(volume);
        settingsController.setMusicVolume();

        verify(mockPreferences).putFloat("musicVolume", volume);
        verify(mockPreferences).flush();
    }

    @Test
    void testSetSoundVolume() {
        float volume = 0.6f;
        settingsController.saveSoundVolume(volume);
        settingsController.setSoundVolume();

        verify(mockPreferences).putFloat("soundVolume", volume);
        verify(mockPreferences).flush();
    }

    @Test
    void testSetScreenSize() {
        settingsController.saveScreenSize(1280, 720);
        settingsController.setScreenSize();

        assertEquals(1280, settingsController.getScreenWidth(), "Screen width should be set correctly.");
        assertEquals(720, settingsController.getScreenHeight(), "Screen height should be set correctly.");
    }

    @Test
    void testSetFullScreen() {
        Graphics mockGraphics = Gdx.graphics;

        settingsController.saveFullscreen(true);
        settingsController.setFullScreen();

        verify(mockGraphics).setFullscreenMode(any());
        verify(mockPreferences).putBoolean("fullscreen", true);
        verify(mockPreferences).flush();
    }

    @Test
    void testSetVsync() {
        Graphics mockGraphics = Gdx.graphics;

        settingsController.saveVsync(true);
        settingsController.setVsync();

        verify(mockGraphics).setVSync(true);
        verify(mockPreferences).putBoolean("vsync", true);
        verify(mockPreferences).flush();
    }
}

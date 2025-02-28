package io.github.dinner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Graphics;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import io.github.dinner.view.screens.GameScreen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DinnerTest {

    private Dinner dinner;
    private Graphics mockGraphics;
    private Skin mockSkin;
    private Music mockMusic;
    private SpriteBatch mockBatch;

    @BeforeEach
    void setup() {
        // Mock Gdx.graphics
        mockGraphics = Mockito.mock(Graphics.class);
        Gdx.graphics = mockGraphics;

        // Mock other dependencies
        mockSkin = Mockito.mock(Skin.class);
        mockMusic = Mockito.mock(Music.class);
        mockBatch = Mockito.mock(SpriteBatch.class);

        // Initialize Dinner instance
        dinner = new Dinner();
        dinner.skin = mockSkin;
        dinner.batch = mockBatch; // Mock del batch
    }

    @Test
    void testSetScreen() {
        GameScreen mockScreen = Mockito.mock(GameScreen.class);
        dinner.setScreen(mockScreen);

        assertEquals(mockScreen, dinner.getScreen(), "The active screen should be the one that was set.");
    }

    @Test
    void testDispose() {
        dinner.dispose();

        verify(mockSkin).dispose();
        verify(mockBatch).dispose();
    }

    @Test
    void testResize() {
        int newWidth = 1920;
        int newHeight = 1080;
        GameScreen mockScreen = Mockito.mock(GameScreen.class);
        dinner.setScreen(mockScreen);

        dinner.resize(newWidth, newHeight);

        verify(mockScreen).resize(newWidth, newHeight);
    }

    @Test
    void testRender() {
        GameScreen mockScreen = Mockito.mock(GameScreen.class);
        dinner.setScreen(mockScreen);

        float deltaTime = 1.0f / 60.0f; // Simulazione di 60 FPS
        when(Gdx.graphics.getDeltaTime()).thenReturn(deltaTime);

        dinner.render();

        verify(mockScreen).render(deltaTime); // Verifica il deltaTime passato
    }
}

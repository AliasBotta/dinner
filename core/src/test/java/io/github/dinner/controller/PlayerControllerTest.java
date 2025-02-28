package io.github.dinner.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import io.github.dinner.model.Player;
import io.github.dinner.view.screens.GameScreen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PlayerControllerTest {

    private PlayerController playerController;
    private Player mockPlayer;

    @BeforeEach
    void setUp() {
        mockPlayer = mock(Player.class);

        // Mock the Player's box position
        when(mockPlayer.getBox()).thenReturn(new Rectangle(0, 0, 1, 1));

        // Mock LevelController and GameScreen.levelController
        LevelController mockLevelController = mock(LevelController.class);
        GameScreen.levelController = mockLevelController;

        // Mock TiledMap and its layers
        com.badlogic.gdx.maps.tiled.TiledMap mockTiledMap = mock(com.badlogic.gdx.maps.tiled.TiledMap.class);
        when(mockLevelController.getMap()).thenReturn(mockTiledMap);

        // Mock MapLayers
        com.badlogic.gdx.maps.MapLayers mockMapLayers = mock(com.badlogic.gdx.maps.MapLayers.class);
        when(mockTiledMap.getLayers()).thenReturn(mockMapLayers);

        // Mock delle animazioni
        Animation<TextureRegion> mockIdleAnimation = mock(Animation.class);
        Animation<TextureRegion> mockWalkAnimation = mock(Animation.class);
        when(mockPlayer.getIdleAnimation(any())).thenReturn(mockIdleAnimation);
        when(mockPlayer.getWalkAnimation(any())).thenReturn(mockWalkAnimation);

        // Mock the collision layer
        MapLayer mockMapLayer = mock(MapLayer.class);
        when(mockMapLayers.get("Collisioni")).thenReturn(mockMapLayer);

        // Mock the map objects
        MapObjects mockMapObjects = mock(MapObjects.class);
        when(mockMapLayer.getObjects()).thenReturn(mockMapObjects);

        // Pass the mockPlayer to the PlayerController
        playerController = new PlayerController(mockPlayer);
    }

    @Test
    void testKeyDown() {
        int keycode = Input.Keys.W;
        assertTrue(playerController.keyDown(keycode));
    }

    @Test
    void testKeyUp() {
        int keycode = Input.Keys.W;
        playerController.keyDown(keycode);
        assertTrue(playerController.keyUp(keycode));
    }

    @Test
    void testUpdateInput_NoPressedKeys() {
        playerController.updateInput(1.0f);
        assertNotNull(PlayerController.getAnimationToRender());
    }

    @Test
    void testReleaseAllKeys() {
        playerController.keyDown(Input.Keys.W);
        playerController.keyDown(Input.Keys.A);
        playerController.releaseAllKeys();

        assertTrue(playerController.keyUp(Input.Keys.W));
        assertTrue(playerController.keyUp(Input.Keys.A));
    }
}

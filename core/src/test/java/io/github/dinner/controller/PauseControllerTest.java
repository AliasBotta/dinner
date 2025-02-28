package io.github.dinner.controller;

import io.github.dinner.Dinner;
import io.github.dinner.Dinner.GameState;
import io.github.dinner.view.screens.GameScreen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PauseControllerTest {

    private Dinner game;
    private PauseController pauseController;

    @BeforeEach
    void setup() {
        game = new Dinner();
        game.gameState = GameState.RUNNING; // Start with RUNNING state
        pauseController = new PauseController(game);

        // Mock the menu controller
        GameScreen.menuController = mock(MenuController.class);

        // Set dialogue box visibility to false
        GameScreen.isDialogueBoxVisible = false;
    }

    @Test
    void testKeyDownWhileDialogueBoxIsVisible() {
        // Set dialogue box visibility to true
        GameScreen.isDialogueBoxVisible = true;

        // Simulate pressing the ESCAPE key
        boolean result = pauseController.keyDown(com.badlogic.gdx.Input.Keys.ESCAPE);
        assertFalse(result, "keyDown should return false when dialogue box is visible.");

        // Ensure the game state remains unchanged
        assertEquals(GameState.RUNNING, game.gameState, "Game state should remain RUNNING when dialogue box is visible.");
    }

    @Test
    void testKeyDownWithNonEscapeKey() {
        // Simulate pressing a non-ESCAPE key
        boolean result = pauseController.keyDown(com.badlogic.gdx.Input.Keys.SPACE);
        assertFalse(result, "keyDown should return false for non-ESCAPE keys.");

        // Ensure the game state remains unchanged
        assertEquals(GameState.RUNNING, game.gameState, "Game state should remain unchanged when a non-ESCAPE key is pressed.");
    }
}

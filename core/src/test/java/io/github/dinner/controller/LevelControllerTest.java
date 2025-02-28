package io.github.dinner.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;

class LevelControllerTest {

    private LevelController levelController;

    @BeforeEach
    void setUp() {
        levelController = LevelController.getLevelController();

        // Clear loadedLevels map before each test
        LevelController.loadedLevels = new HashMap<>();
    }

    @Test
    void testSingletonInstance() {
        LevelController anotherInstance = LevelController.getLevelController();
        assertSame(levelController, anotherInstance, "LevelController should be a singleton");
    }

}

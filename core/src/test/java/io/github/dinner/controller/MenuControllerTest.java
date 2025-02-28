package io.github.dinner.controller;

import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.SnapshotArray;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.dinner.model.states.MenuState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class MenuControllerTest {

    private MenuController menuController;
    private MenuState mockMenuState;
    private Stage mockStage;
    private InputMultiplexer mockInputMultiplexer;

    @BeforeEach
    void setup() {
        mockMenuState = Mockito.mock(MenuState.class);
        mockStage = Mockito.mock(Stage.class);
        mockInputMultiplexer = Mockito.mock(InputMultiplexer.class);

        // Mock del viewport
        Viewport mockViewport = Mockito.mock(Viewport.class);
        when(mockStage.getViewport()).thenReturn(mockViewport);

        when(mockMenuState.getStage()).thenReturn(mockStage);
        menuController = new MenuController(mockMenuState);

        // Mock Gdx.graphics
        Graphics mockGraphics = Mockito.mock(Graphics.class);
        when(mockGraphics.getWidth()).thenReturn(1920);
        when(mockGraphics.getHeight()).thenReturn(1080);
        com.badlogic.gdx.Gdx.graphics = mockGraphics;

        // Mock Gdx.input
        Input mockInput = Mockito.mock(Input.class);
        when(mockInput.getInputProcessor()).thenReturn(mockInputMultiplexer);
        com.badlogic.gdx.Gdx.input = mockInput;
    }

    @Test
    void testChangeState() {
        MenuState newMenuState = mock(MenuState.class);
        Stage newStage = mock(Stage.class);
        Viewport newViewport = mock(Viewport.class);

        Table mockTable = Mockito.mock(Table.class);

        when(newMenuState.getStage()).thenReturn(newStage);
        when(newStage.getViewport()).thenReturn(newViewport);
        when(newMenuState.getTable()).thenReturn(mockTable);
        when(newMenuState.getButtonWidthPercentage()).thenReturn(0.1f);
        when(newMenuState.getButtonHeightPercentage()).thenReturn(0.05f);
        when(newMenuState.getButtonPadPercentage()).thenReturn(0.02f);
        when(newMenuState.getScale()).thenReturn(1.0f);

        menuController.changeState(newMenuState);

        assertEquals(newMenuState, menuController.getCurrentMenu());
        verify(newViewport).update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        verify(newMenuState, times(2)).getTable();
        verify(newMenuState).drawTable(eq(mockTable), eq(192.0f), eq(54.0f), eq(21.6f), eq(54.0f));
    }

    @Test
    void testAddInputProcessor() {
        when(mockInputMultiplexer.getProcessors()).thenReturn(new SnapshotArray<>());

        menuController.addInputProcessor(mockStage);
        verify(mockInputMultiplexer, times(1)).addProcessor(mockStage);
    }

    @Test
    void testRemoveInputProcessor() {
        SnapshotArray<InputProcessor> processors = new SnapshotArray<>();
        processors.add(mockStage);
        when(mockInputMultiplexer.getProcessors()).thenReturn(processors);

        menuController.removeInputProcessor(mockStage);
        verify(mockInputMultiplexer, times(1)).removeProcessor(mockStage);
    }

    @Test
    void testGetStage() {
        assertEquals(mockStage, menuController.getStage());
    }

    @Test
    void testGetCurrentMenu() {
        assertEquals(mockMenuState, menuController.getCurrentMenu());
    }
}

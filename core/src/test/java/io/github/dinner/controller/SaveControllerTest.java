package io.github.dinner.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import io.github.dinner.model.memento.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SaveControllerTest {

    private SaveController saveController;

    @BeforeEach
    public void setUp() {
        saveController = SaveController.getInstance();
        saveController.setCurrentSlot(0); // Inizializza lo slot corrente a 0
    }

    @Test
    public void testSingletonInstance() {
        SaveController instance1 = SaveController.getInstance();
        SaveController instance2 = SaveController.getInstance();
        assertSame(instance1, instance2, "SaveController deve essere un singleton");
    }

    @Test
    public void testSetCurrentSlot() {
        saveController.setCurrentSlot(1);
        assertEquals(1, saveController.getCurrentSlot(), "Il valore dello slot corrente deve essere 1");
    }

    @Test
    public void testWriteGameDataToDisk() {
        FileHandle mockFileHandle = mock(FileHandle.class);

        SaveController.GameData mockGameData = new SaveController.GameData();
        mockGameData.playerState = new PlayerMemento();
        mockGameData.roomsState = new HashMap<>();

        // Mock delle dipendenze di Gdx.files.local
        Gdx.files = mock(com.badlogic.gdx.Files.class);
        when(Gdx.files.local(anyString())).thenReturn(mockFileHandle);
        when(mockFileHandle.file()).thenReturn(new java.io.File("mockFile.json"));

        // Simula il salvataggio
        saveController.writeGameDataToDisk();

        // Verifica che il metodo writeString sia stato chiamato con il JSON generato realmente
        verify(mockFileHandle, times(1)).writeString("{}", false);
    }
}

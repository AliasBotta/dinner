package io.github.dinner.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.math.Rectangle;
import io.github.dinner.model.Player;
import io.github.dinner.model.interactables.Item;
import io.github.dinner.model.interactables.Npc;
import io.github.dinner.model.interactables.InteractableObject;
import io.github.dinner.view.screens.GameScreen;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class InteractableObjectControllerTest {

    private InteractableObjectController controller;
    private LevelController levelControllerMock;
    private MapLayer collisionLayerMock;
    private MapObjects mapObjectsMock;

    @BeforeEach
    void setUp() {
        // Mock dependencies
        levelControllerMock = mock(LevelController.class);
        collisionLayerMock = mock(MapLayer.class);
        mapObjectsMock = mock(MapObjects.class);

        // Mock TiledMap e MapLayers
        TiledMap tiledMapMock = mock(TiledMap.class);
        MapLayers mapLayersMock = mock(MapLayers.class);
        when(tiledMapMock.getLayers()).thenReturn(mapLayersMock);
        when(mapLayersMock.get("Collisioni")).thenReturn(collisionLayerMock);
        when(collisionLayerMock.getObjects()).thenReturn(mapObjectsMock);

        // Mock MapObjects iterator
        when(mapObjectsMock.iterator()).thenReturn(new ArrayList<MapObject>().iterator());

        // Collegare il mock al livello
        when(levelControllerMock.getMap()).thenReturn(tiledMapMock);
        GameScreen.levelController = levelControllerMock;

        // Mock Gdx.files
        Gdx.files = mock(Files.class);

        // Mock Gdx.gl
        Gdx.gl = mock(GL20.class);

        // Mock statico di Player
        Player mockPlayer = mock(Player.class);
        Player.player = mockPlayer; // Accesso diretto al campo statico (modifica temporanea per il test)
        when(mockPlayer.getPlayerDirection()).thenReturn(Player.PlayerDirection.EAST);

        // Inizializza il controller
        controller = InteractableObjectController.getInteractableObjectController();
        controller.flush(); // Pulisce tutte le liste prima di ogni test
    }

    @Test
    void testSingletonInstance() {
        InteractableObjectController anotherInstance = InteractableObjectController.getInteractableObjectController();
        assertSame(controller, anotherInstance, "Le istanze del singleton devono essere le stesse");
    }

    @Test
    void testAddAndRemoveItem() {
        Item item = mock(Item.class);
        Rectangle itemBox = new Rectangle(1, 1, 1, 1);
        when(item.getBox()).thenReturn(itemBox);

        controller.add(item);
        assertTrue(controller.getItemList().contains(item), "L'item dovrebbe essere presente nella lista");

        // Simula l'aggiunta del MapObject corrispondente
        RectangleMapObject mapObject = new RectangleMapObject(itemBox.x, itemBox.y, itemBox.width, itemBox.height);
        List<MapObject> mockObjects = new ArrayList<>();
        mockObjects.add(mapObject);
        when(mapObjectsMock.iterator()).thenReturn(mockObjects.iterator());

        controller.remove(item);
        assertFalse(controller.getItemList().contains(item), "L'item dovrebbe essere stato rimosso dalla lista");

        verify(mapObjectsMock, times(1)).remove(eq(mapObject));
    }


    @Test
    void testCheckInteractions() {
        Rectangle playerBox = new Rectangle(0, 0, 1, 1);
        Rectangle interactableBox = new Rectangle(1, 0, 1, 1);

        InteractableObject interactable = mock(InteractableObject.class);
        when(interactable.getBox()).thenReturn(interactableBox);

        controller.add(interactable);

        Player player = Player.getPlayer(); // Utilizza il mock creato nello `setUp`

        boolean interactionOccurred = controller.checkInteractions(playerBox);
        assertTrue(interactionOccurred, "Dovrebbe verificarsi un'interazione quando l'oggetto è nel range e la direzione è corretta");

        verify(interactable, times(1)).interact(player);
    }

    @Test
    void testFlush() {
        // Mock degli oggetti interagibili
        Item item = mock(Item.class);
        Npc npc = mock(Npc.class);
        InteractableObject interactable = mock(InteractableObject.class);

        // Mock delle RectangleBox
        Rectangle itemBox = new Rectangle(1, 1, 1, 1);
        Rectangle npcBox = new Rectangle(2, 2, 1, 1);
        Rectangle interactableBox = new Rectangle(3, 3, 1, 1);

        when(item.getBox()).thenReturn(itemBox);
        when(npc.getBox()).thenReturn(npcBox);
        when(interactable.getBox()).thenReturn(interactableBox);

        // Aggiungi gli oggetti al controller
        controller.add(item);
        controller.add(npc);
        controller.add(interactable);

        // Configura il mock per rimuovere oggetti
        List<MapObject> mapObjectList = new ArrayList<>();
        when(mapObjectsMock.iterator()).thenReturn(mapObjectList.iterator());

        // Esegui il flush
        controller.flush();

        // Verifica che tutte le liste siano vuote
        assertTrue(controller.getItemList().isEmpty(), "La lista degli item dovrebbe essere vuota dopo il flush");
        assertTrue(controller.getNpcList().isEmpty(), "La lista degli NPC dovrebbe essere vuota dopo il flush");
        assertTrue(controller.interactableObjectList.isEmpty(), "La lista degli oggetti interagibili dovrebbe essere vuota dopo il flush");

        // Verifica che il metodo removeFromCollisionLayer sia stato chiamato correttamente
        verify(mapObjectsMock, atLeastOnce()).iterator();
    }
}

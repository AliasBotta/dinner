package io.github.dinner.controller;

import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import io.github.dinner.model.states.LevelState;
import io.github.dinner.view.levels.basement.CorridorBasementLevel;
import io.github.dinner.view.levels.basement.SecretRoomBasementLevel;
import io.github.dinner.view.levels.cutscenes.*;
import io.github.dinner.view.levels.first_floor.*;
import io.github.dinner.view.levels.second_floor.*;
import io.github.dinner.view.levels.third_floor.CeilingRoomTFLevel;
import io.github.dinner.view.levels.third_floor.CeilingTFLevel;
import io.github.dinner.view.screens.GameScreen;
import io.github.dinner.model.memento.RoomMemento;

import java.util.HashMap;
import java.util.Map;

import static io.github.dinner.Dinner.isNewGame;

/**
 * LevelController funge da buffer per la gestione lazy dei vari LevelState e dei
 * relativi RoomMemento (o LevelMemento).
 */
public class LevelController {

    private String currentLevel;
    // Mappa con i LevelState "attivi" (già caricati)
    public static Map<String, LevelState> loadedLevels = new HashMap<>();

    // Singleton
    private static LevelController levelController;
    private LevelController() {}
    public static synchronized LevelController getLevelController() {
        if (levelController == null) {
            levelController = new LevelController();
        }
        return levelController;
    }

    /**
     * Sceglie un livello (stanza) come corrente:
     * - Se NON è già caricato, lo istanzia e ripristina il Memento (se esiste)
     * - Poi esegue init()
     */
    public void setLevel(String levelName) { // Serve a caricare lo stato corretto delle stanze quando si è già all'interno di una partita

        LevelState newState = createLevelState(levelName);

        if (!loadedLevels.containsKey(levelName) && !isNewGame) {
            // Creiamo la specifica classe LevelState
            loadedLevels.put(levelName, newState);
            // Recuperiamo un Memento salvato, se esiste (viene preso dal file di salvataggio selezionato tramite getOrLoadGameData, che restituisce il GameData associato al file di salvataggio)
            RoomMemento savedMemento = SaveController.getInstance().getLevelMemento(levelName);
            // Applichiamo i dati (se esistono)
            if (savedMemento != null) newState.restoreState(savedMemento);
            // Mettiamo in loadedLevels
        } else if (!loadedLevels.containsKey(levelName) && isNewGame) {
            loadedLevels.put(levelName, newState);
        }

        this.currentLevel = levelName;
        init();
        GameScreen.updateInteractionController();
    }

    /**
     * Carica la classe di LevelState in base al nome
     * (switch, factory, ecc.)
     */
    private LevelState createLevelState(String levelName) {
        switch (levelName) {
            case "ballroomFFLevel":
                return new BallroomFFLevel();
            case "ballroomToiletFFLevel":
                return new BallroomToiletFFLevel();
            case "closetFFLevel":
                return new ClosetFFLevel();
            case "diningRoomFFLevel":
                return new DiningRoomFFLevel();
            case "eastCorridorFFLevel":
                return new EastCorridorFFLevel();
            case "genericBathroomFFLevel":
                return new GenericBathroomFFLevel();
            case "hallFFLevel":
                return new HallFFLevel();
            case "kitchenFFLevel":
                return new KitchenFFLevel();
            case "room1FFLevel":
                return new Room1FFLevel();
            case "room2FFLevel":
                return new Room2FFLevel();
            case "westCorridorFFLevel":
                return new WestCorridorFFLevel();
            case "daughterRoomSFLevel":
                return new DaughterRoomSFLevel();
            case "eastCorridorSFLevel":
                return new EastCorridorSFLevel();
            case "gardenSFLevel":
                return new GardenSFLevel();
            case "genericBathroomSFLevel":
                return new GenericBathroomSFLevel();
            case "hallSFLevel":
                return new HallSFLevel();
            case "librarySFLevel":
                return new LibrarySFLevel();
            case "ownerBathroomSFLevel":
                return new OwnerBathroomSFLevel();
            case "ownerRoomSFLevel":
                return new OwnerRoomSFLevel();
            case "room3SFLevel":
                return new Room3SFLevel();
            case "room4SFLevel":
                return new Room4SFLevel();
            case "westCorridorSFLevel":
                return new WestCorridorSFLevel();
            case "ceilingTFLevel":
                return new CeilingTFLevel();
            case "ceilingRoomTFLevel":
                return new CeilingRoomTFLevel();
            case "corridorBasementLevel":
                return new CorridorBasementLevel();
            case "secretRoomBasementLevel":
                return new SecretRoomBasementLevel();
            case "diningRoomCutsceneLevel":
                return new DiningRoomCutsceneLevel();
            case "newGameCutsceneLevel":
                return new NewGameCutsceneLevel();
            case "eatedDiningRoomCutsceneLevel":
                return new EatedDiningRoomCutsceneLevel();
            case "room1FFCutsceneLevel":
                return new Room2FFCutsceneLevel();
            case "gardenNoiseOnBlackLevel":
                return new GardenNoiseOnBlackLevel();
            case "room1FFCutsceneLevel2":
                return new Room2FFCutsceneLevel2();
            case "gardenSFNoCorpseLevel":
                return new GardenSFNoCorpseLevel();
            case "newPhaseCutsceneLevel":
                return new NewPhaseCutsceneLevel();
            default:
                throw new IllegalArgumentException("Livello sconosciuto: " + levelName);
        }
    }


    /**
     * Eseguiamo l'init sul LevelState corrente
     */
    private void init() {
        InteractableObjectController.getInteractableObjectController().flush();
        LevelState currentState = loadedLevels.get(currentLevel);
        currentState.init();
    }

    public LevelState getCurrentLevel() {
        return loadedLevels.get(currentLevel);
    }

    public String getCurrentLevelName() {
        return currentLevel;
    }

    public TiledMap getMap() {
        return getCurrentLevel().getMap();
    }

    public OrthogonalTiledMapRenderer getRenderer() {
        return getCurrentLevel().getRenderer();
    }

    //salva sul SaveController tutti i LevelState attualmente caricati, tuttavia non scrive su disco
    public void saveAllLevels() {
        // 1) Per tutti i LevelState caricati, aggiorniamo i Memento
        for (Map.Entry<String, LevelState> entry : loadedLevels.entrySet()) {
            String lvlName = entry.getKey();
            LevelState st = entry.getValue();

            RoomMemento m = st.createMemento();
            SaveController.getInstance().putRoomMemento(lvlName, m);
        }
    }

    /**
     * Indica che un item è stato raccolto.
     * Questo aggiorna direttamente il LevelState
     * (e.g. setta itemPickList.put(itemName, true)).
     */
    public void markItemAsPicked(String itemName) {
        LevelState currentState = loadedLevels.get(currentLevel);
        if (currentState != null) {
            currentState.itemPickList.put(itemName, true);
            // (Opzionale) se vuoi subito salvare su SaveController in modo “incrementale”:
            //   SaveController.getInstance().putRoomMemento(currentLevel, currentState.createMemento());
        }
    }

    public void dispose() {
        if (currentLevel != null && loadedLevels.containsKey(currentLevel)) {
            LevelState levelState = loadedLevels.get(currentLevel);
            // 1) Creiamo un memento
            RoomMemento updatedMemento = levelState.createMemento();
            // 2) Lo salviamo nel SaveController (mappa globale)
            SaveController.getInstance().putRoomMemento(currentLevel, updatedMemento);
            // 3) Dispose
            levelState.dispose();
        }
    }
}

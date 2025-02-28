package io.github.dinner.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Timer;
import io.github.dinner.Dinner;
import io.github.dinner.model.Notebook;
import io.github.dinner.model.Player;
import io.github.dinner.model.Progress;
import io.github.dinner.model.interactables.Npc;
import io.github.dinner.model.memento.*;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SaveController {

    private Timer.Task autoSaveTask;

//    private PlayerMemento playerState;
//    private Dinner.GameState gameState;

    /**
     * Struttura per salvare TUTTI i dati di gioco in un unico JSON:
     * - PlayerMemento (stato del giocatore)
     * - Mappa di RoomMemento (una entry per ciascun "nomeLivello")
     */
    public static class GameData {
        public PlayerMemento playerState;
        public Map<String, RoomMemento> roomsState = new HashMap<>();
        public ProgressMemento progressState;
        public NotebookMemento notebookState;
        public NpcDialogMemento nDialogsState;
    }

    private int currentSlot; //slot attualmente in uso
    private int oldSlot;

    //singleton
    private static SaveController instance;
    private SaveController() { this.currentSlot = 0; }
    public static synchronized SaveController getInstance() {
        if (instance == null) {
            instance = new SaveController();
        }
        return instance;
    }

    private String getMainSaveFilePath() { return "save_slot_" + currentSlot + ".json"; }

    private String getPcSaveFilePath() {
        // Esempio: "save_slot_0_pc.json"
        return "save_slot_" + currentSlot + "_pc" + ".json";
    }

    public void setCurrentSlot(int slot){
        this.currentSlot = slot;
        currentData = null; // forziamo un reload al prossimo getOrLoadGameData()
    }

    /**
     * In memoria, teniamo un buffer (GameData).
     * Quando carichiamo dal disco, lo mettiamo qui;
     * Quando salviamo su disco, scriviamo questo buffer.
     */
    private GameData currentData;
    private GameData currentPcData;

    public void writeGameDataToDisk() { // scrive la struttura GameData su disco
        Json json = new Json();
        String dataToSave = json.toJson(getOrLoadGameData(false));
        FileHandle file = Gdx.files.local(getMainSaveFilePath());
        file.writeString(dataToSave, false);
        System.out.println("Game data salvato in: " + file.file().getAbsolutePath());
    }

    /**
     * Salva su disco in un file "PC" (rollback).
     */
    private void writeGameDataToDiskPcVersion() {
        Json json = new Json();
        System.out.println("PC" + currentData);
        String dataToSave = json.toJson(getOrLoadPcGameData(false));
        FileHandle file = Gdx.files.local(getPcSaveFilePath());
        file.writeString(dataToSave, false);
        System.out.println("Game data (PC) salvato in: " + file.file().getAbsolutePath());
    }

    //ritorna la struttura GameData, se questa non è presente la carica dalla memoria
    public GameData getOrLoadPcGameData(boolean isLoadingPhase) {

        if(isLoadingPhase == true)
            return loadPcGameData(); // :)

        if (currentData == null) { // Proviamo a caricare dal file, se esiste
            FileHandle file = Gdx.files.local(getPcSaveFilePath());
            if (file.exists()) {
                Json json = new Json();
                currentData = json.fromJson(GameData.class, file.readString());
            } else {
                currentData = isLoadingPhase ? null : new GameData();
            }
        }
        return currentData;
    }

    // metodo da usare esclusivamente al posto di getorLoadGameData(true)
    public GameData loadPcGameData(){
        FileHandle file = Gdx.files.local(getPcSaveFilePath());

        if (file.exists()) {
            Json json = new Json();
            return json.fromJson(GameData.class, file.readString());
        } else
            return null;
    }

    public GameData getOrLoadGameData(boolean isLoadingGame) {
        if (currentData == null) {
            FileHandle file = Gdx.files.local(getMainSaveFilePath());
            if (file.exists()) {
                Json json = new Json();
                currentData = json.fromJson(GameData.class, file.readString());
            } else {
                currentData = isLoadingGame ? null : new GameData();
            }
        }
        return currentData;
    }

    public RoomMemento getLevelMemento(String levelName) {
        return getOrLoadGameData(false).roomsState.get(levelName);
    }

    public ProgressMemento getProgressState () { return getOrLoadGameData(false).progressState; }

    public NotebookMemento getNotebookState () { return getOrLoadGameData(false).notebookState; }

    public void setProgressState (ProgressMemento progressMemento) { getOrLoadGameData(false).progressState = progressMemento; }

    public void setNotebookState (NotebookMemento notebookMemento) { getOrLoadGameData(false).notebookState = notebookMemento; }

    /**
     * Inserisce/aggiorna il memento per una data stanza.
     * NON scrive su disco immediatamente (vedi saveAll()).
     */
    public void putRoomMemento(String levelName, RoomMemento m) {
        getOrLoadGameData(false).roomsState.put(levelName, m);
    }

    public void setPlayerState(PlayerMemento playerState) {
        getOrLoadGameData(false).playerState = playerState;
    }

    public void setNpcDialogState(NpcDialogMemento npcDialogState) {getOrLoadGameData(false).nDialogsState = npcDialogState ; }

//    private static PlayerMemento capturePlayerState() {
//        Player player = Player.getPlayer();
//        List<ItemMemento> capturedInventory = new ArrayList<>();  // Lista temporanea per catturare l'inventario
//
//        player.forEachItem(item -> capturedInventory.add(new ItemMemento(item.getName(), item.getTexturePath())) );
//
//        return new PlayerMemento(
//            player.getPlayerDirection(),
//            capturedInventory,
//            (int) player.getBox().x,
//            (int) player.getBox().y,
//            LevelController.getLevelController().getCurrentLevelName()
//        );
//    }

    public void deleteProgress() throws FileNotFoundException, IllegalStateException {
        FileHandle file = Gdx.files.local("save_slot_" + this.currentSlot + ".json");
        if (!file.exists())
            throw new FileNotFoundException("File di salvataggio per lo slot " + this.currentSlot + " non trovato.");
        if (!file.delete()) // se il file non è stato eliminato
            throw new IllegalStateException("Impossibile eliminare il file di salvataggio per lo slot " + this.currentSlot + ".");
        System.out.println("File di salvataggio per lo slot " + this.currentSlot + " eliminato correttamente.");
    }

    public boolean saveAll(boolean isFromPc) {
        Player.getPlayer().saveState(); // salva lo stato del player
        LevelController.getLevelController().saveAllLevels(); // salva tutti i levelState attualmente caricati
        Progress.saveState();
        Notebook.saveState();
        Npc.saveState();

        if(!isFromPc){
            writeGameDataToDisk(); //scrive tutto su disco
        } else {
            writeGameDataToDiskPcVersion();
        }
        return true;
    }

    // saveall per il salvataggi con pc
    /*
    public boolean saveAll(boolean isFromPc) {
        // Eseguiamo comunque il salvataggio "standard" per aggiornare currentData.
        boolean result = saveAll();
        if (isFromPc) {
            // Se stiamo salvando da PC, incrementiamo la versione
            int newPcVersion = getPcSaveVersion() + 1;
            // E salviamo nel file "save_slot_n_pc_x.json"
            writeGameDataToDiskPcVersion();
        }
        return result;
    }*/

    /**
     * Restituisce l'ultimo numero di versione pc salvato per lo slot attuale,
     * oppure 0 se non esiste nessun file "save_slot_n_pc_x.json".
     */

    /*
    public int getPcSaveVersion() {
        // Esempio di nome file di salvataggio al pc: "save_slot_0_pc.json"
        String prefix = "save_slot_" + currentSlot + "_pc";
        Pattern pattern = Pattern.compile("^" + prefix + "(\\d+)\\.json$");

        FileHandle dir = Gdx.files.local(".");
        FileHandle[] files = dir.list();  // lista tutti i file nella cartella local
        int maxVersion = 0;
        for (FileHandle f : files) {
            Matcher matcher = pattern.matcher(f.name());
            if (matcher.matches()) {
                int version = Integer.parseInt(matcher.group(1));
                if (version > maxVersion) {
                    maxVersion = version;
                }
            }
        }
        return maxVersion;
    } */

    /**
     * Esegue il "rollback" caricando il file "save_slot_n_pc_version.json"
     * e imposta il currentData a quel file.
     * Ritorna true se il file esiste ed è stato caricato, false altrimenti.
     */
    public boolean restoreFromPcVersion() {
        FileHandle file = Gdx.files.local(getPcSaveFilePath());
        if (!file.exists()) {
            System.err.println("Nessun salvataggio PC trovato");
            return false;
        }
        Json json = new Json();
        currentData = json.fromJson(GameData.class, file.readString());
        System.out.println("Restore eseguito da file: " + file.path());
        // Se vuoi, puoi anche scriverlo sul file principale (dipende dalla logica di gioco)
        // In molti casi, preferiamo semplicemente caricare in memoria e non sovrascrivere
        // il save "principale" a meno che non sia richiesto.
        return true;
    }

    public void startAutoSaveTimer() {

        stopAutoSaveTimer();

        autoSaveTask = new Timer.Task() {
            @Override
            public void run() {
                if(Dinner.gameState.equals(Dinner.GameState.RUNNING)){
                    System.out.println("[AUTO-SAVE] Avvio salvataggio automatico...");

                    oldSlot = currentSlot;
                    setCurrentSlot(0);
                    saveAll(false);
                    setCurrentSlot(oldSlot);
                }
            }
        };

        Timer.schedule(autoSaveTask, 0, 5*60);
    }

    public void stopAutoSaveTimer() {
        if (this.autoSaveTask != null) {
            this.autoSaveTask.cancel(); // Cancella il task
            this.autoSaveTask = null;   // Libera il riferimento
        }
    }

    public int getCurrentSlot() {
        return this.currentSlot;
    }

}

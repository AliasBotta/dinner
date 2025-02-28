package io.github.dinner.model;

import io.github.dinner.controller.SaveController;
import io.github.dinner.model.memento.NotebookMemento;
import io.github.dinner.model.memento.ProgressMemento;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Progress {
    public static Map<String, Boolean> progressMap = new HashMap<String, Boolean>();

    public static void initializeProgressMap() {
        initializeDoorsState();
        Progress.progressMap.put("isKnifePicked", false);
        Progress.progressMap.put("talkedToAurora", false);
        Progress.progressMap.put("isCorpseVisited", false);
        Progress.progressMap.put("talkedToAurora2", false);
        Progress.progressMap.put("talkedToAdelaide", false);
        Progress.progressMap.put("talkedToVittorio", false);
        Progress.progressMap.put("isKitchenPasswordPicked", false);
        Progress.progressMap.put("isGardenFound", false);
        Progress.progressMap.put("isLibraryHintPicked1", false);
        Progress.progressMap.put("isCorpseDisappeared", false);
    }

    private static void initializeDoorsState()
    {
        Progress.progressMap.put("isRoom1Locked", true);
        Progress.progressMap.put("isClosetLocked", true);
        Progress.progressMap.put("isKitchenLocked", true);
        Progress.progressMap.put("isWestCorridorFFLocked", true);
        Progress.progressMap.put("isSecretRoomLocked", true);
        Progress.progressMap.put("isWestCorridorSFLocked", true);
        Progress.progressMap.put("isLibraryLocked", true);
        Progress.progressMap.put("isRoom4Locked", true);
        Progress.progressMap.put("isDaughterRoomLocked", true);
    }

    // Metodo per salvare lo stato attuale dei progressi in un memento
    public static void saveState() {
        SaveController.getInstance().setProgressState(new ProgressMemento(new HashMap<>(progressMap)));
    }

    // Metodo per ripristinare lo stato dei progressi da un memento
    public static void restoreState(ProgressMemento memento) {
        Progress.progressMap = new HashMap<>(memento.getProgressMap());  // Ripristina lo stato salvato
    }
}

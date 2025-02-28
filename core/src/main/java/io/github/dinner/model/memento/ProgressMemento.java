package io.github.dinner.model.memento;

import io.github.dinner.model.Progress;

import java.util.HashMap;
import java.util.Map;

public class ProgressMemento {
    private Map<String, Boolean> progressMap;

    // Costruttore senza argomenti necessario per la deserializzazione
    public ProgressMemento() {
        this.progressMap = new HashMap<>();  // Inizializzazione per evitare NullPointerException
    }

    // Costruttore per creare un memento con lo stato corrente
    public ProgressMemento(Map<String, Boolean> progressMap) {
        this.progressMap = new HashMap<>(progressMap);  // Copia difensiva
    }

    // Restituisce una copia del mappa dei progressi
    public Map<String, Boolean> getProgressMap() {
        return new HashMap<>(progressMap);
    }
}

package io.github.dinner.model.memento;

import io.github.dinner.model.Notebook;

import java.util.LinkedHashMap;
import java.util.Map;

public class NotebookMemento {
    private Map<String, Boolean> notes;

    // Costruttore senza argomenti necessario per la deserializzazione
    public NotebookMemento() {
        this.notes = new LinkedHashMap<>();  // Inizializzazione per evitare NullPointerException
    }

    // Costruttore per creare un memento con lo stato corrente delle note
    public NotebookMemento(Map<String, Boolean> notes) {
        this.notes = new LinkedHashMap<>(notes);  // Copia difensiva
    }

    // Restituisce una copia della mappa delle note
    public Map<String, Boolean> getNotes() {
        return new LinkedHashMap<>(notes);
    }
}

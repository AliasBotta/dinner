package io.github.dinner.model;

import io.github.dinner.controller.SaveController;
import io.github.dinner.model.memento.NotebookMemento;

import java.util.LinkedHashMap;
import java.util.Map;

public class Notebook {
    public static Map<String, Boolean> notes = new LinkedHashMap<>();

    // Metodo per salvare lo stato attuale delle note in un memento
    public static void saveState() {
        SaveController.getInstance().setNotebookState(new NotebookMemento(new LinkedHashMap<>(notes)));
    }

    // Metodo per ripristinare lo stato delle note da un memento
    public static void restoreState(NotebookMemento memento) {
        notes = new LinkedHashMap<>(memento.getNotes());  // Ripristina lo stato salvato
    }

    public static void initializeNotebook(){
        notes.clear();
    }
}

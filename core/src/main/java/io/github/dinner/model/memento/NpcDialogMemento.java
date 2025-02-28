package io.github.dinner.model.memento;

import java.util.HashMap;
import java.util.Map;

public class NpcDialogMemento {
    private Map<String, Integer> dialogCounts;

    public NpcDialogMemento() {
        this.dialogCounts = new HashMap<>();
    }

    public NpcDialogMemento(Map<String, Integer> currentDialogCounts) {
        this.dialogCounts = new HashMap<>(currentDialogCounts);
    }

    public Map<String, Integer> getDialogCounts() {
        return this.dialogCounts;
    }
}

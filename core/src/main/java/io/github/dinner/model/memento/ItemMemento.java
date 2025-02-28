package io.github.dinner.model.memento;

public class ItemMemento {
    private String name;
    private String texturePath;

    // Costruttore senza argomenti necessario per la deserializzazione JSON
    public ItemMemento() {
    }

    public ItemMemento(String name, String texturePath) {
        this.name = name;
        this.texturePath = texturePath;
    }

    public String getName() {
        return name;
    }

    public String getTexturePath() {
        return texturePath;
    }
}

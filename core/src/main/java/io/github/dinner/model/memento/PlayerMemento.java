package io.github.dinner.model.memento;

import io.github.dinner.model.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class PlayerMemento {
    private Player.PlayerDirection playerDirection;
    private List<ItemMemento> inventory;
    private int xPosition;
    private int yPosition;
    private String currentRoom;
    private int phase;

    public Character getGender() {
        return gender;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    private Character gender;
    private String playerName;

    // Costruttore senza argomenti necessario per la deserializzazione JSON
    public PlayerMemento() {
        this.inventory = new ArrayList<>();  // Inizializzazione per evitare NullPointerException
    }

    public PlayerMemento(Player.PlayerDirection playerDirection, List<ItemMemento> inventory, int xPosition, int yPosition, String currentRoom, int phase, Character gender, String playerName) {
        this.playerDirection = playerDirection;
        this.inventory = new ArrayList<>(inventory);  // Crea una copia difensiva
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.currentRoom = currentRoom;
        this.phase = phase;
        this.gender = gender;
        this.playerName = playerName;
    }

    public int getPhase() {
        return phase;
    }

    public Player.PlayerDirection getPlayerDirection() {
        return playerDirection;
    }

    public List<ItemMemento> getInventory() {
        return new ArrayList<>(inventory);
    }

    public int getXPosition() {
        return xPosition;
    }

    public int getYPosition() {
        return yPosition;
    }

    public String getCurrentRoom() {
        return currentRoom;
    }

    public void forEachItemMemento(Consumer<ItemMemento> action) {
        inventory.forEach(action);
    }
}

package io.github.dinner.model.memento;

import java.util.HashMap;
import java.util.Map;

public class RoomMemento {
    private Map<String, Boolean> itemPickList; //true se un item è stato preso, false altrimenti
    private Map<String, String> generalDict; // dizionario generale per effettuare delle annotazioni di qualisiasi tipo

    //costruttore senza argomenti necessario per la deserializzazione JSON
    public RoomMemento() {
        this.itemPickList = new HashMap<>();
        this.generalDict = new HashMap<>();
    }

    public RoomMemento(Map<String, Boolean> itemPickList, Map<String, String> generalDict){
        this.itemPickList = itemPickList;
        this.generalDict = generalDict;
    }

    public Map<String, Boolean> getItemPickList() {
        return itemPickList;
    }

    public void setItemPickList(Map<String, Boolean> itemDict) {
        this.itemPickList = itemDict;
    }

    public Map<String, String> getGeneralDict() {
        return generalDict;
    }

    public void setGeneralDict(Map<String, String> generalDict) {
        this.generalDict = generalDict;
    }

    public void setItemAnnotation(String itemName, Boolean isPicked) {
        this.itemPickList.put(itemName, isPicked);
    }

    public void setGeneralAnnotation(String key, String value) {
        this.generalDict.put(key, value);
    }

    public Boolean getItemAnnotation(String itemName) {
        return this.itemPickList.get(itemName);
    }

    public String getGeneralAnnotation(String key) {
        return this.generalDict.get(key);
    }
}

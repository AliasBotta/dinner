package io.github.dinner.model.states;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import io.github.dinner.controller.InteractableObjectController;
import io.github.dinner.model.memento.RoomMemento;

import java.util.HashMap;
import java.util.Map;

public abstract class LevelState {
    protected TiledMap map;
    protected OrthogonalTiledMapRenderer renderer;
    protected Music music;
    protected InteractableObjectController interactableObjectController;
    private String floorType;
    public String name;
    public Map<String, Boolean> itemPickList; //true se un item è stato preso, false altrimenti
    public Map<String, String> generalDict; // dizionario generale per effettuare delle annotazioni di qualisiasi tipo

    public LevelState() {
        this.interactableObjectController = InteractableObjectController.getInteractableObjectController();
        this.itemPickList = new HashMap<String, Boolean>();
        this.generalDict = new HashMap<String, String>();
        System.out.println("Inizializzato il levelstate di " + this);
    }

     public final static float unitScale = 1 / 18f;

    public abstract void init();

    public TiledMap getMap() {
        return this.map;
    }

    public OrthogonalTiledMapRenderer getRenderer() {
        return this.renderer;
    }

    public InteractableObjectController getInteractableObjectController() { return this.interactableObjectController; }

    public Music getMusic() {
        return this.music;
    }

    public void dispose() {
//        // Libera il TiledMap
//        if (this.map != null) {
//            this.map.dispose();
//            this.map = null;
//        }
//
//        // Libera il TiledMapRenderer
//        if (this.renderer != null) {
//            this.renderer.dispose();
//            this.renderer = null;
//        }
//
//        // Ferma e libera la musica
//        if (this.music != null) {
//            this.music.stop();
//            this.music.dispose();
//            this.music = null;
//        }
//
//        System.out.println("LevelState: Risorse liberate correttamente.");
    }


    public String getFloorType(){
        return this.floorType;
    };

    public RoomMemento createMemento() {
        return new RoomMemento(
            new HashMap<>(this.itemPickList),
            new HashMap<>(this.generalDict)
        );
    }

    // Metodo per ripristinare lo stato della Room da un RoomMemento
    public void restoreState(RoomMemento memento) {
        if (memento == null) return;  // nulla da fare
        this.itemPickList = new HashMap<>(memento.getItemPickList());
        this.generalDict = new HashMap<>(memento.getGeneralDict());
    }
}

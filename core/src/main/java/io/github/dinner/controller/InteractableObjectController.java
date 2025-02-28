package io.github.dinner.controller;

import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.MapLayer;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

import com.badlogic.gdx.math.Rectangle;
import io.github.dinner.model.Player;
import io.github.dinner.model.interactables.Item;
import io.github.dinner.model.interactables.Npc;
import io.github.dinner.model.interactables.InteractableObject;
import io.github.dinner.view.screens.GameScreen;

// controller per tutti gli oggetti interagibili, come gli item o gli NPC

public class InteractableObjectController extends InputAdapter {
    public List<Item> itemList = new ArrayList<>();
    private static Map<InteractableObject, MapObject> objectMap = new LinkedHashMap<>();
    public List<Npc> npcList = new ArrayList<>();
    public List<InteractableObject> interactableObjectList = new ArrayList<>();
    private final List<Item> itemListCopy = new ArrayList<>();
    private final List<Npc> npcListCopy = new ArrayList<>();
    private final List<InteractableObject> interactableObjectListCopy = new ArrayList<>();

    //singleton
    private static InteractableObjectController interactableObjectController;
    private InteractableObjectController(){
        super();
    }
    public static InteractableObjectController getInteractableObjectController(){
        if (interactableObjectController == null) interactableObjectController = new InteractableObjectController();

        return interactableObjectController;
    }

    // METODI FACADE, consentono di ciclare nelle liste degli oggetti interagibili specificando un consumer
    public void forEachNpc (Consumer<Npc> action) {
        npcList.forEach(action);
    }
    public void forEachItem (Consumer<Item> action) {
        itemList.forEach(action);
    }
    public void forEachInteractableObject (Consumer<InteractableObject> action) { interactableObjectList.forEach(action); }

    public boolean checkInteractions(Rectangle playerBox) {

        itemListCopy.clear();
        npcListCopy.clear();
        interactableObjectListCopy.clear();

        itemListCopy.addAll(itemList);
        npcListCopy.addAll(npcList);
        interactableObjectListCopy.addAll(interactableObjectList);

        // Unisce le due liste in un unico stream per il controllo delle interazioni
        Stream<InteractableObject> combinedStream = Stream.concat(itemListCopy.stream(), Stream.concat(npcListCopy.stream(), interactableObjectListCopy.stream()));

        // Usa un flag per memorizzare se si è verificata un'interazione
        final boolean[] interactionOccurred = {false};

        combinedStream.forEach(interactable -> {
            if (isInRange(playerBox, interactable.getBox())) {
                if (interactable instanceof Npc) {//cambiamento della direction dell'NPC all'interazione
                    Npc.Direction newDirection = null;
                    switch (Player.getPlayer().getPlayerDirection()) {
                        case NORTH: newDirection = Npc.Direction.SOUTH; break;
                        case SOUTH: newDirection = Npc.Direction.NORTH; break;
                        case WEST: newDirection = Npc.Direction.EAST; break;
                        case EAST: newDirection = Npc.Direction.WEST; break;
                    }
                    ((Npc) interactable).setDirection(newDirection);
                }

                interactable.interact(Player.getPlayer());
                interactionOccurred[0] = true;
            }
        });
        return interactionOccurred[0];
    }

    // oltre a veritificare la distanza, il metodo verifica anche che la direzione del giocatore è corretta
    public static boolean isInRange(Rectangle playerBox, Rectangle objectBox) {
        Player.PlayerDirection playerDirection = Player.getPlayer().getPlayerDirection();

        // Calcolo delle coordinate del giocatore
        float playerLeft = playerBox.x;
        float playerRight = playerBox.x + playerBox.width;
        float playerTop = playerBox.y + playerBox.height;
        float playerBottom = playerBox.y;

        // Calcolo delle coordinate dell'oggetto
        float objectLeft = objectBox.x;
        float objectRight = objectBox.x + objectBox.width;
        float objectTop = objectBox.y + objectBox.height;
        float objectBottom = objectBox.y;

        // Verifica se il giocatore guarda nella direzione corretta e il bordo del playerBox è adiacente all'objectBox
        switch (playerDirection) {
            case NORTH:
                if (playerTop == objectBottom && playerRight > objectLeft && playerLeft < objectRight) {
                    return true; // Giocatore guarda verso l'alto e il bordo superiore è adiacente al bordo inferiore dell'oggetto
                }
                break;
            case SOUTH:
                if (playerBottom == objectTop && playerRight > objectLeft && playerLeft < objectRight) {
                    return true; // Giocatore guarda verso il basso e il bordo inferiore è adiacente al bordo superiore dell'oggetto
                }
                break;
            case EAST:
                if (playerRight == objectLeft && playerBottom < objectTop && playerTop > objectBottom) {
                    return true; // Giocatore guarda verso destra e il bordo destro è adiacente al bordo sinistro dell'oggetto
                }
                break;
            case WEST:
                if (playerLeft == objectRight && playerBottom < objectTop && playerTop > objectBottom) {
                    return true; // Giocatore guarda verso sinistra e il bordo sinistro è adiacente al bordo destro dell'oggetto
                }
                break;
        }
        return false; // Non è nel range o la direzione non corrisponde
    }

    // Overloading del metodo add con tutte le classi della famiglia InteractableObject
    public void add(InteractableObject interactable) {
        if (!contains(interactable)) {
            interactableObjectList.add(interactable);
            addToCollisionLayer(interactable);
        }
    }
    public void add(Item item) {
        if (!contains(item)) {
            itemList.add(item);
            addToCollisionLayer(item);
        }
    }
    public void add(Npc npc) {
        if (!contains(npc)) {
            npcList.add(npc);
            addToCollisionLayer(npc);
        }
    }
    public void remove(InteractableObject interactableObject)
    {
        if(contains(interactableObject))
        {
            interactableObjectList.remove(interactableObject);
        }
    }

    public boolean contains(InteractableObject interactable) { return interactableObjectList.contains(interactable); }
    public boolean contains(Item item) { return itemList.contains(item); }
    public boolean contains(Npc npc) { return npcList.contains(npc); }


//    public void changeInteractableObjectList(List<InteractableObject> interactableObjectList) {//metodo che cambia le liste di oggetti interagibili al cambio stanza
//        this.interactableObjectList.forEach(InteractableObject -> removeFromCollisionLayer(InteractableObject));
//        this.interactableObjectList.clear();
//        this.interactableObjectList.addAll(interactableObjectList);
//        this.interactableObjectList.forEach(InteractableObject -> addToCollisionLayer(InteractableObject));
//    }
//    public void changeItemList(List<Item> itemList) {
//        // Pulisce la lista attuale dalle collisioni e aggiorna con i nuovi item
//        this.itemList.forEach(Item -> removeFromCollisionLayer(Item));
//        this.itemList.clear();
//        this.itemList.addAll(itemList);
//        this.itemList.forEach(Item -> addToCollisionLayer(Item));
//    }
//    public void changeNpcList(List<Npc> npcList) {
//        this.npcList.forEach(Npc -> removeFromCollisionLayer(Npc));
//        this.npcList.clear();
//        this.npcList.addAll(npcList);
//        this.npcList.forEach(Npc -> addToCollisionLayer(Npc));
//    }





    // Metodo per aggiungere l'oggetto al layer di collisione
    private static void addToCollisionLayer(InteractableObject interactable) {
        MapLayer collisionObjectLayer = GameScreen.levelController.getMap().getLayers().get("Collisioni");
        MapObject newMapObject = extractMapObject(interactable);

        boolean exists = false;
        for (MapObject existingObject : collisionObjectLayer.getObjects()) {
            if (existingObject instanceof RectangleMapObject) {
                Rectangle existingBox = ((RectangleMapObject) existingObject).getRectangle();
                Rectangle interactableBox = interactable.getBox();

                if (existingBox.equals(interactableBox)) {
                    exists = true;
                    break;
                }
            }
        }

        if (!exists) {
            collisionObjectLayer.getObjects().add(newMapObject);
            System.out.println("Aggiunta collision box per l'oggetto in posizione: " + interactable.getBox());
        } else {
            System.out.println("Collision box già presente per l'oggetto in posizione: " + interactable.getBox());
        }
    }



    public static void removeFromCollisionLayer(InteractableObject interactable) {
        MapLayer collisionObjectLayer = GameScreen.levelController.getMap().getLayers().get("Collisioni");
        Rectangle interactableBox = interactable.getBox();

        boolean removed = false;

        for (MapObject existingObject : collisionObjectLayer.getObjects()) {
            if (existingObject instanceof RectangleMapObject) {
                Rectangle existingBox = ((RectangleMapObject) existingObject).getRectangle();

                if (existingBox.equals(interactableBox)) {
                    collisionObjectLayer.getObjects().remove(existingObject);
                    removed = true;
                    System.out.println("Rimossa collision box per posizione: " + interactableBox);
                    break;
                }
            }
        }

        if (!removed) {
            System.out.println("Collision box non trovata per posizione: " + interactableBox);
        }
    }

    public static MapObject extractMapObject(InteractableObject interactable) {
        // Verifica se l'oggetto interattivo esiste già nella mappa
        MapObject existingMapObject = objectMap.get(interactable);
        if (existingMapObject != null) {
            return existingMapObject;
        } else {
            Rectangle interactableBox = interactable.getBox();
            RectangleMapObject newMapObject = new RectangleMapObject(interactableBox.x, interactableBox.y, interactableBox.width, interactableBox.height);
            objectMap.put(interactable, newMapObject);
            return newMapObject;
        }
    }

    public List<Item> getItemList() {
        return new ArrayList<>(itemList);
    }

    public List<Npc> getNpcList() {
        return new ArrayList<>(npcList);
    }

    public void remove (Item item){
        if (contains(item)){
            removeFromCollisionLayer(item);
            itemList.remove(item);
        }
    }

    // Metodo per svuotare tutte le liste e la mappa di oggetti interagibili
    public void flush() {
        itemList.clear();
        npcList.clear();
        interactableObjectList.clear();
        objectMap.clear();
    }
}

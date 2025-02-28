package io.github.dinner.view.levels.first_floor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import io.github.dinner.Dinner;
import io.github.dinner.controller.AudioController;
import io.github.dinner.controller.InteractableObjectController;
import io.github.dinner.model.Player;
import io.github.dinner.model.Progress;
import io.github.dinner.model.interactables.InteractableObject;
import io.github.dinner.model.interactables.Item;
import io.github.dinner.model.states.LevelState;
import io.github.dinner.view.screens.GameScreen;
import io.github.dinner.view.screens.TransitionScreen;

import static com.badlogic.gdx.math.MathUtils.random;

public class KitchenFFLevel extends LevelState {
    private String floorType = "wood";

    public KitchenFFLevel() {
        super();

        this.map = new TmxMapLoader().load("map/1st Floor/Kitchen/Kitchen.tmx");

        MapLayer collisionObjectLayer = this.map.getLayers().get("Collisioni");
        for (MapObject object : collisionObjectLayer.getObjects()) {
            if (object instanceof RectangleMapObject) {
                Rectangle rect = ((RectangleMapObject) object).getRectangle();
                rect.x *= unitScale;
                rect.y *= unitScale;
                rect.width *= unitScale;
                rect.height *= unitScale;
            }
        }
        this.renderer = new OrthogonalTiledMapRenderer(map, unitScale);
    }

    @Override
    public void init() {
        InteractableObject diningRoomEntrance = new InteractableObject(new Rectangle(9, 3, 2, 1), true) {
            @Override
            public void interact(Player player) {
                TransitionScreen fadeScreen = new TransitionScreen(
                    GameScreen.levelController.getCurrentLevelName(),
                    "diningRoomFFLevel",
                    (Dinner) Gdx.app.getApplicationListener(),
                    Player.getPlayer().getBox().x+2, 8,
                    Player.PlayerDirection.SOUTH
                );
                AudioController.stopAllSounds();
                AudioController.playSound("door_entrance_" + String.valueOf(random.nextInt(2)+1), false); //senza String.valueOf il gioco crasha!! (e non si sente il suono)
                ((Dinner) Gdx.app.getApplicationListener()).setScreen(fadeScreen);
            }
        };
        interactableObjectController.add(diningRoomEntrance);

        Item coltello = new Item("coltello", "un coltello", "items/knife.png",new Rectangle(14, 6, 1, 1), true){
            @Override
            public void interact(Player player) {
                super.interact(player, () -> Progress.progressMap.put("isKnifePicked", true));
            }
        };

        if(!Progress.progressMap.get("isKnifePicked")){
            interactableObjectController.add(coltello);
        } else {
            InteractableObjectController.removeFromCollisionLayer(coltello);
            interactableObjectController.remove(coltello);
        }

        /*
        Boolean isKnifePicked = this.itemPickList.get(coltello.getName());
        if (Boolean.TRUE.equals(isKnifePicked)) {
            // Non aggiungo il knife come InteractableObject,
            // perché è già raccolto
        } else {
            // Se non è raccolto, lo aggiungo
            interactableObjectController.add(coltello);
        }*/
    }


    @Override
    public void dispose() {
//        if (this.interactableObjectController != null) {
//            this.interactableObjectController.flush();
//        }
//
//        // Rilascia le risorse di LevelState
//        super.dispose();
//
//        // Log per debug
//        Gdx.app.log("BallroomFFLevel", "Risorse rilasciate correttamente.");
    }
    @Override
    public String getFloorType() {
        return this.floorType;
    }
}

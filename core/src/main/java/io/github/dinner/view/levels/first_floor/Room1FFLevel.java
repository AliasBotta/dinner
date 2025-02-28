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
import io.github.dinner.model.interactables.Npc;
import io.github.dinner.model.interactables.npcs.Adelaide;
import io.github.dinner.model.states.LevelState;
import io.github.dinner.view.screens.GameScreen;
import io.github.dinner.view.screens.TransitionScreen;

import static com.badlogic.gdx.math.MathUtils.random;

public class Room1FFLevel extends LevelState {
    private String floorType = "wood";

    public Room1FFLevel() {
        super();

        this.map = new TmxMapLoader().load("map/1st Floor/Room 1/Room1.tmx");

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

        Adelaide adelaide = new Adelaide(new Rectangle(6, 4, 1, 1.22f), true, Npc.Direction.EAST){
            @Override
            public void interact(Player player){
                GameScreen.dialogController.startDialog(Adelaide.getCurrentDialog());
            }
        };

        if(Progress.progressMap.get("isKitchenPasswordPicked")){
            interactableObjectController.add(adelaide);
        } else if (!Progress.progressMap.get("isKitchenPasswordPicked") && Progress.progressMap.get("talkedToVittorio")) {
            InteractableObjectController.removeFromCollisionLayer(adelaide);
        }

        InteractableObject eastCorridorEntrance = new InteractableObject(new Rectangle(7, 6, 1, 1), true) {
            @Override
            public void interact(Player player) {
                TransitionScreen fadeScreen = new TransitionScreen(
                    GameScreen.levelController.getCurrentLevelName(),
                    "eastCorridorFFLevel",
                    (Dinner) Gdx.app.getApplicationListener(),
                    Player.getPlayer().getBox().x, 8,
                    Player.PlayerDirection.NORTH
                );
                AudioController.stopAllSounds();
                AudioController.playSound("door_entrance_" + String.valueOf(random.nextInt(2)+1), false); //senza String.valueOf il gioco crasha!! (e non si sente il suono)
                ((Dinner) Gdx.app.getApplicationListener()).setScreen(fadeScreen);
            }
        };
        interactableObjectController.add(eastCorridorEntrance);
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

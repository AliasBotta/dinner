package io.github.dinner.view.levels.second_floor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import io.github.dinner.Dinner;
import io.github.dinner.controller.AudioController;
import io.github.dinner.model.Player;
import io.github.dinner.model.Progress;
import io.github.dinner.model.dialog.Dialogs;
import io.github.dinner.model.interactables.InteractableObject;
import io.github.dinner.model.states.LevelState;
import io.github.dinner.view.screens.GameScreen;
import io.github.dinner.view.screens.TransitionScreen;

import static com.badlogic.gdx.math.MathUtils.random;

public class WestCorridorSFLevel extends LevelState {
    private String floorType = "wood";

    public WestCorridorSFLevel() {
        super();

        this.map = new TmxMapLoader().load("map/2nd Floor/WestCorridor_2ndFloor/WestCorridor_2ndFloor.tmx");

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
        InteractableObject hallSFEntrance = new InteractableObject(new Rectangle(13, 5, 1, 2), true) {
            @Override
            public void interact(Player player) {
                TransitionScreen fadeScreen = new TransitionScreen(
                    GameScreen.levelController.getCurrentLevelName(),
                    "hallSFLevel",
                    (Dinner) Gdx.app.getApplicationListener(),
                    5, Player.getPlayer().getBox().y+2,
                    Player.PlayerDirection.EAST
                );
                AudioController.stopAllSounds();
                AudioController.playSound("door_entrance_" + String.valueOf(random.nextInt(2)+1), false); //senza String.valueOf il gioco crasha!! (e non si sente il suono)
                ((Dinner) Gdx.app.getApplicationListener()).setScreen(fadeScreen);
            }
        };
        interactableObjectController.add(hallSFEntrance);

        InteractableObject ownerRoomEntrance = new InteractableObject(new Rectangle(9, 5, 1, 1), true) {
            @Override
            public void interact(Player player) {
                TransitionScreen fadeScreen = new TransitionScreen(
                    GameScreen.levelController.getCurrentLevelName(),
                    "ownerRoomSFLevel",
                    (Dinner) Gdx.app.getApplicationListener(),
                    18, Player.getPlayer().getBox().y,
                    Player.PlayerDirection.WEST
                );
                AudioController.stopAllSounds();
                AudioController.playSound("door_entrance_" + String.valueOf(random.nextInt(2)+1), false); //senza String.valueOf il gioco crasha!! (e non si sente il suono)
                ((Dinner) Gdx.app.getApplicationListener()).setScreen(fadeScreen);
            }
        };
        interactableObjectController.add(ownerRoomEntrance);

        InteractableObject daughterRoomEntrance = new InteractableObject(new Rectangle(9, 12, 1, 1), true) {
            @Override
            public void interact(Player player) {
                if(!Progress.progressMap.get("isDaughterRoomLocked")){
                    TransitionScreen fadeScreen = new TransitionScreen(
                        GameScreen.levelController.getCurrentLevelName(),
                        "daughterRoomSFLevel",
                        (Dinner) Gdx.app.getApplicationListener(),
                        12, 3,
                        Player.PlayerDirection.WEST
                    );
                    AudioController.stopAllSounds();
                    AudioController.playSound("door_entrance_" + String.valueOf(random.nextInt(2)+1), false); //senza String.valueOf il gioco crasha!! (e non si sente il suono)
                    ((Dinner) Gdx.app.getApplicationListener()).setScreen(fadeScreen);
                } else {
                    AudioController.playSound("door_closed", false);
                    GameScreen.dialogController.startDialog(Dialogs.lockedDoorDialog);
                }
            }
        };
        interactableObjectController.add(daughterRoomEntrance);

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

package io.github.dinner.view.levels.basement;

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

public class CorridorBasementLevel extends LevelState {
    private String floorType = "wood";

    public CorridorBasementLevel() {
        super();

        this.map = new TmxMapLoader().load("map/Basement/BasementCorridor/BasementCorridor.tmx");

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

        InteractableObject upStairsLeft = new InteractableObject(new Rectangle(3, 1, 1, 1), true) {
            @Override
            public void interact(Player player) {
                TransitionScreen fadeScreen = new TransitionScreen(
                    GameScreen.levelController.getCurrentLevelName(),
                    "hallFFLevel",
                    (Dinner) Gdx.app.getApplicationListener(),
                    6, 10,
                    Player.PlayerDirection.SOUTH
                );
                AudioController.stopAllSounds();
                AudioController.playSound("stairs_up", false);
                ((Dinner) Gdx.app.getApplicationListener()).setScreen(fadeScreen);
            }
        };
        interactableObjectController.add(upStairsLeft);

        InteractableObject secretRoomEntrance = new InteractableObject(new Rectangle(7, 1, 1, 1), true) {
            @Override
            public void interact(Player player) {
                if (!Progress.progressMap.get("isSecretRoomLocked")) {
                    TransitionScreen fadeScreen = new TransitionScreen(
                        GameScreen.levelController.getCurrentLevelName(),
                        "secretRoomBasementLevel",
                        (Dinner) Gdx.app.getApplicationListener(),
                        7, 5,
                        Player.PlayerDirection.SOUTH
                    );
                    AudioController.stopAllSounds();
                    AudioController.playSound("door_entrance_" + String.valueOf(random.nextInt(2) + 1), false); //senza String.valueOf il gioco crasha!! (e non si sente il suono)
                    ((Dinner) Gdx.app.getApplicationListener()).setScreen(fadeScreen);
                }
                else
                {
                    AudioController.playSound("door_closed", false);
                    GameScreen.dialogController.startDialog(Dialogs.lockedDoorDialog);
                }
            }
        };
        interactableObjectController.add(secretRoomEntrance);

        InteractableObject upStairsRight = new InteractableObject(new Rectangle(11, 1, 1, 1), true) {
            @Override
            public void interact(Player player) {
                TransitionScreen fadeScreen = new TransitionScreen(
                    GameScreen.levelController.getCurrentLevelName(),
                    "hallFFLevel",
                    (Dinner) Gdx.app.getApplicationListener(),
                    14, 10,
                    Player.PlayerDirection.SOUTH
                );
                AudioController.stopAllSounds();
                AudioController.playSound("stairs_up", false);
                ((Dinner) Gdx.app.getApplicationListener()).setScreen(fadeScreen);
            }
        };
        interactableObjectController.add(upStairsRight);
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

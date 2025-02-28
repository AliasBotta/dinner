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
import io.github.dinner.model.interactables.npcs.Vittorio;
import io.github.dinner.model.states.LevelState;
import io.github.dinner.view.screens.GameScreen;
import io.github.dinner.view.screens.TransitionScreen;

import static com.badlogic.gdx.math.MathUtils.random;
import static io.github.dinner.view.screens.GameScreen.updateInventoryUI;

public class EastCorridorSFLevel extends LevelState {
    private String floorType = "wood";

    public EastCorridorSFLevel() {
        super();

        this.map = new TmxMapLoader().load("map/2nd Floor/EastCorridor_2ndFloor/EastCorridor_2ndFloor.tmx");

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
        this.name = "eastCorridorSFLevel";
    }

    @Override
    public void init() {
        InteractableObject hallSFEntrance = new InteractableObject(new Rectangle(1, 7, 1, 2), true) {
            @Override
            public void interact(Player player) {
                TransitionScreen fadeScreen = new TransitionScreen(
                    GameScreen.levelController.getCurrentLevelName(),
                    "hallSFLevel",
                    (Dinner) Gdx.app.getApplicationListener(),
                    15, Player.getPlayer().getBox().y,
                    Player.PlayerDirection.WEST
                );
                AudioController.stopAllSounds();
                AudioController.playSound("door_entrance_" + String.valueOf(random.nextInt(2)+1), false); //senza String.valueOf il gioco crasha!! (e non si sente il suono)
                ((Dinner) Gdx.app.getApplicationListener()).setScreen(fadeScreen);
            }
        };
        interactableObjectController.add(hallSFEntrance);

        InteractableObject libraryEntrance = new InteractableObject(new Rectangle(16, 6, 1, 1), true) {
            @Override
            public void interact(Player player) {
                if (!Progress.progressMap.get("isLibraryLocked")) {
                    if (Player.getPlayer().containsItem(Vittorio.libraryPassword)){
                        Player.getPlayer().removeItem(Vittorio.libraryPassword);
                        updateInventoryUI();
                    }
                    TransitionScreen fadeScreen = new TransitionScreen(
                        GameScreen.levelController.getCurrentLevelName(),
                        "librarySFLevel",
                        (Dinner) Gdx.app.getApplicationListener(),
                        11, Player.getPlayer().getBox().y,
                        Player.PlayerDirection.EAST
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
        interactableObjectController.add(libraryEntrance);

        InteractableObject genericBathroomEntrance = new InteractableObject(new Rectangle(12, 6, 1, 1), true) {
            @Override
            public void interact(Player player) {
                TransitionScreen fadeScreen = new TransitionScreen(
                    GameScreen.levelController.getCurrentLevelName(),
                    "genericBathroomSFLevel",
                    (Dinner) Gdx.app.getApplicationListener(),
                    17, 4,
                    Player.PlayerDirection.WEST
                );
                AudioController.stopAllSounds();
                AudioController.playSound("door_entrance_" + String.valueOf(random.nextInt(2)+1), false); //senza String.valueOf il gioco crasha!! (e non si sente il suono)
                ((Dinner) Gdx.app.getApplicationListener()).setScreen(fadeScreen);
            }
        };
        interactableObjectController.add(genericBathroomEntrance);

        InteractableObject room3Entrance = new InteractableObject(new Rectangle(12, 10, 1, 1), true) {
            @Override
            public void interact(Player player) {
                TransitionScreen fadeScreen = new TransitionScreen(
                    GameScreen.levelController.getCurrentLevelName(),
                    "room3SFLevel",
                    (Dinner) Gdx.app.getApplicationListener(),
                    18, 4,
                    Player.PlayerDirection.WEST
                );
                AudioController.stopAllSounds();
                AudioController.playSound("door_entrance_" + String.valueOf(random.nextInt(2)+1), false); //senza String.valueOf il gioco crasha!! (e non si sente il suono)
                ((Dinner) Gdx.app.getApplicationListener()).setScreen(fadeScreen);
            }
        };
        interactableObjectController.add(room3Entrance);

        InteractableObject room4Entrance = new InteractableObject(new Rectangle(12, 2, 1, 1), true) {
            @Override
            public void interact(Player player) {
                if (!Progress.progressMap.get("isRoom4Locked")) {
                    TransitionScreen fadeScreen = new TransitionScreen(
                        GameScreen.levelController.getCurrentLevelName(),
                        "room4SFLevel",
                        (Dinner) Gdx.app.getApplicationListener(),
                        18, 4,
                        Player.PlayerDirection.WEST
                    );
                    AudioController.stopAllSounds();
                    AudioController.playSound("door_entrance_" + String.valueOf(random.nextInt(2) + 1), false); //senza String.valueOf il gioco crasha!! (e non si sente il suono)
                    ((Dinner) Gdx.app.getApplicationListener()).setScreen(fadeScreen);
                } else if (Progress.progressMap.get("isRoom4Locked") && Progress.progressMap.get("talkedToAdelaide")) {
                    GameScreen.dialogController.startDialog(Vittorio.getCurrentDialog());
                } else if (Progress.progressMap.get("isRoom4Locked")) {
                    AudioController.playSound("door_closed", false);
                    GameScreen.dialogController.startDialog(Dialogs.lockedDoorDialog);
                }
            }
        };
        interactableObjectController.add(room4Entrance);
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

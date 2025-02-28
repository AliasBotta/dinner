package io.github.dinner.view.levels.first_floor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import io.github.dinner.controller.AudioController;
import io.github.dinner.model.Player;
import io.github.dinner.model.Progress;
import io.github.dinner.Dinner;
import io.github.dinner.model.dialog.Dialog;
import io.github.dinner.model.dialog.Dialogs;
import io.github.dinner.model.dialog.LinearDialogNode;
import io.github.dinner.model.interactables.InteractableObject;
import io.github.dinner.model.states.LevelState;
import io.github.dinner.view.screens.GameScreen;
import io.github.dinner.view.screens.TransitionScreen;

import static com.badlogic.gdx.math.MathUtils.random;

public class HallFFLevel extends LevelState {

    private String floorType = "wood";

    public HallFFLevel() {
        super();

        this.map = new TmxMapLoader().load("map/1st Floor/Hall 1st Floor/Hall.tmx");

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
        this.name = "hallFFLevel";
        System.out.println("Inizializzato il livello");
    }

    @Override
    public void init() {


        InteractableObject eastCorridorEntrance = new InteractableObject(new Rectangle(16, 7, 1, 3), true) {
            @Override
            public void interact(Player player) {
                TransitionScreen fadeScreen = new TransitionScreen(
                    GameScreen.levelController.getCurrentLevelName(),
                    "eastCorridorFFLevel",
                    (Dinner) Gdx.app.getApplicationListener(),
                    4, Player.getPlayer().getBox().y == 7 ? 9 : 10,
                    Player.PlayerDirection.EAST
                );
                AudioController.stopAllSounds();
                AudioController.playSound("door_entrance_" + String.valueOf(random.nextInt(2)+1), false); //senza String.valueOf il gioco crasha!! (e non si sente il suono)
                ((Dinner) Gdx.app.getApplicationListener()).setScreen(fadeScreen);
            }
        };
        interactableObjectController.add(eastCorridorEntrance);

        InteractableObject westCorridorEntrance = new InteractableObject(new Rectangle(4, 7, 1, 2), true) {
            @Override
            public void interact(Player player) {
                if (!Progress.progressMap.get("isWestCorridorFFLocked")) {
                    TransitionScreen fadeScreen = new TransitionScreen(
                        GameScreen.levelController.getCurrentLevelName(),
                        "westCorridorFFLevel",
                        (Dinner) Gdx.app.getApplicationListener(),
                        18, Player.getPlayer().getBox().y == 7 ? 5 : 6,
                        Player.PlayerDirection.WEST
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
        interactableObjectController.add(westCorridorEntrance);

        InteractableObject upStairs = new InteractableObject(new Rectangle(9, 12, 3, 1), true) {
            @Override
            public void interact(Player player) {
                TransitionScreen fadeScreen = new TransitionScreen(
                    GameScreen.levelController.getCurrentLevelName(),
                    "hallSFLevel",
                    (Dinner) Gdx.app.getApplicationListener(),
                    Player.getPlayer().getBox().x, 12,
                    Player.PlayerDirection.NORTH
                );
                AudioController.stopAllSounds();
                AudioController.playSound("stairs_up", false);
                ((Dinner) Gdx.app.getApplicationListener()).setScreen(fadeScreen);
            }
        };
        interactableObjectController.add(upStairs);

        InteractableObject downStairsLeft = new InteractableObject(new Rectangle(6, 11, 1, 1), true) {
            @Override
            public void interact(Player player) {
                TransitionScreen fadeScreen = new TransitionScreen(
                    GameScreen.levelController.getCurrentLevelName(),
                    "corridorBasementLevel",
                    (Dinner) Gdx.app.getApplicationListener(),
                    3, 2,
                    Player.PlayerDirection.NORTH
                );
                AudioController.stopAllSounds();
                AudioController.playSound("stairs_up", false);
                ((Dinner) Gdx.app.getApplicationListener()).setScreen(fadeScreen);
            }
        };
        interactableObjectController.add(downStairsLeft);

        InteractableObject downStairsRight = new InteractableObject(new Rectangle(14, 11, 1, 1), true) {
            @Override
            public void interact(Player player) {
                TransitionScreen fadeScreen = new TransitionScreen(
                    GameScreen.levelController.getCurrentLevelName(),
                    "corridorBasementLevel",
                    (Dinner) Gdx.app.getApplicationListener(),
                    11, 2,
                    Player.PlayerDirection.NORTH
                );
                AudioController.stopAllSounds();
                AudioController.playSound("stairs_up", false);
                ((Dinner) Gdx.app.getApplicationListener()).setScreen(fadeScreen);
            }
        };
        interactableObjectController.add(downStairsRight);

        InteractableObject homeownerStatue = new InteractableObject(new Rectangle(10, 8, 1, 1), true) {

            @Override
            public void interact(Player player) {  // Correggi il parametro qui
                Dialog dialog = new Dialog();
                dialog.addNode(new LinearDialogNode(null, "Una statua di Giovanni in piena hall... un po' pacchiano", 0));

                GameScreen.dialogController.startDialog(dialog);
            }
        };

        interactableObjectController.add(homeownerStatue);

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

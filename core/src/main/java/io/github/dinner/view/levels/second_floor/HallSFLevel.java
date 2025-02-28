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
import io.github.dinner.controller.InteractableObjectController;
import io.github.dinner.model.Player;
import io.github.dinner.model.Progress;
import io.github.dinner.model.dialog.Dialogs;
import io.github.dinner.model.interactables.InteractableObject;
import io.github.dinner.model.interactables.Npc;
import io.github.dinner.model.interactables.npcs.Aurora;
import io.github.dinner.model.states.LevelState;
import io.github.dinner.view.screens.GameScreen;
import io.github.dinner.view.screens.TransitionScreen;

import static com.badlogic.gdx.math.MathUtils.random;
import static io.github.dinner.model.interactables.Npc.getDialogCount;
import static io.github.dinner.model.interactables.Npc.setDialogCount;

public class HallSFLevel extends LevelState {
    private String floorType = "wood";

    public HallSFLevel() {
        super();

        this.map = new TmxMapLoader().load("map/2nd Floor/Hall 2nd Floor/Hall2.tmx");

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
        this.name = "hallSFLevel";
    }

    @Override
    public void init() {

        Aurora aurora = new Aurora(new Rectangle(10, 13, 1, 1.22f), true, Npc.Direction.NORTH){
            @Override
            public void interact(Player player) {
                GameScreen.dialogController.startDialog(Aurora.getCurrentDialog());
            }
        };

        if(Progress.progressMap.get("isCorpseVisited") && GameScreen.phase == 0 && !Progress.progressMap.get("talkedToAurora2") && !Progress.progressMap.get("talkedToAdelaide")) {
            interactableObjectController.add(aurora);
        } else if (Progress.progressMap.get("talkedToAurora2") || Progress.progressMap.get("talkedToAdelaide")) {
            InteractableObjectController.removeFromCollisionLayer(aurora);
            interactableObjectController.remove(aurora);
        }

        InteractableObject downStairs = new InteractableObject(new Rectangle(9, 11, 3, 1), true) {
            @Override
            public void interact(Player player) {

                if(Progress.progressMap.get("talkedToAurora2") && getDialogCount(Aurora.name) < 4) //devo essere sicuro di poter scendere (quindi di aver già parlato con Aurora) per poter settare l'nDialog di Aurora a 4
                    setDialogCount(Aurora.name, 4);

                TransitionScreen fadeScreen = new TransitionScreen(
                    GameScreen.levelController.getCurrentLevelName(),
                    "hallFFLevel",
                    (Dinner) Gdx.app.getApplicationListener(),
                    Player.getPlayer().getBox().x, 11,
                    Player.PlayerDirection.SOUTH
                );
                AudioController.stopAllSounds();
                AudioController.playSound("stairs_down", false);
                ((Dinner) Gdx.app.getApplicationListener()).setScreen(fadeScreen);
            }
        };
        interactableObjectController.add(downStairs);

        InteractableObject gardenEntrance = new InteractableObject(new Rectangle(10, 16, 1, 1), true) {
            @Override
            public void interact(Player player) {
                TransitionScreen fadeScreen = new TransitionScreen(
                    GameScreen.levelController.getCurrentLevelName(),
                    !Progress.progressMap.get("isKnifePicked") ? "gardenSFLevel" : "gardenSFNoCorpseLevel",
                    (Dinner) Gdx.app.getApplicationListener(),
                    29, 1,
                    Player.PlayerDirection.NORTH
                );
                AudioController.stopAllSounds();
                AudioController.playSound("stairs_down", false);
                AudioController.playSound("door_entrance_" + String.valueOf(random.nextInt(2)+1), false); //senza String.valueOf il gioco crasha!! (e non si sente il suono)
                ((Dinner) Gdx.app.getApplicationListener()).setScreen(fadeScreen);
                GameScreen.smoothZoom(GameScreen.GARDEN_ZOOM_FACTOR, 2f, Player.getPlayer().getBox().x, Player.getPlayer().getBox().y);
            }
        };
        interactableObjectController.add(gardenEntrance);

        InteractableObject eastCorridorEntrance = new InteractableObject(new Rectangle(16, 7, 1, 2 ), true) {
            @Override
            public void interact(Player player) {
                    TransitionScreen fadeScreen = new TransitionScreen(
                        GameScreen.levelController.getCurrentLevelName(),
                        "eastCorridorSFLevel",
                        (Dinner) Gdx.app.getApplicationListener(),
                        2, Player.getPlayer().getBox().y,
                        Player.PlayerDirection.EAST
                    );
                    AudioController.stopAllSounds();
                    AudioController.playSound("door_entrance_" + String.valueOf(random.nextInt(2)+1), false); //senza String.valueOf il gioco crasha!! (e non si sente il suono)
                    ((Dinner) Gdx.app.getApplicationListener()).setScreen(fadeScreen);
            }
        };
        interactableObjectController.add(eastCorridorEntrance);

        InteractableObject westCorridorEntrance = new InteractableObject(new Rectangle(4, 7, 1, 2 ), true) {
            @Override
            public void interact(Player player) {
                if (!Progress.progressMap.get("isWestCorridorSFLocked")) {
                    TransitionScreen fadeScreen = new TransitionScreen(
                        GameScreen.levelController.getCurrentLevelName(),
                        "westCorridorSFLevel",
                        (Dinner) Gdx.app.getApplicationListener(),
                        12, Player.getPlayer().getBox().y - 2,
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

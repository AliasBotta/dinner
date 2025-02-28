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
import io.github.dinner.model.dialog.Dialogs;
import io.github.dinner.model.interactables.InteractableObject;
import io.github.dinner.model.interactables.Npc;
import io.github.dinner.model.interactables.npcs.Aurora;
import io.github.dinner.model.states.LevelState;
import io.github.dinner.view.screens.GameScreen;
import io.github.dinner.view.screens.TransitionScreen;

import static com.badlogic.gdx.math.MathUtils.random;

public class EastCorridorFFLevel extends LevelState {
    private String floorType = "wood";
    public static Aurora aurora;

    public EastCorridorFFLevel() {
        super();

        this.map = new TmxMapLoader().load("map/1st Floor/EastCorridor_1stFloor/EastCorridor_1stFloor.tmx");

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

        /*Item knife2 = new Item("knife2", "un coltello", "characters/Knife.png",new Rectangle(5, 9, 1, 1), true);
        Boolean isKnife2Picked = this.itemPickList.get(knife2.getName());
        if (Boolean.TRUE.equals(isKnife2Picked)) {
            // Non aggiungo il knife come InteractableObject,
            // perché è già raccolto
        } else {
            // Se non è raccolto, lo aggiungo
            interactableObjectController.add(knife2);
        }*/

        if(Progress.progressMap.get("isCorpseVisited")){
            Progress.progressMap.put("isRoom1Locked", false);
        }

        aurora = new Aurora(new Rectangle(13, 10, 1, 1.22f), true, Npc.Direction.SOUTH){
            @Override
            public void interact(Player player) {
                GameScreen.dialogController.startDialog(Aurora.getCurrentDialog());
            }
        };

        if(!Progress.progressMap.get("talkedToAurora") && !Progress.progressMap.get("isGardenFound") && GameScreen.phase == 0) {
            interactableObjectController.add(aurora);
        } else if (Progress.progressMap.get("talkedToAurora") || Progress.progressMap.get("isGardenFound")) {
            InteractableObjectController.removeFromCollisionLayer(aurora);
            interactableObjectController.remove(aurora);
        }


        InteractableObject hallFFEntrance = new InteractableObject(new Rectangle(3, 9, 1, 2), true) {
            @Override
            public void interact(Player player) {
                TransitionScreen fadeScreen = new TransitionScreen(
                    GameScreen.levelController.getCurrentLevelName(),
                    "hallFFLevel",
                    (Dinner) Gdx.app.getApplicationListener(),
                    15, Player.getPlayer().getBox().y == 9 ? 7 : 8,
                    Player.PlayerDirection.WEST
                );
                AudioController.stopAllSounds();
                AudioController.playSound("door_entrance_" + String.valueOf(random.nextInt(2)+1), false); //senza String.valueOf il gioco crasha!! (e non si sente il suono)
                ((Dinner) Gdx.app.getApplicationListener()).setScreen(fadeScreen);
            }
        };
        interactableObjectController.add(hallFFEntrance);

        //11,11
        InteractableObject diningRoomFFEntrance = new InteractableObject(new Rectangle(11, 12, 2, 1), true) {
            @Override
            public void interact(Player player) {
                TransitionScreen fadeScreen = new TransitionScreen(
                    GameScreen.levelController.getCurrentLevelName(),
                    "diningRoomFFLevel",
                    (Dinner) Gdx.app.getApplicationListener(),
                    Player.getPlayer().getBox().x, 2,
                    Player.PlayerDirection.NORTH
                );
                AudioController.stopAllSounds();
                AudioController.playSound("door_entrance_" + String.valueOf(random.nextInt(2)+1), false); //senza String.valueOf il gioco crasha!! (e non si sente il suono)
                ((Dinner) Gdx.app.getApplicationListener()).setScreen(fadeScreen);
            }
        };
        interactableObjectController.add(diningRoomFFEntrance);

        InteractableObject room1Entrance = new InteractableObject(new Rectangle(7, 7, 1, 1), true) {
            @Override
            public void interact(Player player) {
                if(!Progress.progressMap.get("isRoom1Locked")) {
                    TransitionScreen fadeScreen = new TransitionScreen(
                        GameScreen.levelController.getCurrentLevelName(),
                        "room1FFLevel",
                        (Dinner) Gdx.app.getApplicationListener(),
                        Player.getPlayer().getBox().x, 5,
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
        interactableObjectController.add(room1Entrance);

        InteractableObject room2Entrance = new InteractableObject(new Rectangle(13, 7, 1, 1), true) {
            @Override
            public void interact(Player player) {
                TransitionScreen fadeScreen = new TransitionScreen(
                    GameScreen.levelController.getCurrentLevelName(),
                    "room2FFLevel",
                    (Dinner) Gdx.app.getApplicationListener(),
                    7, 5,
                    Player.PlayerDirection.SOUTH
                );
                AudioController.stopAllSounds();
                AudioController.playSound("door_entrance_" + String.valueOf(random.nextInt(2)+1), false); //senza String.valueOf il gioco crasha!! (e non si sente il suono)
                ((Dinner) Gdx.app.getApplicationListener()).setScreen(fadeScreen);
            }
        };
        interactableObjectController.add(room2Entrance);

        InteractableObject bathroomEntrance = new InteractableObject(new Rectangle(19, 7, 1, 1), true) {
            @Override
            public void interact(Player player) {
                TransitionScreen fadeScreen = new TransitionScreen(
                    GameScreen.levelController.getCurrentLevelName(),
                    "genericBathroomFFLevel",
                    (Dinner) Gdx.app.getApplicationListener(),
                    7, 5,
                    Player.PlayerDirection.SOUTH
                );
                AudioController.stopAllSounds();
                AudioController.playSound("door_entrance_" + String.valueOf(random.nextInt(2)+1), false); //senza String.valueOf il gioco crasha!! (e non si sente il suono)
                ((Dinner) Gdx.app.getApplicationListener()).setScreen(fadeScreen);
            }
        };
        interactableObjectController.add(bathroomEntrance);

        InteractableObject closetEntrance = new InteractableObject(new Rectangle(21, 17, 1, 1), true) {
            @Override
            public void interact(Player player) {
                if(!Progress.progressMap.get("isClosetLocked")) {
                    TransitionScreen fadeScreen = new TransitionScreen(
                        GameScreen.levelController.getCurrentLevelName(),
                        "closetFFLevel",
                        (Dinner) Gdx.app.getApplicationListener(),
                        7, 2,
                        Player.PlayerDirection.NORTH
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
        interactableObjectController.add(closetEntrance);
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

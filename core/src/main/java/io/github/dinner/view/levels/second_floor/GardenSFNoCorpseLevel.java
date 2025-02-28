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
import io.github.dinner.model.dialog.Dialog;
import io.github.dinner.model.dialog.LinearDialogNode;
import io.github.dinner.model.interactables.InteractableObject;
import io.github.dinner.model.interactables.Npc;
import io.github.dinner.model.interactables.npcs.Carlo;
import io.github.dinner.model.states.LevelState;
import io.github.dinner.view.screens.GameScreen;
import io.github.dinner.view.screens.TransitionScreen;

import static com.badlogic.gdx.math.MathUtils.random;

public class GardenSFNoCorpseLevel extends LevelState {

    private String floorType = "grass";

    public GardenSFNoCorpseLevel() {
        super();

        this.map = new TmxMapLoader().load("map/2nd Floor/Giardino/GardenAfter.tmx");

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
        this.name = "gardenSFNoCorpseLevel";
    }

    @Override
    public void init(){

        GameScreen.zoomFactor = GameScreen.GARDEN_ZOOM_FACTOR;

        Dialog dialog = new Dialog();

        InteractableObject nonImpiccato = new InteractableObject(new Rectangle(28, 22, 1, 1), true) {
            @Override
            public void interact(Player player) {
                GameScreen.dialogController.startDialog(dialog);
            }
        };

        if(!Progress.progressMap.get("isCorpseDisappeared")){
            interactableObjectController.add(nonImpiccato);
            InteractableObjectController.removeFromCollisionLayer(nonImpiccato);
        } else if(interactableObjectController.contains(nonImpiccato)){
            interactableObjectController.remove(nonImpiccato);
        }

        dialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(),"Signor Giovanni, ma...", 0, () -> {
            AudioController.stopAllSounds();
            AudioController.playSound("bush_rustling", false);
        }).setPointer(1));

        dialog.addNode(new LinearDialogNode(null, "* si sente un rumore fra i cespugli *", 1).setPointer(2));
        dialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "Cos'è questo rumore?!", 2).setPointer(3));
        dialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "Sembra provenire da est...", 3, () -> {
            interactableObjectController.remove(nonImpiccato);
            Progress.progressMap.put("isCorpseDisappeared", true); //significa che il giocatore ha interagito con il "non impiccato"
            Carlo carlo = new Carlo(new Rectangle(39, 15, 1, 1.22f), true, Npc.Direction.EAST){
                @Override
                public void interact(Player player) {
                    GameScreen.dialogController.startDialog(Carlo.getCurrentDialog());
                }
            };
            interactableObjectController.add(carlo);
        }));

        if(Progress.progressMap.get("isCorpseDisappeared")){
            Carlo carlo = new Carlo(new Rectangle(39, 15, 1, 1.22f), true, Npc.Direction.EAST){
                @Override
                public void interact(Player player) {
                    GameScreen.dialogController.startDialog(Carlo.getCurrentDialog());
                }
            };
            interactableObjectController.add(carlo);
        }


        AudioController.stopMusic();
        AudioController.playMusic("garden_ambience", true);
        InteractableObject hallSFEntrance = new InteractableObject(new Rectangle(29, 0, 1, 1), true) {
            @Override
            public void interact(Player player) {
                TransitionScreen fadeScreen = new TransitionScreen(
                    GameScreen.levelController.getCurrentLevelName(),
                    "hallSFLevel",
                    (Dinner) Gdx.app.getApplicationListener(),
                    10, 15,
                    Player.PlayerDirection.SOUTH
                );
                ((Dinner) Gdx.app.getApplicationListener()).setScreen(fadeScreen);
                AudioController.stopMusic();
                AudioController.stopAllSounds();
                AudioController.playSound("stairs_up", false);
                AudioController.playSound("door_entrance_" + String.valueOf(random.nextInt(2)+1), false); //senza String.valueOf il gioco crasha!! (e non si sente il suono)
                AudioController.stopMusic();
                AudioController.playMusic("home", true);
                GameScreen.smoothZoom(GameScreen.DEFAULT_ZOOM_FACTOR, 2f, Player.getPlayer().getBox().x, Player.getPlayer().getBox().y);
            }
        };
        interactableObjectController.add(hallSFEntrance);
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

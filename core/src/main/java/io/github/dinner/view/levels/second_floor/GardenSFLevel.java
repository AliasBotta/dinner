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
import io.github.dinner.model.Notebook;
import io.github.dinner.model.Player;
import io.github.dinner.model.Progress;
import io.github.dinner.model.dialog.Dialog;
import io.github.dinner.model.dialog.LinearDialogNode;
import io.github.dinner.model.interactables.InteractableObject;
import io.github.dinner.model.interactables.npcs.Aurora;
import io.github.dinner.model.states.LevelState;
import io.github.dinner.view.screens.GameScreen;
import io.github.dinner.view.screens.TransitionScreen;

import static com.badlogic.gdx.math.MathUtils.random;
import static io.github.dinner.model.interactables.Npc.getDialogCount;
import static io.github.dinner.model.interactables.Npc.setDialogCount;

public class GardenSFLevel extends LevelState {

    private String floorType = "grass";
    private static InteractableObject impiccato;

    public GardenSFLevel() {
        super();

        this.map = new TmxMapLoader().load("map/2nd Floor/Giardino/Garden.tmx");

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
        this.name = "gardenSFLevel";
    }

    @Override
    public void init() {

        GameScreen.zoomFactor = GameScreen.GARDEN_ZOOM_FACTOR;

        Progress.progressMap.put("isGardenFound", true);

        Dialog dialog = new Dialog();
        dialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(),"SIGNOR GIOVANNI!!", 0).setPointer(1));
        dialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "Ma... è morto! Maledizione, sapevo che qualcosa potesse andare storto!", 1).setPointer(2));
        dialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "...posso ancora salvare la mia carriera, devo tirare giù il suo corpo per poterlo esaminare.", 2).setPointer(3));
        dialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "Ma certo! In cucina troverò sicuramente un coltello per tagliare il cappio!", 3, () -> {
            Progress.progressMap.put("isCorpseVisited", true);
            interactableObjectController.remove(impiccato);
            Notebook.notes.put("Il signor Giovanni è morto. Se gioco bene le mie carte e trovo il colpevole posso salvare la mia carriera!", true);
        }));

        impiccato = new InteractableObject(new Rectangle(28, 22, 1, 1), true) {
            @Override
            public void interact(Player player) {
                GameScreen.dialogController.startDialog(dialog);
            }
        };


        if(!Progress.progressMap.get("isCorpseVisited")){
            interactableObjectController.add(impiccato);
            InteractableObjectController.removeFromCollisionLayer(impiccato);
        } else if (interactableObjectController.contains(impiccato)){
            interactableObjectController.remove(impiccato);
        }

        AudioController.stopMusic();
        AudioController.playMusic("garden_ambience", true);
        InteractableObject hallSFEntrance = new InteractableObject(new Rectangle(29, 0, 1, 1), true) {
            @Override
            public void interact(Player player) {

                if(getDialogCount(Aurora.name) < 3)
                    setDialogCount(Aurora.name, 3);

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

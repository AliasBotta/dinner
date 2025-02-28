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
import io.github.dinner.model.states.LevelState;
import io.github.dinner.view.screens.GameScreen;
import io.github.dinner.view.screens.TransitionScreen;

import static com.badlogic.gdx.math.MathUtils.random;

public class LibrarySFLevel extends LevelState {
    private String floorType = "wood";

    public LibrarySFLevel() {
        super();

        this.map = new TmxMapLoader().load("map/2nd Floor/Library/Library.tmx");

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
        this.name = "librarySFLevel";
    }

    @Override
    public void init() {

        Dialog turingQuiz = new Dialog();
        turingQuiz.addNode(new LinearDialogNode("Storia dell'informatica, vol. 1", "...Il matematico, logico e crittografo britannico Alan Turing, nato il 23 giugno 1912 e morto il 7 giugno 1954, è considerato uno dei padri fondatori dell'informatica.", 0).setPointer(1));
        turingQuiz.addNode(new LinearDialogNode("Storia dell'informatica, vol. 1", "La causa di morte fu presumibilmente il suicidio. Turing fu infatti sottoposto a castrazione chimica a causa della sua omosessualità, ai tempi purtroppo non accettata e considerata illegale.", 1).setPointer(2));
        turingQuiz.addNode(new LinearDialogNode("Storia dell'informatica, vol. 1", "Diede un enorme contributo nella sconfitta della Germania nazista durante la seconda guerra mondiale, decifrando i codici usati nelle comunicazioni tedesche.", 2, () -> {
            if(!Progress.progressMap.get("isLibraryHintPicked1")){
                Notebook.notes.put("In biblioteca ho letto che Alan Turing è nato il 23 giugno 1912. Un po' di cultura generale non fa mai male.", false);
            }
            Progress.progressMap.put("isLibraryHintPicked1", true);
        }));

        InteractableObject hint = new InteractableObject(new Rectangle(16, 10, 1, 1), true) {
            @Override
            public void interact(Player player) {
                GameScreen.dialogController.startDialog(turingQuiz);
            }
        };


        if(!Progress.progressMap.get("isLibraryHintPicked1") && !Progress.progressMap.get("talkedToVittorio")) {
            interactableObjectController.add(hint);
            InteractableObjectController.removeFromCollisionLayer(hint);
        } else if (interactableObjectController.contains(hint)){
            interactableObjectController.remove(hint);
        }

        InteractableObject eastCorridorEntrance = new InteractableObject(new Rectangle(10, 6, 1, 1), true) {
            @Override
            public void interact(Player player) {
                TransitionScreen fadeScreen = new TransitionScreen(
                    GameScreen.levelController.getCurrentLevelName(),
                    "eastCorridorSFLevel",
                    (Dinner) Gdx.app.getApplicationListener(),
                    15, Player.getPlayer().getBox().y,
                    Player.PlayerDirection.WEST
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

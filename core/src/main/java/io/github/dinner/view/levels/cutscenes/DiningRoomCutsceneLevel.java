package io.github.dinner.view.levels.cutscenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Timer;
import io.github.dinner.Dinner;
import io.github.dinner.controller.AudioController;
import io.github.dinner.model.Player;
import io.github.dinner.model.dialog.Dialog;
import io.github.dinner.model.dialog.LinearDialogNode;
import io.github.dinner.model.interactables.Npc;
import io.github.dinner.model.interactables.npcs.*;
import io.github.dinner.model.states.LevelState;
import io.github.dinner.view.screens.GameScreen;
import io.github.dinner.view.screens.TransitionScreen;

public class DiningRoomCutsceneLevel extends LevelState {

    private String floorType = "wood";

    public DiningRoomCutsceneLevel() {
        super();

        this.map = new TmxMapLoader().load("map/1st Floor/Dining Room/DiningRoom_CUTSCENE_BEFORE.tmx");

        this.renderer = new OrthogonalTiledMapRenderer(map, unitScale);
        this.name = "diningRoomCutsceneLevel";
        System.out.println("Inizializzato il livello");
    }

    @Override
    public void init() {
        Player.getPlayer().setPosition(9, 6, Player.PlayerDirection.SOUTH);

        Npc giovanni = new Giovanni(new Rectangle(6, 5, 1, 1.22f), true, Npc.Direction.EAST);
        interactableObjectController.add(giovanni);

        Npc vittorio = new Vittorio(new Rectangle(11, 6, 1, 1.22f), true, Npc.Direction.SOUTH);
        interactableObjectController.add(vittorio);

        Npc aurora = new Aurora(new Rectangle(8, 6, 1, 1.22f), true, Npc.Direction.SOUTH);
        interactableObjectController.add(aurora);

        Npc adelaide = new Adelaide(new Rectangle(12, 6, 1, 1.22f), true, Npc.Direction.SOUTH);
        interactableObjectController.add(adelaide);

        Npc eleonora = new Eleonora(new Rectangle(14, 6, 1, 1.22f), true, Npc.Direction.SOUTH);
        interactableObjectController.add(eleonora);

        System.out.println("Lista di item:" + itemPickList);

        GameScreen.camera.position.set(14, 4, 0);
        GameScreen.smoothZoom(0.3f, 5f, 6, 5);

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                AudioController.playMusic("home", true);
                Dialog dinnerDialog = new Dialog();
                dinnerDialog.addNode(new LinearDialogNode(Giovanni.name, "Benvenuti nella mia modesta proprietà!", 0).setPointer(1));
                dinnerDialog.addNode(new LinearDialogNode(Giovanni.name, "Ognuno di voi è stato invitato per gustare una deliziosa cena toscana in mezzo alle nostre meravigliose campagne", 1).setPointer(2));
                dinnerDialog.addNode(new LinearDialogNode(Giovanni.name, "...", 2).setPointer(3));
                dinnerDialog.addNode(new LinearDialogNode(Giovanni.name, "Certo, saprete bene che non siete qui solo per mangiare, abbiamo delle cose da discutere...",3).setPointer(4));
                dinnerDialog.addNode(new LinearDialogNode(Giovanni.name, "Non temete, avrete le vostre risposte a tempo debito, per il momento non roviniamoci l'appetito.", 4).setPointer(5));
                dinnerDialog.addNode(new LinearDialogNode(Giovanni.name, "Abbuffiamoci!", 5, () -> {
                    TransitionScreen fadeScreen = new TransitionScreen(
                        null,//GameScreen.levelController.getCurrentLevelName(),
                        "eatedDiningRoomCutsceneLevel",
                        (Dinner) Gdx.app.getApplicationListener(),
                        9, 6,
                        Player.PlayerDirection.SOUTH,
                        (GameScreen) ((Dinner) Gdx.app.getApplicationListener()).getScreen(),
                        0,
                        0.04f
                    );
                    ((Dinner) Gdx.app.getApplicationListener()).setScreen(fadeScreen);
                }));
                GameScreen.dialogController.startDialog(dinnerDialog);
            }
        }, 5);

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

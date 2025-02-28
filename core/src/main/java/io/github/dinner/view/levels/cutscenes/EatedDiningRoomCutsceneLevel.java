package io.github.dinner.view.levels.cutscenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Timer;
import io.github.dinner.Dinner;
import io.github.dinner.model.Player;
import io.github.dinner.model.dialog.Dialog;
import io.github.dinner.model.dialog.LinearDialogNode;
import io.github.dinner.model.interactables.Npc;
import io.github.dinner.model.interactables.npcs.*;
import io.github.dinner.model.states.LevelState;
import io.github.dinner.view.screens.GameScreen;
import io.github.dinner.view.screens.TransitionScreen;

public class EatedDiningRoomCutsceneLevel extends LevelState {

    private String floorType = "wood";

    public EatedDiningRoomCutsceneLevel() {
        super();

        this.map = new TmxMapLoader().load("map/1st Floor/Dining Room/DiningRoom_CUTSCENE_AFTER.tmx");

        this.renderer = new OrthogonalTiledMapRenderer(map, unitScale);
        this.name = "eatedDiningRoomCutsceneLevel";
        System.out.println("Inizializzato il livello");
    }

    @Override
    public void init(){
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

        Dialog dinnerDialog = new Dialog();
        Dialog dinnerDialog2 = new Dialog();

        dinnerDialog.addNode(new LinearDialogNode(Giovanni.name, "Oh, bene! Spero sia stato tutto di vostro gradimento.", 0, () -> {
            GameScreen.smoothZoom(0.3f, 1, 8, 6);
            aurora.setDirection(Npc.Direction.WEST);
        }).setPointer(1));
        dinnerDialog.addNode(new LinearDialogNode(Aurora.name, "Papà, questa è stata la pappa migliore di sempre!", 1, () -> {
            GameScreen.smoothZoom(0.3f, 1f, 14, 6);

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    eleonora.setDirection(Npc.Direction.EAST);
                }
            }, 1);

            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    eleonora.setDirection(Npc.Direction.WEST);
                    GameScreen.dialogController.startDialog(dinnerDialog2);
                }
            }, 2);
        }));

        dinnerDialog2.addNode(new LinearDialogNode(Eleonora.name, "Tutto buonissimo Giovanni, ricorda di fare i complimenti da parte mia a Carlo per la splendida cena.", 0, () -> {
            GameScreen.smoothZoom(0.3f, 1f, 11, 6);
            vittorio.setDirection(Npc.Direction.WEST);
        }).setPointer(1));
        dinnerDialog2.addNode(new LinearDialogNode(Vittorio.name, "Sì sì, tutto molto buono... andiamo al dunque ora.", 1, () -> {
            GameScreen.smoothZoom(0.3f, 1f, 9, 6);
        }).setPointer(2));
        dinnerDialog2.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "???", 2, () -> {
            GameScreen.smoothZoom(0.3f, 1f, 12, 6);
            adelaide.setDirection(Npc.Direction.WEST);
        }).setPointer(3));
        dinnerDialog2.addNode(new LinearDialogNode(Adelaide.name, "Sì Giovanni... * burp * oops scusate!! Ehm... tutto molto buono Giovanni ma, come dice il nostro commensale barbuto, andiamo al sodo.", 3, () -> {
            GameScreen.smoothZoom(0.3f, 1f, 6, 5);
        }).setPointer(4));
        dinnerDialog2.addNode(new LinearDialogNode(Giovanni.name, "Non sono mica un mostro! C'è tempo per \"arrivare al sodo\", ora sarete stanchi, alcuni di voi hanno fatto diversi chilometri per arrivare fin qui.", 4).setPointer(5));
        dinnerDialog2.addNode(new LinearDialogNode(Giovanni.name, "Non vi preoccupate per la tavola, ci penserà Carlo. Potete andare nelle vostre stanze, domani parleremo con calma di tutto quanto.", 5, () -> {
            GameScreen.smoothZoom(0.3f, 1f, 9, 6);
        }).setPointer(6));
        dinnerDialog2.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "* Al telefono non si era parlato di pernottamento... *", 6).setPointer(7));
        dinnerDialog2.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "* ...ma se serve per sapere di più sul caso, dormo anche sotto i ponti *", 7, () -> {
            Timer.schedule(new Timer.Task() {
                @Override
                public void run(){
                    TransitionScreen transition = new TransitionScreen(null,
                        "room1FFCutsceneLevel",
                        (Dinner) Gdx.app.getApplicationListener(),
                        5, 4,
                        Player.PlayerDirection.EAST,
                        (GameScreen) ((Dinner) Gdx.app.getApplicationListener()).getScreen(),
                        0,
                        0.04f
                    );
                    ((Dinner) Gdx.app.getApplicationListener()).setScreen(transition);
                }
            }, 1);
        }));

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                GameScreen.dialogController.startDialog(dinnerDialog);
            }
        }, 1);

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

package io.github.dinner.view.levels.cutscenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Timer;
import io.github.dinner.Dinner;
import io.github.dinner.controller.AudioController;
import io.github.dinner.model.Player;
import io.github.dinner.model.dialog.Dialog;
import io.github.dinner.model.dialog.LinearDialogNode;
import io.github.dinner.model.states.LevelState;
import io.github.dinner.view.screens.GameScreen;
import io.github.dinner.view.screens.TransitionScreen;

public class GardenNoiseOnBlackLevel extends LevelState {

    private String floorType = "wood";

    public GardenNoiseOnBlackLevel() {
        super();

        this.map = new TmxMapLoader().load("map/black.tmx");
        this.renderer = new OrthogonalTiledMapRenderer(map, unitScale);
        this.name = "gardenNoiseOnBlackLevel";
    }

    @Override
    public void init(){
        Player.getPlayer().setVisible(false);

        AudioController.stopMusic();
        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                AudioController.playSound("old_scream", false);
            }
        }, 1);

        Dialog blackDialog = new Dialog();
        blackDialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "Cosa è stato?!", 0, ()->{
            TransitionScreen transition = new TransitionScreen(null,
                "room1FFCutsceneLevel2",
                (Dinner) Gdx.app.getApplicationListener(),
                5, 4,
                Player.PlayerDirection.EAST,
                (GameScreen) ((Dinner) Gdx.app.getApplicationListener()).getScreen(),
                0,
                0.04f
            );
            ((Dinner) Gdx.app.getApplicationListener()).setScreen(transition);
        }));

        //blackDialog.addNode(new LinearDialogNode("\n", 0));

        Timer.schedule(new Timer.Task(){
            @Override
            public void run() {
                GameScreen.dialogController.startDialog(blackDialog);
            }
        }, 2);
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

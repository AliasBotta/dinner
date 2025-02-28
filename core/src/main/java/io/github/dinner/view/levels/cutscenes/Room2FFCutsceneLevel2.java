package io.github.dinner.view.levels.cutscenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.utils.Timer;
import io.github.dinner.Dinner;
import io.github.dinner.controller.AudioController;
import io.github.dinner.controller.SaveController;
import io.github.dinner.model.Player;
import io.github.dinner.model.dialog.Dialog;
import io.github.dinner.model.dialog.LinearDialogNode;
import io.github.dinner.model.states.LevelState;
import io.github.dinner.view.screens.GameScreen;
import io.github.dinner.view.screens.TransitionScreen;

public class Room2FFCutsceneLevel2 extends LevelState {

    private String floorType = "wood";

    public Room2FFCutsceneLevel2() {
        super();

        this.map = new TmxMapLoader().load("map/1st Floor/Room 2/Room2.tmx");

        this.renderer = new OrthogonalTiledMapRenderer(map, unitScale);
        this.name = "room1FFCutsceneLevel2";
        System.out.println("Inizializzato il livello");
    }

    public void init(){
        Player.getPlayer().setVisible(true);
        Player.getPlayer().setPosition(5, 4, Player.PlayerDirection.EAST);

        Dialog roomDialog = new Dialog();

        roomDialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "Quel rumore proveniva dal giardino... devo subito andare a controllare, speriamo non sia quello che temo...", 0, ()->{
            GameScreen.smoothZoom(GameScreen.DEFAULT_ZOOM_FACTOR, 2f, Player.getPlayer().getBox().x, Player.getPlayer().getBox().y);
            Dinner.gameState = Dinner.GameState.RUNNING;
            SaveController.getInstance().startAutoSaveTimer();
            SaveController.getInstance().saveAll(true);
            AudioController.playMusic("home", true);

            TransitionScreen transition = new TransitionScreen(GameScreen.levelController.getCurrentLevelName(),
                "room2FFLevel",
                (Dinner) Gdx.app.getApplicationListener(),
                5, 4,
                Player.PlayerDirection.EAST,
                (GameScreen) ((Dinner) Gdx.app.getApplicationListener()).getScreen(),
                0,
                0.04f);
            ((Dinner) Gdx.app.getApplicationListener()).setScreen(transition);
        }));

        Timer.schedule(new Timer.Task() {
            public void run() {
                GameScreen.dialogController.startDialog(roomDialog);
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

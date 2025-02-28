package io.github.dinner.view.levels.cutscenes;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import io.github.dinner.Dinner;
import io.github.dinner.controller.AudioController;
import io.github.dinner.controller.SaveController;
import io.github.dinner.model.Player;
import io.github.dinner.model.Progress;
import io.github.dinner.model.dialog.Dialog;
import io.github.dinner.model.dialog.LinearDialogNode;
import io.github.dinner.model.states.LevelState;
import io.github.dinner.view.screens.GameScreen;
import io.github.dinner.view.screens.TransitionScreen;

public class NewPhaseCutsceneLevel extends LevelState {



    public NewPhaseCutsceneLevel() {
        super();

        this.map = new TmxMapLoader().load("map/black.tmx");
        this.renderer = new OrthogonalTiledMapRenderer(map, unitScale);
        this.name = "newPhaseCutsceneLevel";
    }

    @Override
    public void init(){
        Dinner.gameState = Dinner.GameState.CUTSCENE;
        Player.getPlayer().setVisible(false);
        GameScreen.phase++;

        Dialog dialog = new Dialog();
        dialog.addNode(new LinearDialogNode(null, "Grazie per aver giocato la demo di Dinner.", 0).setPointer(1));
        dialog.addNode(new LinearDialogNode(null, "Se ti è piaciuto metti 30 e lode!", 1).setPointer(2));
        dialog.addNode(new LinearDialogNode(null, "I nuovi atti di Dinner verranno sviluppati in futuro, intanto, goditi la casa del signor Giovanni!", 2, () -> {
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
        GameScreen.isDialogNameVisible = false;
        GameScreen.dialogController.startDialog(dialog);
        Progress.progressMap.put("isRoom1Locked", false);
        Progress.progressMap.put("isClosetLocked", false);
        Progress.progressMap.put("isKitchenLocked", false);
        Progress.progressMap.put("isWestCorridorFFLocked", false);
        Progress.progressMap.put("isWestCorridorSFLocked", false);
        Progress.progressMap.put("isLibraryLocked", false);
        Progress.progressMap.put("isRoom4Locked", false);
        SaveController.getInstance().saveAll(true);
        SaveController.getInstance().startAutoSaveTimer();
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
}

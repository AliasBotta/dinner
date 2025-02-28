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

public class NewGameCutsceneLevel extends LevelState {

    public NewGameCutsceneLevel() {
        super();

        this.map = new TmxMapLoader().load("map/black.tmx");
        this.renderer = new OrthogonalTiledMapRenderer(map, unitScale);
        this.name = "newGameCutsceneLevel";
    }

    @Override
    public void init() {
        Dinner.gameState = Dinner.GameState.CUTSCENE;
        Player.getPlayer().setVisible(false);

        Dialog startCutscene = new Dialog();
        startCutscene.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "Sono trascorsi 20 anni dal giorno in cui la mia carriera è stata distrutta...", 0).setPointer(1));
        startCutscene.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "18 anni di sofferenze, 2 di umiliazioni... nessuno ingaggerebbe per casi importanti " +
            (Player.getPlayer().getGender().equals('M') ? "un investigatore radiato" : "un'investigatrice radiata"), 1).setPointer(2));
        startCutscene.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "Certo, se i tuoi clienti sono abitanti di un piccolo paese come Collesalvetti non puoi aspettarti chissà cosa", 2).setPointer(3));
        startCutscene.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "...", 3).setPointer(4));
        startCutscene.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "Ma ora ho finalmente una chance, non posso fallire...", 4).setPointer(5));
        startCutscene.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "Il caso del signor Giovanni potrebbe segnare il mio riscatto", 5).setPointer(6));
        startCutscene.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "...", 6).setPointer(7));
        startCutscene.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "Non devo fare tardi alla cena.", 7, () ->
        {
            AudioController.stopMusic();
            AudioController.playMusic("start_boom", false);

            Timer.schedule(new Timer.Task() {
                @Override
                public void run(){
                    TransitionScreen transition = new TransitionScreen(null,
                        "diningRoomCutsceneLevel",
                        (Dinner) Gdx.app.getApplicationListener(),
                        10, 6,
                        Player.PlayerDirection.NORTH,
                        (GameScreen) ((Dinner) Gdx.app.getApplicationListener()).getScreen(),
                        1,
                        0.004f
                    );
                    ((Dinner) Gdx.app.getApplicationListener()).setScreen(transition);
                    Player.getPlayer().setVisible(true);
                }
            }, 2);

        }));
        GameScreen.isDialogNameVisible = true;
        GameScreen.dialogController.startDialog(startCutscene);
    }
}

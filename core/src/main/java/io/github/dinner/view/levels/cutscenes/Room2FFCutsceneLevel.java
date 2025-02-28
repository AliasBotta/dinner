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
import io.github.dinner.model.interactables.npcs.Giovanni;
import io.github.dinner.model.states.LevelState;
import io.github.dinner.view.screens.GameScreen;
import io.github.dinner.view.screens.TransitionScreen;

public class Room2FFCutsceneLevel extends LevelState {

    private String floorType = "wood";

    public Room2FFCutsceneLevel() {
        super();

        this.map = new TmxMapLoader().load("map/1st Floor/Room 2/Room2.tmx");

        this.renderer = new OrthogonalTiledMapRenderer(map, unitScale);
        this.name = "room1FFCutsceneLevel";
        System.out.println("Inizializzato il livello");
    }

    @Override
    public void init(){

        Player.getPlayer().setPosition(5, 4, Player.PlayerDirection.EAST);

        Npc giovanni = new Giovanni(new Rectangle(7, 4, 1, 1.22f), true, Npc.Direction.WEST);
        interactableObjectController.add(giovanni);

        GameScreen.smoothZoom(0.3f, 1, 6, 4);

        Dialog roomDialog = new Dialog();

        roomDialog.addNode(new LinearDialogNode(Giovanni.name, "Ti sarai " + (Player.getPlayer().getGender().equals('M') ? "chiesto " : "chiesta ") + "perché sei qui, in mezzo a tanta gente sconosciuta.", 0).setPointer(1));
        roomDialog.addNode(new LinearDialogNode(Giovanni.name, "Devi perdonarmi, purtroppo ti ho " + (Player.getPlayer().getGender().equals('M') ? "ingannato, ragazzo." : "ingannata, ragazza."), 1).setPointer(2));
        roomDialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "...", 2).setPointer(3));
        roomDialog.addNode(new LinearDialogNode(Giovanni.name, "Al telefono ti avevo detto che ci saremmo incontrati per discutere del caso ma la verità è che il caso a cui voglio sottoporti...", 3).setPointer(4));
        roomDialog.addNode(new LinearDialogNode(Giovanni.name, "...è proprio questo che stai vivendo.", 4).setPointer(5));
        roomDialog.addNode(new LinearDialogNode(Giovanni.name, "Tutte queste persone hanno dei grossi debiti con me ed io sono preoccupato, " + Player.getPlayer().getPlayerName(), 5).setPointer(6));
        roomDialog.addNode(new LinearDialogNode(Giovanni.name, "Tutti loro hanno un motivo per, ecco... fare a meno di me.", 6).setPointer(7));
        roomDialog.addNode(new LinearDialogNode(Giovanni.name, "Ti chiederai perché li abbia invitati a cena allora... beh, ho intenzione di estinguere i loro debiti nei prossimi giorni, a patto che non abbiano cattive intenzioni nei miei confronti.", 7).setPointer(8));
        roomDialog.addNode(new LinearDialogNode(Giovanni.name, "Per questo ti ho chiamato assieme a loro, li ho messi tutti a tua disposizione in modo che tu possa indagare al meglio e arrestare chiunque voglia torcere un capello a me e alla mia adorata Aurora.", 8).setPointer(9));
        roomDialog.addNode(new LinearDialogNode(Giovanni.name, "Povera piccola... non fare finta di non averlo notato, da quando ha perso sua madre non si è più ripresa... non passa inosservata una trentenne che parla come una dolce bambina.", 9).setPointer(10));
        roomDialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "* Sigh *", 10).setPointer(11));
        roomDialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "Signor Giovanni, non crede che possa essere pericoloso per lei e sua figlia avere in casa tutta questa gente?", 11).setPointer(12));
        roomDialog.addNode(new LinearDialogNode(Giovanni.name, "Oh " + (Player.getPlayer().getGender().equals('M') ? "caro" : "cara") + ", dubito fortemente che qualcuno possa farci del male qua in casa mia, non dopo quella buonissima cena! Ahahahah", 12).setPointer(13));
        roomDialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "Uhm... sì ahahah, certo...", 13).setPointer(14));
        roomDialog.addNode(new LinearDialogNode(Giovanni.name, (Player.getPlayer().getGender().equals('M') ? "Tranquillo" : "Tranquilla") + ", sono sicuro che andrà tutto per il meglio", 14).setPointer(15));
        roomDialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "* Se stanotte qualcosa va storto per me è la fine, addio definitivamente alla mia carriera *", 15).setPointer(16));
        roomDialog.addNode(new LinearDialogNode(Giovanni.name, "Ora fa' dei bei sogni, " + Player.getPlayer().getPlayerName() + ". Domani iniziano le indagini! D'altronde... ti pago per questo ihihih", 16, () -> {
            TransitionScreen transition = new TransitionScreen(null,
                "gardenNoiseOnBlackLevel",
                (Dinner) Gdx.app.getApplicationListener(),
                5, 4,
                Player.PlayerDirection.NORTH,
                (GameScreen) ((Dinner) Gdx.app.getApplicationListener()).getScreen(),
                0,
                0.04f);
            ((Dinner) Gdx.app.getApplicationListener()).setScreen(transition);
        }));

        Timer.schedule(new Timer.Task() {
            @Override
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

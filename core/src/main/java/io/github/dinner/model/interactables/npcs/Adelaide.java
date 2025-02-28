package io.github.dinner.model.interactables.npcs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import io.github.dinner.model.Notebook;
import io.github.dinner.model.Player;
import io.github.dinner.model.Progress;
import io.github.dinner.model.dialog.ChoiceDialogNode;
import io.github.dinner.model.dialog.Dialog;
import io.github.dinner.model.dialog.LinearDialogNode;
import io.github.dinner.model.interactables.Npc;
import io.github.dinner.view.levels.first_floor.DiningRoomFFLevel;
import io.github.dinner.view.screens.GameScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Adelaide extends Npc {

    public static ArrayList<Map<Integer, Dialog>> dialogs = new ArrayList<>();
    public static String name = "Adelaide";

    public Adelaide(Rectangle box, boolean showInteractionWidget, Direction direction) {
        super(new Texture("characters/Adelaide_Idle.png"), box, showInteractionWidget, direction);
        dialogs.add(0, new HashMap<Integer, Dialog>());
        dialog0_1();
        dialog0_2();
        dialog0_3();
    }

    public static Dialog getCurrentDialog(){
        return dialogs.get(GameScreen.phase).get(getDialogCount(Adelaide.name));
    }

    public static void initializeDialogs(){
        setDialogCount(Adelaide.name, 1);
        dialogs.add(0, new HashMap<Integer, Dialog>());
        dialog0_1();
        dialog0_2();
        dialog0_3();
    }

    public static void dialog0_1(){
        Dialog dialog = new Dialog();

        dialog.addNode(new LinearDialogNode(Adelaide.name, "Oh ehm... ciao, " + (Player.getPlayer().getGender().equals('M') ? "vicino" : "vicina") + " di stanza... ehm, qual buon vento-", 0).setPointer(1));
        dialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "Ciao, sto cercando la cucina, mi serve un coltello per ehm... mi serve un coltello.", 1).setPointer(2));
        dialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "* Meglio non fare troppi allarmismi per il momento *", 2).setPointer(3));
        dialog.addNode(new LinearDialogNode(Adelaide.name, "Impossibile, la porta è chiusa... non che abbia provato ad aprirla, no no!", 3).setPointer(4));
        dialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "...", 4).setPointer(5));
        dialog.addNode(new LinearDialogNode(Adelaide.name, "La bloccano sempre quando Carlo non ci lavora per paura che la bamboccia, Aurora, possa farsi male.", 5).setPointer(6));
        dialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "Sai un sacco di cose sulla cucina... non hai idea di dove possa essere la chiave?", 6).setPointer(7));
        dialog.addNode(new LinearDialogNode(Adelaide.name, "Perché dovrei saperlo?! Se lo sapessi, secondo te, sarei ancora qua a... ehm, volevo dire, non ne ho idea, l'unico che potrebbe aiutarc-... aiutarti è Vittorio, l'informatico.", 7).setPointer(8));
        dialog.addNode(new LinearDialogNode(Adelaide.name, "Qui alcune porte in realtà sono chiuse con delle password, lui potrebbe hackerarle! Credo...", 8).setPointer(9));
        dialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "Dove sta Vittorio?", 9).setPointer(10));
        dialog.addNode(new LinearDialogNode(Adelaide.name, "Si è preso la stanza al piano di sopra, corridoio est.", 10).setPointer(11));
        dialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "Grazie mille, ehm... signora.", 11).setPointer(12));
        dialog.addNode(new LinearDialogNode(Adelaide.name, "Signorina! E comunque mi chiamo Adelaide.", 12, () -> {
            DiningRoomFFLevel.adelaide.setDirection(Npc.Direction.NORTH);
            Progress.progressMap.put("talkedToAdelaide", true);
            Notebook.notes.put("Adelaide ha detto che Vittorio può aiutarmi a sbloccare le porte protette da password... quanto può essere pericoloso un individuo con questo tipo di capacità?", true);
            incrementDialogCount(Adelaide.name);
        }));

        dialogs.get(0).put(1, dialog);
    }

    public static void dialog0_2(){
        Dialog dialog = new Dialog();

        dialog.addNode(new LinearDialogNode(Adelaide.name, "Sei ancora qui? Non hai una password da recuperare tu?? Non che a me interessi...", 0, () -> {
            DiningRoomFFLevel.adelaide.setDirection(Npc.Direction.NORTH);
        }));

        dialogs.get(0).put(2, dialog);
    }

    public static void dialog0_3(){
        Dialog dialog = new Dialog();
        ChoiceDialogNode choiceDialog = new ChoiceDialogNode(Adelaide.name, "Hai trovato la chiave della cucina??", 0);
        choiceDialog.addChoice("Sì", 1);
        choiceDialog.addChoice("No", 2);
        dialog.addNode(choiceDialog);

        dialog.addNode(new LinearDialogNode(Adelaide.name, "E ME LO DICI SOLO ADESSO?!... Cioè, non che sia così interessata... (che fame!)", 1));
        dialog.addNode(new LinearDialogNode(Adelaide.name, "E COSA ASPETTI A TROVARLA?!... Ehm, sì, non bisogna arrendersi mai, forza, forza!", 2));

        dialogs.get(0).put(3, dialog);
    }
}

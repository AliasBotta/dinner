package io.github.dinner.model.interactables.npcs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import io.github.dinner.model.Notebook;
import io.github.dinner.model.Player;
import io.github.dinner.model.dialog.Dialog;
import io.github.dinner.model.dialog.LinearDialogNode;
import io.github.dinner.model.interactables.Npc;
import io.github.dinner.view.screens.GameScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Carlo extends Npc {

    public static ArrayList<Map<Integer, Dialog>> dialogs = new ArrayList<>();
    public static String name = "Carlo";

    public Carlo(Rectangle box, boolean showInteractionWidget, Direction direction) {
        super(new Texture("characters/Carlo_Idle.png"), box, showInteractionWidget, direction);
        dialogs.add(0, new HashMap<Integer, Dialog>());
        dialog0_1();
        dialog0_2();
    }

    public static Dialog getCurrentDialog(){
        return dialogs.get(GameScreen.phase).get(getDialogCount(Carlo.name));
    }

    public static void initializeDialogs(){
        setDialogCount(Carlo.name, 1);
        dialogs.add(0, new HashMap<Integer, Dialog>());
        dialog0_1();
        dialog0_2();
    }

    public static void dialog0_1() {
        Dialog dialog = new Dialog();

        dialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "E tu chi sei?! Che ci fai qui?!", 0).setPointer(1));
        dialog.addNode(new LinearDialogNode(Carlo.name, "Chi sono?? Chi sei tu!", 1).setPointer(2));
        dialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "Rispondi alla mia domanda o ti sbatto in galera, sono un" + (Player.getPlayer().getGender().equals('M') ? " investigatore!" : "'investigatrice!"), 2).setPointer(3));
        dialog.addNode(new LinearDialogNode(Carlo.name, "Io... stavo solo prendendo un po' d'aria... dopo aver cucinato per 6 persone me la merito, no??", 3).setPointer(4));
        dialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "E avevi bisogno di entrare all'interno di un labirinto per prendere aria?! A chi pensi di prendere in giro?!", 4).setPointer(5));
        dialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "Il tuo datore di lavoro è morto nel bel mezzo del labirinto, il corpo scompare e \"casualmente\" trovo te nei paraggi...", 5).setPointer(6));
        dialog.addNode(new LinearDialogNode(Carlo.name, "Ehi aspetta un attimo, il signor Giovanni è morto?! Non ne avevo idea, giuro, non... io non ne so niente!", 6).setPointer(7));
        dialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "...Carlo, giusto? Eri lo chef di Giovanni... come hai potuto-", 7).setPointer(8));
        dialog.addNode(new LinearDialogNode(Carlo.name, "Ti ho detto che non so di cosa tu stia parlando, non torcerei mai un capello nè al signor Giovanni nè alla signorina Aurora, perché dovrei??", 8).setPointer(9));
        dialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "* Ha ragione, al momento non ho un movente *", 9).setPointer(10));
        dialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "...e allora che ci facevi qui?! Non ripetermi la balla della boccata d'aria!", 10).setPointer(11));
        dialog.addNode(new LinearDialogNode(Carlo.name, "Senti, a te non devo dire un bel niente, per quanto ne so sei solo " + (Player.getPlayer().getGender().equals('M') ? "uno" : "una") + " dei tanti falliti che si sono indebitati col capo.", 11).setPointer(12));
        dialog.addNode(new LinearDialogNode(Carlo.name, "Vuoi giocare a fare l'" + (Player.getPlayer().getGender().equals('M') ? "investigatore?! Sentiti libero" : "investigatrice?! Sentiti libera") + ", accedi al computer nella tua stanza e fai le tue ricerche.", 12).setPointer(13));
        dialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "Ti smaschererò, puoi starne certo!", 13).setPointer(14));
        dialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "* Sigh devo assolutamente provare quel computer *", 14, () -> {
            incrementDialogCount(Carlo.name);
            Notebook.notes.put("Ho trovato Carlo in giardino dopo la scomparsa del cadavere di Giovanni. Si è difeso dicendo che volesse prendere una boccata d'aria, ma è assurdo farlo nel bel mezzo di un labirinto!", true);
        }));
        dialogs.get(0).put(1, dialog);
    }

    public static void dialog0_2() {
        Dialog dialog = new Dialog();

        dialog.addNode(new LinearDialogNode(Carlo.name, "Allora? Cosa hai da guardare ancora?!", 0).setPointer(1));
        dialog.addNode(new LinearDialogNode(Carlo.name, "Vai al computer dentro la tua stanza, così puoi giocare a fare l'" + (Player.getPlayer().getGender().equals('M') ? "investigatore." : "investigatrice."), 1));

        dialogs.get(0).put(2, dialog);
    }
}

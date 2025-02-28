package io.github.dinner.model.interactables.npcs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Timer;
import io.github.dinner.model.Notebook;
import io.github.dinner.model.Player;
import io.github.dinner.model.Progress;
import io.github.dinner.model.dialog.ChoiceDialogNode;
import io.github.dinner.model.dialog.Dialog;
import io.github.dinner.model.dialog.LinearDialogNode;
import io.github.dinner.model.interactables.Npc;
import io.github.dinner.view.levels.first_floor.EastCorridorFFLevel;
import io.github.dinner.view.screens.GameScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Aurora extends Npc {

    public static ArrayList<Map<Integer, Dialog>> dialogs = new ArrayList<>();
    public static String name = "Aurora";

    public Aurora(Rectangle box, boolean showInteractionWidget, Direction direction) {
        super(new Texture("characters/Aurora_Idle.png"), box, showInteractionWidget, direction);
        dialogs.add(0, new HashMap<Integer, Dialog>());
        dialog0_1();
        dialog0_2();
        dialog0_3();
        dialog0_4();
    }

    public static Dialog getCurrentDialog(){
        System.out.println("STATO DIALOGHI: " + Npc.nDialogs.entrySet().stream()
            .map(entry -> entry.getKey() + "=" + entry.getValue())
            .collect(Collectors.joining(", ", "{", "}")));
        System.out.println("DIALOGS DI AURORA: " + dialogs.get(GameScreen.phase));
        return dialogs.get(GameScreen.phase).get(getDialogCount(Aurora.name));
    }

    public static void initializeDialogs(){
        setDialogCount(Aurora.name, 1);
        dialogs.add(0, new HashMap<Integer, Dialog>());
        dialog0_1();
        dialog0_2();
        dialog0_3();
        dialog0_4();
    }

    private static void dialog0_1() {
        Dialog dialog = new Dialog();
        dialog.addNode(new LinearDialogNode(Aurora.name, "Papà ama cantare, sì sì!", 0).setPointer(1));
        ChoiceDialogNode auroraNode1 = new ChoiceDialogNode(Aurora.name, "Tu l'hai sentito papà cantare?",1);
        auroraNode1.addChoice("Sì", 3);
        auroraNode1.addChoice("No", 4);
        dialog.addNode(auroraNode1);
        dialog.addNode(new LinearDialogNode(Aurora.name,"Ah che bello, è andato in giardino a cantare e ora sai anche tu quanto è bravo!", 3, () ->{
            Timer.schedule(new Timer.Task(){
                @Override
                public void run() {
                    EastCorridorFFLevel.aurora.setDirection(Npc.Direction.WEST);
                }
            }, 0.5f);
        }).setPointer(5));

        dialog.addNode(new LinearDialogNode(Aurora.name, "Che peccato...", 4));

        dialog.addNode(new LinearDialogNode(Aurora.name, "Se lo vuoi sentire vai: dritto di là la la la... sali la scala la la la... esci in giardino la la la... non canto bene come papo...", 5, () -> {
            incrementDialogCount(Aurora.name);
        }).setPointer(6));

        dialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "* Meglio tenere a mente questa canzone... *", 6, () -> {
            Notebook.notes.put("Aurora ha detto: 'dritto di là la la la... sali la scala la la la... esci in giardino la la la...', devono essere le indicazioni per il giardino.", false);
            Progress.progressMap.put("talkedToAurora", true);
        }));

        dialogs.get(0).put(1, dialog);
    }

    private static void dialog0_2() {
        Dialog dialog = new Dialog();
        dialog.addNode(new LinearDialogNode(Aurora.name, "Dritto di là la la la... sali la scala la la la... esci in giardino la la la... non canto bene come papo...", 0, () ->{
            Timer.schedule(new Timer.Task(){
                @Override
                public void run() {
                    EastCorridorFFLevel.aurora.setDirection(Npc.Direction.WEST);
                }
            }, 0.5f);
        }));

        dialogs.get(0).put(2, dialog);
    }

    private static void dialog0_3() {
        Dialog dialog = new Dialog();
        dialog.addNode(new LinearDialogNode(Aurora.name, "Hai ascoltato papà che canta?", 0).setPointer(1));
        dialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "Oh ehm... Aurora! Cosa ci fai qui?", 1).setPointer(2));
        dialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "* Se scopre che è morto pure suo padre diventa più pazza di ora *", 2).setPointer(3));
        dialog.addNode(new LinearDialogNode(Aurora.name, "Cercavo la creta per i vasi della mamma, a lei piacciono i miei vasi! Guarda, li ho fatti tutti io!", 3).setPointer(4));
        dialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "Meraviglioso Aurora, però adesso starei cercando un coltello...", 4).setPointer(5));
        dialog.addNode(new LinearDialogNode(Aurora.name, "No no no, papà non mi fa entrare in cucina senza il permesso.", 5).setPointer(6));
        dialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "Esatto! La cucina, mi dici dov'è? Durante la cena i piatti erano già tutti a tavola, non ho potuto vedere dove venivano preparati...", 6).setPointer(7));
        dialog.addNode(new LinearDialogNode(Aurora.name, "La strada al contrario tu farai la la la... e la cucina troverai la la la... la tua camera lascerai e dritto tu andrai la la la... se ti sembra familiare, è dove meglio dovrai controllare!", 7).setPointer(8));
        dialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "* Fantastico, un'altra canzone da memorizzare *", 8, () -> {
            Progress.progressMap.put("talkedToAurora2", true);
            Notebook.notes.put("Aurora ha detto: 'La strada al contrario tu farai la la la... e la cucina troverai la la la... la tua camera lascerai e dritto tu andrai la la la... se ti sembra familiare, è dove meglio dovrai controllare!', la cucina deve essere di fronte alla mia stanza, ma lì c'è la sala da pranzo...", false);
            incrementDialogCount(Aurora.name);
        }));

        dialogs.get(0).put(3, dialog);
    }

    private static void dialog0_4() {
        Dialog dialog = new Dialog();
        dialog.addNode(new LinearDialogNode(Aurora.name, "La strada al contrario tu farai la la la... e la cucina troverai la la la... la tua camera lascerai e dritto tu andrai la la la... se ti sembra familiare, è dove meglio dovrai controllare!", 0));

        dialogs.get(0).put(4, dialog);
    }
}

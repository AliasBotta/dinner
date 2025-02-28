package io.github.dinner.model.interactables.npcs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import io.github.dinner.model.Notebook;
import io.github.dinner.model.Player;
import io.github.dinner.model.Progress;
import io.github.dinner.model.dialog.ChoiceDialogNode;
import io.github.dinner.model.dialog.Dialog;
import io.github.dinner.model.dialog.LinearDialogNode;
import io.github.dinner.model.interactables.Item;
import io.github.dinner.model.interactables.Npc;
import io.github.dinner.view.screens.GameScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Vittorio extends Npc {

    public static ArrayList<Map<Integer, Dialog>> dialogs = new ArrayList<>();
    public static String name = "Vittorio";
    public static Item kitchenPassword = new Item("Password della cucina", "la password della cucina!", "items/paper.png" , new Rectangle(10, 10, 1, 1), false);
    public static Item libraryPassword = new Item("Password della biblioteca", "la password della biblioteca!", "items/paper.png" , new Rectangle(10, 10, 1, 1), false);

    public Vittorio(Rectangle box, boolean showInteractionWidget, Direction direction) {
        super(new Texture("characters/Vittorio_Idle.png"), box, showInteractionWidget, direction);
        dialogs.add(0, new HashMap<Integer, Dialog>());
        dialog0_1();
        dialog0_2();
        dialog0_3();
    }

    public static Dialog getCurrentDialog(){
        return dialogs.get(GameScreen.phase).get(getDialogCount(Vittorio.name));
    }

    public static void initializeDialogs(){
        setDialogCount(Vittorio.name, 1);
        dialogs.add(0, new HashMap<Integer, Dialog>());
        dialog0_1();
        dialog0_2();
        dialog0_3();
    }

    private static void dialog0_1(){
        Dialog dialog = new Dialog();

        dialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "* knock knock *", 0).setPointer(1));
        dialog.addNode(new LinearDialogNode(Vittorio.name, "Chi va là?!", 1).setPointer(2));
        dialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "Ciao Vittorio, sono " + Player.getPlayer().getPlayerName() + " avrei bisogno di una mano con la password della cucina...", 2).setPointer(3));
        dialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "...mi chiedevo, ecco, se potessi hackerarla.", 3).setPointer(4));
        dialog.addNode(new LinearDialogNode(Vittorio.name, "Ti chiedevi se potessi?! Pensi che sia un incompetente per caso?! Hai idea di chi ti sta parlando?!", 4).setPointer(5));
        dialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "...una porta?", 5).setPointer(6));

        dialog.addNode(new LinearDialogNode(Vittorio.name, "Molto divertente! Puoi prenderti gioco di me quanto vuoi ma il tuo spirito non potrà mai superare il mio intelletto!", 6).setPointer(7));
        dialog.addNode(new LinearDialogNode(Vittorio.name, "Per tua informazione, io le password non solo le so trovare, ma sono talmente rapido, furbo e intelligente che le ho già trovate, scritte su dei foglietti di carta e conservate gelosamente!", 7).setPointer(8));
        dialog.addNode(new LinearDialogNode(Vittorio.name, "Solo le porte non protette da password rimarranno chiuse per me, come il seminterrato.", 8).setPointer(9));
        dialog.addNode(new LinearDialogNode(Vittorio.name, "Quel vecchio ha deciso di chiuderlo con una normalissima chiave... bah, retrogrado buzzurro, pensa di essere più furbo solo perché ha i soldi.", 9).setPointer(10));
        dialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "* Che insopportabile! Ma magari... *", 10).setPointer(11));
        dialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "Oh Vittorio, io non posso nulla di fronte alla tua intelligenza, ti chiedo solo umilmente di concedermi una delle tue password...", 11).setPointer(12));
        dialog.addNode(new LinearDialogNode(Vittorio.name, "So cosa stai facendo, ma il tuo patetico tentativo di estorcere importanti dati con l'adulazione mi ha intenerito, quindi ti darò una possibilità.", 12).setPointer(13));
        dialog.addNode(new LinearDialogNode(Vittorio.name, "Rispondi correttamente alla seguente domanda e riceverai la password della cucina (tanto non ne sarai in grado ahahahah).", 13).setPointer(14));
        ChoiceDialogNode quizDialog = new ChoiceDialogNode(Vittorio.name, "Quando è nato il grande Alan Turing?", 14);
        quizDialog.addChoice("23 giugno 1912", 15);
        quizDialog.addChoice("23 giugno 1934", 16);
        quizDialog.addChoice("13 ottobre 1934", 16);
        quizDialog.addChoice("13 ottobre 1912", 16);
        dialog.addNode(quizDialog);

        dialog.addNode(new LinearDialogNode(Vittorio.name, "Come?! Beh non c'è troppo da stupirsi, è una cosa che sanno tutti...", 15).setPointer(17));
        dialog.addNode(new LinearDialogNode(Vittorio.name, "Ora prendi la tua password da sotto la porta e smamma! Ho delle cose da fare qui!", 17, () -> {
            Progress.progressMap.put("talkedToVittorio", true);
            incrementDialogCount(Adelaide.name);
            setDialogCount(Vittorio.name, 3);

            kitchenPassword.addToInventory(() -> {
                Progress.progressMap.put("isKitchenPasswordPicked", true);
                Dialog pickedDialog = new Dialog();
                pickedDialog.addNode(new LinearDialogNode(null, "Hai raccolto la password della cucina!", 0));
            });
        }));

        dialog.addNode(new LinearDialogNode(Vittorio.name, "Risposta sbagliata! Forse può esserti più utile leggere un libro ogni tanto...", 16).setPointer(18));
        dialog.addNode(new LinearDialogNode(Vittorio.name, "Ecco, tieni la password della biblioteca qui di fronte, se sai leggere forse puoi trovare la risposta del quiz ahahah",18, () -> {
            setDialogCount(Vittorio.name, 2);

            libraryPassword.addToInventory(() -> {
                Progress.progressMap.put("isLibraryLocked", false);
                Dialog pickedDialog = new Dialog();
                pickedDialog.addNode(new LinearDialogNode(null, "Hai raccolto la password della biblioteca!", 0));
            });
        }));


        dialogs.get(0).put(1, dialog);
    }

    private static void dialog0_2(){
        Dialog dialog = new Dialog();

        dialog.addNode(new LinearDialogNode(Vittorio.name, "Vediamo se finalmente hai imparato qualcosa.", 0).setPointer(1));

        ChoiceDialogNode quizDialog = new ChoiceDialogNode(Vittorio.name, "Quando è nato il grande Alan Turing?", 1);
        quizDialog.addChoice("23 giugno 1912", 2);
        quizDialog.addChoice("23 giugno 1934", 3);
        quizDialog.addChoice("13 ottobre 1934", 3);
        quizDialog.addChoice("13 ottobre 1912", 3);
        dialog.addNode(quizDialog);

        dialog.addNode(new LinearDialogNode(Vittorio.name, "Sei una delusione... anzi no, sapevo già che non ce l'avresti fatta ahahahah", 3));
        dialog.addNode(new LinearDialogNode(Vittorio.name, "Come?! Beh non c'è troppo da stupirsi, è una cosa che sanno tutti...", 2).setPointer(4));
        dialog.addNode(new LinearDialogNode(Vittorio.name, "Ora prendi la tua password da sotto la porta e smamma! Ho delle cose da fare qui!", 4, () -> {
            Progress.progressMap.put("talkedToVittorio", true);
            incrementDialogCount(Adelaide.name);
            setDialogCount(Vittorio.name, 3);
            Notebook.notes.put("Vittorio è un narcisista scontroso, ma non sembra in grado di uccidere qualcuno. Meglio tenerlo d'occhio in ogni caso: ha accesso troppo facile a molte zone della casa.", true);

            kitchenPassword.addToInventory(() -> {
                Progress.progressMap.put("isKitchenPasswordPicked", true);
                Dialog pickedDialog = new Dialog();
                pickedDialog.addNode(new LinearDialogNode(null, "Hai raccolto la password della cucina!", 0));
            });
        }));


        dialogs.get(0).put(2, dialog);
    }

    private static void dialog0_3() {
        Dialog dialog = new Dialog();

        dialog.addNode(new LinearDialogNode(Vittorio.name, "Hai avuto la tua password, ora smamma ho detto!", 0));

        dialogs.get(0).put(3, dialog);
    }
}

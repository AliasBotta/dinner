package io.github.dinner.model.interactables.npcs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import io.github.dinner.model.dialog.Dialog;
import io.github.dinner.model.interactables.Npc;
import io.github.dinner.view.screens.GameScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Eleonora extends Npc {

    public static ArrayList<Map<Integer, Dialog>> dialogs = new ArrayList<>();
    public static String name = "Eleonora";

    public Eleonora(Rectangle box, boolean showInteractionWidget, Direction direction) {
        super(new Texture("characters/Eleonora_Idle.png"), box, showInteractionWidget, direction);
        dialogs.add(0, new HashMap<Integer, Dialog>());
    }

    public static Dialog getCurrentDialog(){
        return dialogs.get(GameScreen.phase).get(getDialogCount(Eleonora.name));
    }

    public static void initializeDialogs(){
        setDialogCount(Eleonora.name, 1);
        dialogs.add(0, new HashMap<Integer, Dialog>());

    }
}

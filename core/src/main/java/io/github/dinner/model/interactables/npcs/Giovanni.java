package io.github.dinner.model.interactables.npcs;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import io.github.dinner.model.dialog.Dialog;
import io.github.dinner.model.interactables.Npc;
import io.github.dinner.view.screens.GameScreen;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Giovanni extends Npc {

    public static ArrayList<Map<Integer, Dialog>> dialogs = new ArrayList<>();
    public static int nDialog;
    public static String name = "Giovanni";

    public Giovanni(Rectangle box, boolean showInteractionWidget, Direction direction) {
        super(new Texture("characters/Giovanni_Idle.png"), box, showInteractionWidget, direction);
        dialogs.add(0, new HashMap<Integer, Dialog>());
    }

    public static Dialog getCurrentDialog(){
        return dialogs.get(GameScreen.phase).get(getDialogCount(Giovanni.name));
    }

    public static void initializeDialogs(){
        setDialogCount(Giovanni.name, 1);
        dialogs.add(0, new HashMap<Integer, Dialog>());
    }
}

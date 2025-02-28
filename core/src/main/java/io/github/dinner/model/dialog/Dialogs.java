package io.github.dinner.model.dialog;

import io.github.dinner.controller.AudioController;
import io.github.dinner.model.Player;

public class Dialogs {

    public static Dialog lockedDoorDialog;

    public static void initDoorDialogs()
    {
        lockedDoorDialog = new Dialog();
        lockedDoorDialog.addNode(new LinearDialogNode(Player.getPlayer().getPlayerName(), "* La porta è chiusa. *", 0));
    }
}

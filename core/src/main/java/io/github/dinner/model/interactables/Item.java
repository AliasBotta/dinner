package io.github.dinner.model.interactables;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import io.github.dinner.controller.AudioController;
import io.github.dinner.controller.LevelController;
import io.github.dinner.model.Player;
import io.github.dinner.model.dialog.ChoiceDialogNode;
import io.github.dinner.model.dialog.Dialog;
import io.github.dinner.model.dialog.LinearDialogNode;
import io.github.dinner.view.screens.GameScreen;
import io.github.dinner.util.Action;

import java.util.Objects;


public class Item extends InteractableObject {
    private String name;
    private String texturePath;
    private Texture texture;
    private Dialog dialog;

    public Item(String name, String dialogName, String texturePath, Rectangle box, boolean showInteractionWidget) {
        super(box, showInteractionWidget);
        this.name = name;
        this.texturePath = texturePath;
        this.texture = new Texture(texturePath);
        //this.dialog = createPickupDialog(dialogName);
    }

    private Dialog createPickupDialog(String dialogName, Action onPick) {
        Dialog itemPickDialog = new Dialog();
        ChoiceDialogNode itemPickChoice = new ChoiceDialogNode(null, "Vuoi raccogliere " + dialogName + "?",0);
        itemPickChoice.addChoice("Sì", 1);
        itemPickChoice.addChoice("No", 2);
        itemPickDialog.addNode(itemPickChoice);
        itemPickDialog.addNode(new LinearDialogNode(null, "Hai raccolto " + dialogName + ".", 1, () -> this.addToInventory(onPick)));
        itemPickDialog.addNode(new LinearDialogNode("null", "", 2, () -> GameScreen.dialogController.stopDialog()));
        return itemPickDialog;
    }


    public void interact(Player player, Action onPick) {
        GameScreen.dialogController.startDialog(createPickupDialog(this.name, onPick));
    }

    @Override
    public void interact(Player player) {
        this.interact(null, null);
    }

    public void addToInventory(Action onPick){
        AudioController.stopSound("collected");
        AudioController.playSound("collected", false);
        Player.getPlayer().addItem(this);
        LevelController.getLevelController().markItemAsPicked(this.name);
        interactableObjectController.remove(this);
        GameScreen.updateInventoryUI();
        if(onPick != null)
            onPick.action();
    }

    public void removeFromInventory()
    {
        Player.getPlayer().removeItem(this);
        GameScreen.updateInventoryUI();
    }

    public Texture getTexture() {
        return texture;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(name, item.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public String getTexturePath() {
        return texturePath;
    }
}

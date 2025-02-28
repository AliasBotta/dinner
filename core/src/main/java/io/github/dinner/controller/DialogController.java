package io.github.dinner.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import io.github.dinner.model.dialog.*;
import io.github.dinner.model.interactables.Npc;
import io.github.dinner.model.interactables.npcs.*;
import io.github.dinner.view.screens.GameScreen;
import io.github.dinner.view.ui.DialogBox;
import io.github.dinner.view.ui.OptionBox;
import io.github.dinner.util.Action;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class DialogController extends InputAdapter {

	private DialogTraverser traverser;
	private DialogBox dialogBox;
	private OptionBox optionBox;

	public DialogController(DialogBox box, OptionBox optionBox) {
		this.dialogBox = box;
		this.optionBox = optionBox;
	}

	@Override
	public boolean keyUp(int keycode) {
		if (dialogBox.isVisible()) {
			return true;
		}
		return false;
	}

	@Override
	public boolean keyDown(int keycode) {
		if (optionBox.isVisible()) {
			if (keycode == Keys.UP || keycode == Keys.W) {
				optionBox.moveUp();
				return true;
			} else if (keycode == Keys.DOWN || keycode == Keys.S) {
				optionBox.moveDown();
				return true;
			}
		}
		if (dialogBox.isVisible() && !dialogBox.isFinished() && keycode == Keys.E) {
			dialogBox.skipAnimation();
			return true;
		}
		if (traverser != null && keycode == Keys.E) {
			DialogNode thisNode = traverser.getNode();

			if (thisNode instanceof LinearDialogNode) {
				LinearDialogNode node = (LinearDialogNode) thisNode;
                GameScreen.nameLabel.setText(node.getSpeakerName());
				if (node.getAction() != null) {
					node.getAction().action();
				}

				if (node.getPointers().isEmpty()) {
					traverser = null; // end dialog
					dialogBox.setVisible(false);
                    GameScreen.isDialogueBoxVisible = false;
                    GameScreen.nameTable.setVisible(false);
				} else {
					progress(0);
				}
			}
			if (thisNode instanceof ChoiceDialogNode) {
				ChoiceDialogNode node = (ChoiceDialogNode) thisNode;
				optionBox.callAction();
				Gdx.app.log("DialogController", "Called action");
				int index = optionBox.getIndex();
				if(node.getPointers().get(index) == -1) {
					traverser = null;
					dialogBox.setVisible(false);
                    GameScreen.isDialogueBoxVisible = false;
					optionBox.setVisible(false);
                    GameScreen.nameTable.setVisible(false);
				} else {
					progress(index);
				}
			}

			return true;
		}
		if (dialogBox.isVisible()) {
			return true;
		}
		return false;
	}

	public void update(float delta) {
		if (dialogBox.isFinished() && traverser != null) {
			DialogNode nextNode = traverser.getNode();
			if (nextNode instanceof ChoiceDialogNode) {
				optionBox.setVisible(true);
			}
		}
	}

	public void startDialog(Dialog dialog) {
		traverser = new DialogTraverser(dialog);
		dialogBox.setVisible(true);
        GameScreen.isDialogueBoxVisible = true;
        /*if(GameScreen.isDialogNameVisible){
            GameScreen.nameLabel.setText(GameScreen.dialogName);
            GameScreen.nameTable.setVisible(true);
        }else{
            GameScreen.nameTable.setVisible(false);
        }*/

		DialogNode nextNode = traverser.getNode();
		if (nextNode instanceof LinearDialogNode) {
			LinearDialogNode node = (LinearDialogNode) nextNode;
            if(node.getSpeakerName() != null)
            {
                GameScreen.nameLabel.setText(node.getSpeakerName());
                GameScreen.nameTable.setVisible(true);
            }
			dialogBox.animateText(node.getText());
		}
		if (nextNode instanceof ChoiceDialogNode) {
			ChoiceDialogNode node = (ChoiceDialogNode) nextNode;
            if(node.getSpeakerName() != null)
            {
                GameScreen.nameLabel.setText(node.getSpeakerName());
                GameScreen.nameTable.setVisible(true);
            }
			dialogBox.animateText(node.getText());
			optionBox.clearChoices();
			for (Map.Entry<String, Action> entry : node.getOptions().entrySet()) {
				String option = entry.getKey();
				Action action = entry.getValue();
				if (action != null) {
					optionBox.addOption(option, action);
				} else {
					optionBox.addOption(option);
				}
			}
		}
	}

	private void progress(int index) {
		optionBox.setVisible(false);
		DialogNode nextNode = traverser.getNextNode(index);

		if (nextNode instanceof LinearDialogNode) {

			LinearDialogNode node = (LinearDialogNode) nextNode;
            GameScreen.nameLabel.setText(node.getSpeakerName());
			dialogBox.animateText(node.getText());
			if (node.getAction() != null && node.getText().equals(" ")) {
				node.getAction().action();
			}
		}
		if (nextNode instanceof ChoiceDialogNode) {
			ChoiceDialogNode node = (ChoiceDialogNode) nextNode;
            GameScreen.nameLabel.setText(node.getSpeakerName());
			dialogBox.animateText(node.getText());
			optionBox.clearChoices();
			for (Map.Entry<String, Action> entry : node.getOptions().entrySet()) {
				String option = entry.getKey();
				Action action = entry.getValue();
				if (action != null) {
					optionBox.addOption(option, action);
					Gdx.app.log("DialogController", "Added option with action");
				} else {
					optionBox.addOption(option);
				}
			}
		}
	}

	public boolean isDialogShowing() {
		return dialogBox.isVisible();
	}

	public DialogTraverser getTraverser() {
		return traverser;
	}

    public static void resetDialogs(){
        List<String> names = Arrays.asList(Giovanni.name, Vittorio.name, Eleonora.name, Aurora.name, Adelaide.name, Carlo.name);

        names.forEach(name -> Npc.setDialogCount(name, 1));
//        Giovanni.nDialog = 1;
//        Vittorio.nDialog = 1;
//        Eleonora.nDialog = 1;
//        Aurora.nDialog = 1;
//        Adelaide.nDialog = 1;
//        Carlo.nDialog = 1;

        System.out.println("I dialogi potrebbero non essere stati resettati correttamente!!"); // !!!
    }

    public static void initializeDialogs()
    {
        Giovanni.initializeDialogs();
        Vittorio.initializeDialogs();
        Eleonora.initializeDialogs();
        Aurora.initializeDialogs();
        Adelaide.initializeDialogs();
        Carlo.initializeDialogs();
    }

    public void stopDialog()
    {
        this.optionBox.setVisible(false);
        this.dialogBox.setVisible(false);
        GameScreen.nameTable.setVisible(false);
        GameScreen.isDialogueBoxVisible = false;
        traverser = null;
    }
}

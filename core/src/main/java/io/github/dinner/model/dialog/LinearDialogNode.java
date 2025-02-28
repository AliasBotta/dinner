package io.github.dinner.model.dialog;

import io.github.dinner.view.screens.GameScreen;
import io.github.dinner.util.Action;

import java.util.ArrayList;
import java.util.List;

public class LinearDialogNode implements DialogNode {

	private String text;
	private int id;
	private List<Integer> pointers = new ArrayList<Integer>();
	private Action action;
    private GameScreen game;
    private String speakerName;

    public LinearDialogNode(String speakerName, String text, int id) {
        this.text = Dialog.generateDialog(text);
        this.id = id;
        this.action = null;
        this.speakerName = speakerName;
    }

    public LinearDialogNode(String speakerName, String text, int id, Action action) {
        this.text = Dialog.generateDialog(text);
        this.id = id;
        this.action = action;
        this.speakerName = speakerName;
    }

	public void setAction(Action action) {
		this.action = action;
	}

	public DialogNode setPointer(int id) {
		pointers.add(id);
		return this;
	}

	public String getText() {
		return text;
	}

	@Override
	public int getID() {
		return id;
	}

	@Override
	public List<Integer> getPointers() {
		return pointers;
	}

	public void setText(String text) {
		this.text = text;
	}

	public Action getAction() {
		return this.action;
	}

    public String getSpeakerName(){
        return this.speakerName;
    }


}

package io.github.dinner.model.dialog;

import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.List;

public interface DialogNode {

	public int getID();

	public List<Integer> getPointers();

}

package io.github.dinner.view.ui;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import io.github.dinner.controller.AudioController;

public class DialogBox extends Table {
    private String targetText = "";
    private float animTimer = 0f;
    private float animationTotalTime = 0f;
    private float TIME_PER_CHAR = 0.05f;
    private STATE state = STATE.IDLE;
    private Label textLabel;
    private Label nameLabel;

    private enum STATE {
        IDLE,
        ANIMATING
    }

    public DialogBox(Skin skin) {
        super(skin);
        textLabel = new Label("", skin);
        this.add(textLabel).expand().align(Align.topLeft).pad(5f).padLeft(15f);
        this.setBackground(skin.getDrawable("DialogBox"));
    }

    public void animateText(String text) {
        targetText = text;
        animTimer = 0f;
        animationTotalTime = targetText.length() * TIME_PER_CHAR;
        state = STATE.ANIMATING;
    }

    public boolean isFinished() {
        return state == STATE.IDLE;
    }

    private void setText(String text) {
        if (!text.contains("\n")) {
            text = text + "\n";
        }
        textLabel.setText(text);
    }

    public void skipAnimation() {
        if (state == STATE.ANIMATING) {
            setText(targetText);
            state = STATE.IDLE;
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        if (state == STATE.ANIMATING) {
            animTimer += delta;
            if (animTimer >= animationTotalTime) {
                state = STATE.IDLE;
                animTimer = animationTotalTime;
            }
            int numChars = (int) (animTimer / TIME_PER_CHAR);
            if (animTimer % 0.08f <= delta) {
                //typingSound.play(SettingController.gameVolume);
                AudioController.stopSound("type");
                AudioController.playSound("type", false);
            }
            setText(targetText.substring(0, numChars));
        }
    }

    @Override
    public float getPrefWidth() {
        return 485f;
    }

    @Override
    public float getPrefHeight() {
        return 60f;
    }

}

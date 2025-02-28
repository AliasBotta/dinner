package io.github.dinner.view.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.dinner.Dinner;
import io.github.dinner.controller.AudioController;
import io.github.dinner.controller.MenuController;
import io.github.dinner.model.Player;
import io.github.dinner.model.states.MenuState;
import io.github.dinner.view.screens.GameScreen;

public class CharacterSelectionMenu extends MenuState {

    Stage stage;
    private Viewport viewport;
    Dinner game = (Dinner) Gdx.app.getApplicationListener();
    float buttonWidthPercentage;
    float buttonHeightPercentage;
    float buttonPadPercentage;
    float scale;
    Table mainTable;
    CheckBox characterM;
    CheckBox characterF;
    TextField name;
    TextButton start;
    Label characterLabel;

    public CharacterSelectionMenu(MenuController loader) {
        super(loader);

        this.viewport = new ScreenViewport();
        this.stage = new Stage(viewport);
        this.buttonWidthPercentage = 0.12f; // 12% della larghezza della finestra
        this.buttonHeightPercentage = 0.23f; // 23% dell'altezza della finestra
        this.buttonPadPercentage = 0.02f; // 5% dell'altezza
        this.scale = 100f;

        mainTable = new Table();
        mainTable.setBackground(skin.getDrawable("black"));
        mainTable.setFillParent(true);

        characterM = new CheckBox("", skin, "protagonistM");
        characterM.setChecked(true);
        Player.getPlayer().setGender('M');
        characterF = new CheckBox("", skin, "protagonistF");
        name = new TextField("Nome", skin);
        start = new TextButton("INIZIA", skin);

        // Add ChangeListeners to CheckBoxes
        characterM.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioController.playSound("check", false);
                if (characterM.isChecked()) {
                    characterF.setChecked(false);
                    Player.getPlayer().setGender('M');
                }else if(!characterF.isChecked()){
                    characterM.setChecked(true);
                }
            }
        });

        characterF.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioController.playSound("check", false);
                if (characterF.isChecked()) {
                    characterM.setChecked(false);
                    Player.getPlayer().setGender('F');
                }else if(!characterM.isChecked()){
                    characterF.setChecked(true);
                }
            }
        });

        start.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                AudioController.playSound("click_down", false);// Riproduce la prima parte del suono
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                Player.getPlayer().setPlayerName(name.getText());
                // SaveController.getInstance().setCurrentSlot(0);
                // setti il current slot a 0 in modo da prevenire il recupero di dati da altri salvataggi
                Player.getPlayer().setPosition(10, 6, Player.PlayerDirection.SOUTH);
                GameScreen gameScreen = new GameScreen(game, null); //game.getScreen().dispose(); // Serve per fare il dispose del "current screen", ovvero del MenuScreen (la variabile si chiama "screen" ed è nella classe Screen)
                game.setScreen(gameScreen);
            }
        });

        characterLabel = new Label("SELEZIONA IL PERSONAGGIO", skin, "white");

        drawTable(mainTable, Gdx.graphics.getWidth() * buttonWidthPercentage, Gdx.graphics.getWidth() * buttonHeightPercentage, Gdx.graphics.getHeight() * buttonPadPercentage, (Gdx.graphics.getHeight() * 0.06f) / scale);
    }

    public void drawTable(Table mainTable, float buttonWidth, float buttonHeight, float pad, float scale) {
        mainTable.center();

        mainTable.row().spaceBottom(Gdx.graphics.getHeight() * 0.06f * 2);
        mainTable.add(characterLabel).colspan(4).padBottom(pad);
        characterLabel.setFontScale(scale);
        mainTable.row().padBottom(Gdx.graphics.getHeight() * 0.06f * 2);
        mainTable.add(characterM).width(buttonWidth).height(buttonHeight).colspan(2).center();
        mainTable.add(characterF).width(buttonWidth).height(buttonHeight).colspan(2).center();
        mainTable.center();
        mainTable.row().spaceBottom(Gdx.graphics.getHeight() * 0.06f);
        mainTable.add(name).width(Gdx.graphics.getWidth() * 0.2f).height(Gdx.graphics.getHeight() * 0.06f).colspan(4).center();
        name.setAlignment(Align.center);
        mainTable.row().spaceBottom(Gdx.graphics.getHeight() * 0.06f);
        mainTable.add(start).width(Gdx.graphics.getWidth() * 0.2f).height(Gdx.graphics.getHeight() * 0.06f).colspan(4).center();
        mainTable.row().padBottom(pad);

        // Add table to stage
        stage.addActor(mainTable);
    }

    @Override
    public Stage getStage() {
        return this.stage;
    }

    @Override
    public Table getTable(){
        return this.mainTable;
    }

    @Override
    public void dispose(){

        // Libera lo Stage
        if (this.stage != null) {
            this.stage.clear(); // Rimuove tutti gli attori
            this.stage.dispose(); // Libera le risorse dello Stage
            this.stage = null;
        }

        // Libera il Viewport (nessun dispose necessario, solo nullify)
        this.viewport = null;

        // Chiamata al dispose della superclasse
        super.dispose();

        // Log per debug
        Gdx.app.log("PauseMenu", "Risorse liberate correttamente.");
    }

    @Override
    public float getButtonWidthPercentage() {
        return this.buttonWidthPercentage;
    }

    @Override
    public float getButtonHeightPercentage() {
        return this.buttonHeightPercentage;
    }

    @Override
    public float getButtonPadPercentage() {
        return this.buttonPadPercentage;
    }

    @Override
    public float getScale() {
        return this.scale;
    }

    public void appearanceAnimation()
    {
        mainTable.clearActions();
        mainTable.getColor().a = 0;
        GameScreen.stageInventory.addAction(Actions.fadeOut(0.1f));
        mainTable.addAction(Actions.fadeIn(0.1f));
    }

    public void animateDisappearance(Runnable onComplete) {
        mainTable.clearActions();
        GameScreen.stageInventory.getDebugColor().a = 0;
        GameScreen.stageInventory.addAction(Actions.fadeIn(0.1f));
        mainTable.addAction(Actions.sequence(
            Actions.fadeOut(0.1f),
            Actions.run(() -> {
                if(onComplete != null)
                {
                    onComplete.run();
                    ((GameScreen)((Dinner)Gdx.app.getApplicationListener()).getScreen()).playerController.releaseAllKeys();
                }
            })
        ));
    }

}

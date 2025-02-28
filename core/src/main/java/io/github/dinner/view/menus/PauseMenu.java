package io.github.dinner.view.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.dinner.Dinner;
import io.github.dinner.Dinner.GameState;
import io.github.dinner.controller.AudioController;
import io.github.dinner.controller.MenuController;
import io.github.dinner.controller.PauseController;
import io.github.dinner.controller.SaveController;
import io.github.dinner.model.states.MenuState;
import io.github.dinner.view.screens.GameScreen;
import io.github.dinner.view.screens.MenuScreen;

public class PauseMenu extends MenuState {

    Stage stage;
    private Viewport viewport;
    Dinner game = (Dinner) Gdx.app.getApplicationListener();
    float buttonWidthPercentage;
    float buttonHeightPercentage;
    float buttonPadPercentage;
    float scale;
    Table mainTable;
    TextButton resumeButton;
    TextButton saveButton;
    TextButton optionsButton;
    TextButton exitButton;
    Label pauseMenuLabel;

    public PauseMenu(MenuController loader) {
        super(loader);

        this.viewport = new ScreenViewport();
        this.stage = new Stage(viewport);
        this.buttonWidthPercentage = 0.20f; // 20% della larghezza della finestra
        this.buttonHeightPercentage = 0.06f; // 6% dell'altezza della finestra
        this.buttonPadPercentage = 0.2f; // 20% dell'altezza
        this.scale = 50f;

        mainTable = new Table();
        mainTable.setBackground(skin.getDrawable("Pause"));
        mainTable.setFillParent(true);

        resumeButton = new TextButton("Riprendi", skin);
        saveButton = new TextButton("Salva", skin);
        optionsButton = new TextButton("Impostazioni", skin);
        exitButton = new TextButton("Vai al menu", skin);

        resumeButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                AudioController.stopAllSounds();
                AudioController.playSound("click_down", false);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                AudioController.playSound("click_up", false);
                PauseController.resumeGame();
            }
        });

        saveButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                AudioController.playSound("click_down", false);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                AudioController.playSound("click_up", false);
                loader.removeInputProcessor(stage);
                loader.getStage().dispose();
                loader.changeState(new SaveMenu(loader, new PauseMenu(loader)));
                loader.addInputProcessor(loader.getStage());
                Gdx.app.log("PauseMenu", "saveButton cliccato");
            }
        });

        optionsButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                AudioController.playSound("click_down", false);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                AudioController.playSound("click_up", false);
                loader.removeInputProcessor(stage);
                loader.getStage().dispose();
                loader.changeState(new SettingsMenu(loader, new PauseMenu(loader)));
                loader.addInputProcessor(loader.getStage());
            }
        });

        exitButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                AudioController.playSound("click_down", false);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                AudioController.playSound("click_up", false);
                Screen menuScreen = new MenuScreen();
                SaveController.getInstance().stopAutoSaveTimer();

                FileHandle file = Gdx.files.local("save_slot_0_pc");

                if(file.exists()) {
                    file.delete();
                }

                //dispose dello stage del menu di pausa non necessario perché lo fa il dispose del GameScreen

                game.gameState = GameState.RUNNING;
                game.getScreen().dispose();
                game.setScreen(menuScreen);

                AudioController.stopMusic();
                AudioController.playMusic("MainMenu", true);
            }
        });

        pauseMenuLabel = new Label("PAUSA", skin);

        drawTable(mainTable, Gdx.graphics.getWidth() * buttonWidthPercentage, Gdx.graphics.getHeight() * buttonHeightPercentage, Gdx.graphics.getHeight() * buttonPadPercentage, (Gdx.graphics.getHeight() * buttonHeightPercentage) / scale);
    }

    public void drawTable(Table mainTable, float buttonWidth, float buttonHeight, float pad, float scale) {
        mainTable.center();

        mainTable.row().spaceBottom(30);
        mainTable.add(pauseMenuLabel).colspan(4).padTop(pad);
        pauseMenuLabel.setFontScale(scale * 1.5f);
        mainTable.row().spaceBottom(10);
        mainTable.add(resumeButton).width(buttonWidth).height(buttonHeight);
        resumeButton.getLabel().setFontScale(scale);
        mainTable.row().spaceBottom(10);
        mainTable.add(saveButton).width(buttonWidth).height(buttonHeight);
        saveButton.getLabel().setFontScale(scale);
        mainTable.row().spaceBottom(10);
        mainTable.add(optionsButton).width(buttonWidth).height(buttonHeight);
        optionsButton.getLabel().setFontScale(scale);
        mainTable.row().spaceBottom(10);
        mainTable.add(exitButton).width(buttonWidth).height(buttonHeight).padBottom(pad);
        exitButton.getLabel().setFontScale(scale);

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

package io.github.dinner.view.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.dinner.Dinner;
import io.github.dinner.controller.AudioController;
import io.github.dinner.controller.MenuController;
import io.github.dinner.model.states.MenuState;
//import io.github.dinner.controller.SaveController;


public class MainMenu extends MenuState {
    private Stage stage;
    private Viewport viewport;
    private Dinner game;
    float buttonWidthPercentage;
    float buttonHeightPercentage;
    float buttonPadPercentage;
    float scale;
    Table mainTable;
    TextButton playButton;
    TextButton loadButton;
    TextButton optionsButton;
    TextButton exitButton;

    public MainMenu(MenuController loader) {
        super(loader);
        this.game = (Dinner) Gdx.app.getApplicationListener();

        this.viewport = new ScreenViewport();
        this.stage = new Stage(viewport);
        this.buttonWidthPercentage = 0.20f; // 20% della larghezza della finestra
        this.buttonHeightPercentage = 0.06f; // 6% dell'altezza della finestra
        this.buttonPadPercentage = 0.2f; // 20% dell'altezza
        this.scale = 50f;

        mainTable = new Table();
        mainTable.setFillParent(true);

        mainTable.setBackground(skin.getDrawable("background"));

        playButton = new TextButton("NUOVA PARTITA", skin);
        loadButton = new TextButton("CARICA", skin);
        optionsButton = new TextButton("IMPOSTAZIONI", skin);
        exitButton = new TextButton("ESCI", skin);

        playButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                AudioController.playSound("click_down", false);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                AudioController.playSound("click_up", false);
                AudioController.stopMusic();
                loader.changeState(new CharacterSelectionMenu(loader));
            }
        });

        loadButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                AudioController.playSound("click_down", false);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                AudioController.playSound("click_up", false);
                loader.changeState(new SaveMenu(loader, new MainMenu(loader)));
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
                loader.changeState(new SettingsMenu(loader, new MainMenu(loader)));
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
                Gdx.app.exit();
            }
        });

        drawTable(mainTable, Gdx.graphics.getWidth() * buttonWidthPercentage, Gdx.graphics.getHeight() * buttonHeightPercentage, Gdx.graphics.getHeight() * buttonPadPercentage, (Gdx.graphics.getHeight() * buttonHeightPercentage) / scale);

    }

    public void drawTable(Table mainTable, float buttonWidth, float buttonHeight, float pad, float scale) {
        mainTable.center();

        mainTable.row().spaceBottom(10);
        mainTable.add(playButton).width(buttonWidth).height(buttonHeight).padTop(pad+50);
        playButton.getLabel().setFontScale(scale);
        mainTable.row().spaceBottom(10);
        mainTable.add(loadButton).width(buttonWidth).height(buttonHeight);
        loadButton.getLabel().setFontScale(scale);
        mainTable.row().spaceBottom(10);
        mainTable.add(optionsButton).width(buttonWidth).height(buttonHeight);
        optionsButton.getLabel().setFontScale(scale);
        mainTable.row().spaceBottom(15);
        mainTable.add(exitButton).width(buttonWidth).height(buttonHeight).padBottom(30);
        exitButton.getLabel().setFontScale(scale);

        stage.addActor(mainTable);
    }

    @Override
    public Stage getStage() {
        return this.stage;
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
        Gdx.app.log("MainMenu", "Risorse liberate correttamente.");
    }

    @Override
    public Table getTable(){
        return this.mainTable;
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
}

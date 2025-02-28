package io.github.dinner.view.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.dinner.Dinner;
import io.github.dinner.controller.AudioController;
import io.github.dinner.controller.MenuController;
import io.github.dinner.controller.SettingsController;
import io.github.dinner.model.states.MenuState;
import io.github.dinner.view.screens.GameScreen;

public class SettingsMenu extends MenuState {

    private Stage stage;
    private Viewport viewport;
    private MenuState backState;
    Dinner game = (Dinner) Gdx.app.getApplicationListener();
    float scale;
    float buttonWidthPercentage;
    float buttonHeightPercentage;
    float buttonPadPercentage;
    Table mainTable;
    Slider musicVolumeSlider;
    Slider soundVolumeSlider;
    CheckBox fullscreenCheckbox;
    SelectBox<String> resolutionsSelectBox;
    CheckBox vsyncCheckbox;
    TextButton backButton;
    Label settingsLabel;
    Label mvolumeLabel;
    Label svolumeLabel;
    Label fullScreenLabel;
    Label resolutionLabel;
    Label vSyncLabel;


    public SettingsMenu(MenuController loader, MenuState backState) {
        super(loader);
        this.backState = backState;
        SettingsController.init();
        this.buttonWidthPercentage = 0.20f; // 20% della larghezza della finestra
        this.buttonHeightPercentage = 0.06f; // 6% dell'altezza della finestra
        this.buttonPadPercentage = 0.2f; // 20% dell'altezza
        this.scale = 50f;

        viewport = new ScreenViewport();
        stage = new Stage(viewport);

        // inizializza Table
        mainTable = new Table();
        if(Dinner.gameState == Dinner.GameState.PAUSED)
            mainTable.setBackground(skin.getDrawable("Pause"));
        if(Dinner.gameState == Dinner.GameState.RUNNING)
            mainTable.setBackground(skin.getDrawable("PauseMainMenu"));
        mainTable.setFillParent(true);

        Container<TextButton> container = new Container<>();

        // Create buttons
        musicVolumeSlider = new Slider(0f, 1f, 0.1f, false, skin);
        musicVolumeSlider.setValue(SettingsController.getMusicVolume());
        musicVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SettingsController.saveMusicVolume(musicVolumeSlider.getValue());
                SettingsController.setMusicVolume();
            }
        });

        soundVolumeSlider = new Slider(0f, 1f, 0.1f, false, skin);
        soundVolumeSlider.setValue(SettingsController.getSoundVolume());
        soundVolumeSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                SettingsController.saveSoundVolume(soundVolumeSlider.getValue());
                SettingsController.setSoundVolume();
            }
        });

        fullscreenCheckbox = new CheckBox(null, skin);
        fullscreenCheckbox.setChecked(SettingsController.isFullscreen());
        fullscreenCheckbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioController.playSound("check", false);
                boolean isChecked = fullscreenCheckbox.isChecked();
                SettingsController.saveFullscreen(isChecked);
                if(isChecked) {
                    SettingsController.setFullScreen();
                } else {
                    SettingsController.setScreenSize();
                }
                mainTable.clear();
                drawTable(mainTable, Gdx.graphics.getWidth() * buttonWidthPercentage, Gdx.graphics.getHeight() * buttonHeightPercentage, Gdx.graphics.getHeight() * buttonPadPercentage, (Gdx.graphics.getHeight() * buttonHeightPercentage) / scale);
            }
        });

        resolutionsSelectBox = new SelectBox<>(skin);
        resolutionsSelectBox.setItems(SettingsController.getResolutions());
        resolutionsSelectBox.setSelected(SettingsController.getScreenWidth() + "x" + SettingsController.getScreenHeight());

        resolutionsSelectBox.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                AudioController.playSound("click_down", false);
                return super.touchDown(event, x, y, pointer, button);
            }
        });

        resolutionsSelectBox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioController.playSound("click_up", false);
                String selectedResolution = resolutionsSelectBox.getSelected();
                String[] dimensions = selectedResolution.split("x");
                int width = Integer.parseInt(dimensions[0]);
                int height = Integer.parseInt(dimensions[1]);
                SettingsController.saveScreenSize(width, height);
                if(!fullscreenCheckbox.isChecked()){
                    SettingsController.setScreenSize();
                    mainTable.clear();
                    drawTable(mainTable, Gdx.graphics.getWidth() * buttonWidthPercentage, Gdx.graphics.getHeight() * buttonHeightPercentage, Gdx.graphics.getHeight() * buttonPadPercentage, (Gdx.graphics.getHeight() * buttonHeightPercentage) / scale);
                    if(GameScreen.stageInventory!=null)
                        GameScreen.updateInventoryUI();
                }
            }
        });

        vsyncCheckbox = new CheckBox(null, skin);
        vsyncCheckbox.setChecked(SettingsController.isVsync());
        vsyncCheckbox.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                AudioController.playSound("check", false);
                SettingsController.saveVsync(vsyncCheckbox.isChecked());
                SettingsController.setVsync();
            }
        });

        backButton = new TextButton("Indietro", skin);

        // Add listeners to buttons

        if (game.gameState == Dinner.GameState.RUNNING) {
            backButton.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    AudioController.playSound("click_down", false);
                    return super.touchDown(event, x, y, pointer, button);
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    AudioController.playSound("click_up", false);
                    loader.getStage().dispose();
                    loader.changeState(backState);
                }
            });
        } else {
            backButton.addListener(new ClickListener() {
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
                    loader.changeState(backState);
                    loader.addInputProcessor(loader.getStage());
                }
            });
        }

        stage.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ESCAPE) {
                    loader.changeState(backState);
                }
                return false;
            }
        });

        settingsLabel = new Label("IMPOSTAZIONI", skin);
        mvolumeLabel = new Label("Volume Musica: ", skin);
        svolumeLabel = new Label("Volume Suoni: ", skin);
        fullScreenLabel = new Label("Schermo Intero: ", skin);
        resolutionLabel = new Label("Dimensione Finestra: ", skin);
        vSyncLabel = new Label("V-Sync: ", skin);

        drawTable(mainTable, Gdx.graphics.getWidth() * buttonWidthPercentage, Gdx.graphics.getHeight() * buttonHeightPercentage, Gdx.graphics.getHeight() * buttonPadPercentage, (Gdx.graphics.getHeight() * buttonHeightPercentage) / scale);
    }

    public void drawTable(Table mainTable, float buttonWidth, float buttonHeight, float pad, float scale) {
        mainTable.center();

        mainTable.row().spaceBottom(30);
        mainTable.add(settingsLabel).colspan(4).center().padTop(pad); //IMPOSTAZIONI
        settingsLabel.setFontScale(scale * 1.5f);
        mainTable.row().spaceBottom(10);
        //volume musica
        mainTable.add(mvolumeLabel);
        mvolumeLabel.setFontScale(scale);
        mainTable.add(musicVolumeSlider).width(buttonWidth).height(buttonHeight);
        mainTable.row().spaceBottom(10);
        //volume suoni
        mainTable.add(svolumeLabel);
        svolumeLabel.setFontScale(scale);
        mainTable.add(soundVolumeSlider).width(buttonWidth).height(buttonHeight);
        mainTable.row().spaceBottom(10);
        //fullscreen
        mainTable.add(fullScreenLabel);
        fullScreenLabel.setFontScale(scale);
        mainTable.add(fullscreenCheckbox);
        mainTable.row().spaceBottom(10);
        //risoluzione
        mainTable.add(resolutionLabel);
        resolutionLabel.setFontScale(scale);
        mainTable.add(resolutionsSelectBox).width(buttonWidth).height(buttonHeight);
        resolutionsSelectBox.getStyle().font.getData().setScale(scale * 0.9f);
        resolutionsSelectBox.getList().getParent().setHeight(50);
        //qua devo fare il resize della selectbox
        mainTable.row().spaceBottom(10);
        //vsync
        mainTable.add(vSyncLabel);
        vSyncLabel.setFontScale(scale);
        mainTable.add(vsyncCheckbox);
        mainTable.row().spaceBottom(30);
        //indietro
        mainTable.add(backButton).width(buttonWidth).height(buttonHeight).padBottom(pad);
        backButton.getLabel().setFontScale(scale);

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

        // Chiamata a super.dispose() per liberare risorse ereditate
        super.dispose();

        // Log per debug
        Gdx.app.log("SettingsMenu", "Risorse rilasciate correttamente.");
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
    public float getScale(){
        return this.scale;
    }
}

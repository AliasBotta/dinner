package io.github.dinner.view.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.dinner.Dinner;
import io.github.dinner.Dinner.GameState;
import io.github.dinner.controller.AudioController;
import io.github.dinner.controller.MenuController;
import io.github.dinner.controller.SaveController;
import io.github.dinner.model.states.MenuState;
import io.github.dinner.view.screens.GameScreen;

public class SaveMenu extends MenuState {
    private Stage stage;
    private Viewport viewport;
    private MenuState backState;
    Dinner game = (Dinner) Gdx.app.getApplicationListener();
    SaveController saveController;
    float scale;
    float buttonWidthPercentage;
    float buttonHeightPercentage;
    float buttonPadPercentage;
    Table mainTable;
    TextButton autoSaveButton;
    TextButton file1Button;
    TextButton file2Button;
    TextButton file3Button;
    TextButton backButton;
    TextButton deleteAutoSave;
    TextButton deleteFile1Button;
    TextButton deleteFile2Button;
    TextButton deleteFile3Button;
    Label saveMenuLabel;

    public SaveMenu(MenuController loader, MenuState backState) {
        super(loader);

        this.saveController = SaveController.getInstance();

        this.viewport = new ScreenViewport();
        this.stage = new Stage(viewport);
        this.backState = backState;
        this.buttonWidthPercentage = 0.20f; // 20% della larghezza della finestra
        this.buttonHeightPercentage = 0.06f; // 6% dell'altezza della finestra
        this.buttonPadPercentage = 0.2f; // 20% dell'altezza
        this.scale = 50f;

        mainTable = new Table();
        if(Dinner.gameState == Dinner.GameState.PAUSED)
            mainTable.setBackground(skin.getDrawable("Pause"));
        if(Dinner.gameState == Dinner.GameState.RUNNING)
            mainTable.setBackground(skin.getDrawable("PauseMainMenu"));
        mainTable.setFillParent(true);

        autoSaveButton = new TextButton("Auto-Salvataggio", skin);
        file1Button = new TextButton("File 1", skin);
        file2Button = new TextButton("File 2", skin);
        file3Button = new TextButton("File 3", skin);
        backButton = new TextButton("Indietro", skin);
        deleteAutoSave = new TextButton("Elimina", skin);
        deleteFile1Button = new TextButton("Elimina", skin);
        deleteFile2Button = new TextButton("Elimina", skin);
        deleteFile3Button = new TextButton("Elimina", skin);

        if (game.gameState == GameState.RUNNING) {// quando sei nel menu principale
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

            deleteAutoSave.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    AudioController.playSound("click_down", false);
                    return super.touchDown(event, x, y, pointer, button);
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    AudioController.playSound("click_up", false);
                    try {
                        saveController.setCurrentSlot(0);
                        saveController.deleteProgress();
                    } catch (Exception e) {
                        Gdx.app.log("SaveMenu",  e.getMessage());
                    }
                }
            });

            autoSaveButton.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    AudioController.playSound("click_down", false);
                    return super.touchDown(event, x, y, pointer, button);
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    AudioController.playSound("click_up", false);
                    saveController.setCurrentSlot(0);
                    SaveController.GameData dataToLoad = saveController.getOrLoadGameData(true);
                    if(dataToLoad != null) {
                        AudioController.stopMusic();
                        game.setScreen(new GameScreen(game, saveController.getOrLoadGameData(false)));
                        SaveController.getInstance().startAutoSaveTimer();
                    } else {
                        System.out.println("Il file non esiste");
                    }
                }
            });

            file1Button.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    AudioController.playSound("click_down", false);
                    return super.touchDown(event, x, y, pointer, button);
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    AudioController.playSound("click_up", false);
                    saveController.setCurrentSlot(1);

                    FileHandle file = Gdx.files.local("save_slot_1_pc.json");

                    if(file.exists()){
                        FileHandle newFile = Gdx.files.local("save_slot_0_pc.json");
                        System.out.println("Sbrodofloxe");
                        file.copyTo(newFile);
                    }

                    SaveController.GameData dataToLoad = saveController.getOrLoadGameData(true);
                    if(dataToLoad != null) { // se è riuscito a prendere il salvataggio
                        AudioController.stopMusic();
                        game.setScreen(new GameScreen(game, saveController.getOrLoadGameData(false)));
                        SaveController.getInstance().startAutoSaveTimer();
                    } else {
                        System.out.println("Il file non esiste");
                    }
                }
            });

            file2Button.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    AudioController.playSound("click_down", false);
                    return super.touchDown(event, x, y, pointer, button);
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    AudioController.playSound("click_up", false);
                    saveController.setCurrentSlot(2);

                    FileHandle file = Gdx.files.local("save_slot_1_pc.json");

                    if(file.exists()){
                        FileHandle newFile = Gdx.files.local("save_slot_0_pc.json");
                        file.copyTo(newFile);
                    }

                    SaveController.GameData dataToLoad = saveController.getOrLoadGameData(true);
                    if(dataToLoad != null) {
                        AudioController.stopMusic();
                        game.setScreen(new GameScreen(game, saveController.getOrLoadGameData(false)));
                        SaveController.getInstance().startAutoSaveTimer();
                    } else {
                        System.out.println("Il file non esiste");
                    }
                }
            });

            file3Button.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    AudioController.playSound("click_down", false);
                    return super.touchDown(event, x, y, pointer, button);
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    AudioController.playSound("click_up", false);
                    saveController.setCurrentSlot(3);

                    FileHandle file = Gdx.files.local("save_slot_1_pc.json");

                    if(file.exists()){
                        FileHandle newFile = Gdx.files.local("save_slot_0_pc.json");
                        file.copyTo(newFile);
                    }

                    SaveController.GameData dataToLoad = saveController.getOrLoadGameData(true);
                    if(dataToLoad != null) {
                        AudioController.stopMusic();
                        game.setScreen(new GameScreen(game, saveController.getOrLoadGameData(false)));
                        SaveController.getInstance().startAutoSaveTimer();
                    } else {
                        System.out.println("Il file non esiste");
                    }
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

            file1Button.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    AudioController.playSound("click_down", false);
                    return super.touchDown(event, x, y, pointer, button);
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    AudioController.playSound("click_up", false);
                    saveController.setCurrentSlot(1);

                    FileHandle file = Gdx.files.local("save_slot_0_pc.json");

                    if(file.exists()){
                        FileHandle newFile = Gdx.files.local("save_slot_1_pc.json");
                        file.copyTo(newFile);
                    }

                    boolean isSaved = saveController.saveAll(false);
                    Gdx.app.log("SaveMenu", "Salvato in file1: " + isSaved);
                }
            });

            file2Button.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    AudioController.playSound("click_down", false);
                    return super.touchDown(event, x, y, pointer, button);
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    AudioController.playSound("click_up", false);
                    saveController.setCurrentSlot(2);

                    FileHandle file = Gdx.files.local("save_slot_0_pc.json");

                    if(file.exists()){
                        FileHandle newFile = Gdx.files.local("save_slot_2_pc.json");
                        file.copyTo(newFile);
                    }

                    boolean isSaved = saveController.saveAll(false);
                    Gdx.app.log("SaveMenu", "Salvato in file2: " + isSaved);
                }
            });

            file3Button.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    AudioController.playSound("click_down", false);
                    return super.touchDown(event, x, y, pointer, button);
                }

                @Override
                public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                    super.touchUp(event, x, y, pointer, button);
                    AudioController.playSound("click_up", false);
                    saveController.setCurrentSlot(3);

                    FileHandle file = Gdx.files.local("save_slot_0_pc.json");

                    if(file.exists()){
                        FileHandle newFile = Gdx.files.local("save_slot_3_pc.json");
                        file.copyTo(newFile);
                    }

                    boolean isSaved = saveController.saveAll(false);
                    Gdx.app.log("SaveMenu", "Salvato in file3: " + isSaved);
                }
            });
        }



        deleteFile1Button.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                AudioController.playSound("click_down", false);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                AudioController.playSound("click_up", false);
                try {
                    saveController.setCurrentSlot(1);
                    saveController.deleteProgress();
                } catch (Exception e) {
                    Gdx.app.log("SaveMenu",  e.getMessage());
                }
            }
        });

        deleteFile2Button.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                AudioController.playSound("click_down", false);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                AudioController.playSound("click_up", false);
                try {
                    saveController.setCurrentSlot(2);
                    saveController.deleteProgress();
                } catch (Exception e) {
                    Gdx.app.log("SaveMenu",  e.getMessage());
                }
            }
        });

        deleteFile3Button.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                AudioController.playSound("click_down", false);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                AudioController.playSound("click_up", false);
                try {
                    saveController.setCurrentSlot(3);
                    saveController.deleteProgress();
                } catch (Exception e) {
                    Gdx.app.log("SaveMenu",  e.getMessage());
                }
            }
        });

        saveMenuLabel = new Label("SLOT DI SALVATAGGIO", skin);

        drawTable(mainTable, Gdx.graphics.getWidth() * buttonWidthPercentage, Gdx.graphics.getHeight() * buttonHeightPercentage, Gdx.graphics.getHeight() * buttonPadPercentage, (Gdx.graphics.getHeight() * buttonHeightPercentage) / scale);

        Gdx.app.log("SaveMenu", "SaveMenu creato!");
    }

    public void drawTable(Table mainTable, float buttonWidth, float buttonHeight, float pad, float scale) {
        mainTable.center();

        mainTable.row().spaceBottom(30);
        mainTable.add(saveMenuLabel).colspan(2).center().padTop(pad);
        saveMenuLabel.setFontScale(scale * 1.5f);
        mainTable.row().spaceBottom(10);
        if (game.gameState == GameState.RUNNING) {
            mainTable.add(autoSaveButton).pad(10).width(buttonWidth).height(buttonHeight);
            autoSaveButton.getLabel().setFontScale(scale);
            mainTable.add(deleteAutoSave).pad(10).width(buttonWidth).height(buttonHeight);
            deleteAutoSave.getLabel().setFontScale(scale);
        }
        mainTable.row().spaceBottom(10);
        mainTable.add(file1Button).pad(10).width(buttonWidth).height(buttonHeight);
        file1Button.getLabel().setFontScale(scale);
        mainTable.add(deleteFile1Button).pad(10).width(buttonWidth).height(buttonHeight);
        deleteFile1Button.getLabel().setFontScale(scale);
        mainTable.row().spaceBottom(10);
        mainTable.add(file2Button).pad(10).width(buttonWidth).height(buttonHeight);
        file2Button.getLabel().setFontScale(scale);
        mainTable.add(deleteFile2Button).pad(10).width(buttonWidth).height(buttonHeight);
        deleteFile2Button.getLabel().setFontScale(scale);
        mainTable.row().spaceBottom(10);
        mainTable.add(file3Button).pad(10).width(buttonWidth).height(buttonHeight);
        file3Button.getLabel().setFontScale(scale);
        mainTable.add(deleteFile3Button).pad(10).width(buttonWidth).height(buttonHeight);
        deleteFile3Button.getLabel().setFontScale(scale);
        mainTable.row().spaceBottom(10);
        mainTable.add(backButton).width(buttonWidth).height(buttonHeight).padBottom(pad).center();
        backButton.getLabel().setFontScale(scale);

        // Add table to stage
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
            this.stage.dispose(); // Libera risorse dello Stage
            this.stage = null;
        }

        // Libera il Viewport (nullify, nessun dispose necessario)
        this.viewport = null;

        // Chiamata al dispose della superclasse
        super.dispose();

        // Log per debug
        Gdx.app.log("SaveMenu", "Risorse liberate correttamente.");
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

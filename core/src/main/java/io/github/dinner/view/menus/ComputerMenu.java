package io.github.dinner.view.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.dinner.Dinner;
import io.github.dinner.controller.AudioController;
import io.github.dinner.controller.MenuController;
import io.github.dinner.controller.PauseController;
import io.github.dinner.controller.SaveController;
import io.github.dinner.model.Player;
import io.github.dinner.model.states.MenuState;
import io.github.dinner.view.screens.GameScreen;
import io.github.dinner.view.screens.TransitionScreen;

public class ComputerMenu extends MenuState {

    Stage stage;
    private Viewport viewport;
    Dinner game = (Dinner) Gdx.app.getApplicationListener();
    Skin pc_skin;
    float buttonWidthPercentage;
    float buttonHeightPercentage;
    float buttonPadPercentage;
    float scale;
    Table mainTable;
    TextButton resumeButton;
    TextButton notebookButton;
    TextButton phaseLoadButton;
    TextButton newPhaseButton;
    Label titleLabel;
    Label terminalLabel;
    boolean isMouseInside;
    TextureAtlas textureAtlas = new TextureAtlas(Gdx.files.internal("animation/turn_off/turn_off.atlas"));
    Animation<TextureRegion> animation = new Animation<>(1f / 30f, textureAtlas.getRegions(), Animation.PlayMode.NORMAL);

    public ComputerMenu(MenuController loader) {
        super(loader);

        isMouseInside = false;

        AudioController.soundMap.get("pc_noise_1").setOnCompletionListener(music -> {
            AudioController.stopAllSounds();
            AudioController.playSound("pc_noise_2", true);
        });

        AudioController.playSound("pc_text_printing", false);
        AudioController.playSound("pc_noise_1", false);

        this.viewport = new ScreenViewport();
        this.stage = new Stage(viewport);
        this.skin = new Skin(Gdx.files.internal("computer_ui_assets/skin/uiskin.json"));
        this.buttonWidthPercentage = 0.15f; // 15% della larghezza della finestra
        this.buttonHeightPercentage = 0.05f; // 5% dell'altezza della finestra
        this.buttonPadPercentage = 0.05f; // 20% dell'altezza
        this.scale = 35f;

        mainTable = new Table();
        mainTable.setBackground(skin.getDrawable("cursor-black"));
        mainTable.setFillParent(true);

        resumeButton = new TextButton("SPEGNI PC", skin);
        notebookButton = new TextButton("BLOCCO NOTE", skin);
        phaseLoadButton = new TextButton("RIGIOCA ATTO", skin);
        newPhaseButton = new TextButton("NUOVO ATTO", skin);

        resumeButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                AudioController.playSound("pc_click_1", false);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                AudioController.stopAllSounds();
                AudioController.playSound("pc_click_2", false);
                PauseController.resumeGame();
            }
        });

        notebookButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                AudioController.playSound("pc_click_1", false);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                AudioController.playSound("pc_click_2", false);
                loader.removeInputProcessor(stage);
                loader.getStage().dispose();
                NotebookMenu notebookMenu = new NotebookMenu(loader, new ComputerMenu(loader));
                loader.changeState(notebookMenu);
                loader.addInputProcessor(loader.getStage());
                notebookMenu.appearanceAnimation();
            }
        });

        phaseLoadButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                AudioController.playSound("pc_click_1", false);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                AudioController.playSound("pc_click_2", false);
                SaveController.GameData dataToLoad = SaveController.getInstance().getOrLoadPcGameData(true);
                if(dataToLoad != null) {
                    AudioController.stopMusic();
                    AudioController.stopSound("pc_noise_1");
                    AudioController.stopSound("pc_noise_2");
                    game.setScreen(new GameScreen(game, dataToLoad));
                } else {
                    System.out.println("Il file non esiste");
                }
            }
        });

        newPhaseButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                AudioController.playSound("pc_click_1", false);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                AudioController.playSound("pc_click_2", false);

                if(GameScreen.phase < 1){
                    AudioController.stopMusic();
                    AudioController.stopSound("pc_noise_1");
                    AudioController.stopSound("pc_noise_2");
                    SaveController.getInstance().stopAutoSaveTimer();
                    TransitionScreen transition = new TransitionScreen(null,
                        "newPhaseCutsceneLevel",
                        (Dinner) Gdx.app.getApplicationListener(),
                        10, 6,
                        Player.PlayerDirection.NORTH,
                        (GameScreen) ((Dinner) Gdx.app.getApplicationListener()).getScreen(),
                        1,
                        0.04f
                    );
                    ((Dinner) Gdx.app.getApplicationListener()).setScreen(transition);
                    Player.getPlayer().setVisible(true);
                }
            }
        });


        this.titleLabel = new Label("\n\n  ********** Terminale villa Capecchi - Menu Principale **********", skin, "default");
        this.terminalLabel = new Label("\n  ********** RAM TOTALE DISPONIBILE: 64KB\n\n  ********** ROM: 20KB\n\n  ********** CPU: 0.985 MHz\n\n  ********** PRONTO\n\n  ************************************************************************\n\n  >  Software in uso: MainSystem/Blocco_NoteVer1.2.1.exe\n\n  >  SELEZIONA LA FUNZIONALITA':\n\n", skin, "default");

        drawTable(mainTable, Gdx.graphics.getWidth() * buttonWidthPercentage, Gdx.graphics.getHeight() * buttonHeightPercentage, Gdx.graphics.getHeight() * buttonPadPercentage, (Gdx.graphics.getHeight() * buttonHeightPercentage) / scale);
    }

    public void drawTable(Table mainTable, float buttonWidth, float buttonHeight, float pad, float scale) {
        mainTable.top().left();

        mainTable.row().spaceBottom(2);
        mainTable.add(titleLabel).colspan(4).align(Align.left);
        titleLabel.setFontScale(scale);
        mainTable.row().spaceBottom(4);
        mainTable.add(terminalLabel).colspan(4).align(Align.left);
        terminalLabel.setFontScale(scale);
        mainTable.row().spaceBottom(4);
        mainTable.add(notebookButton).width(buttonWidth).height(buttonHeight * 0.5f).align(Align.left).padLeft(20);
        notebookButton.getLabel().setFontScale(scale);
        mainTable.row().spaceBottom(4);
        mainTable.add(phaseLoadButton).width(buttonWidth).height(buttonHeight * 0.5f).align(Align.left).padLeft(20);
        phaseLoadButton.getLabel().setFontScale(scale);
        mainTable.row().spaceBottom(4);
        mainTable.add(newPhaseButton).width(buttonWidth).height(buttonHeight * 0.5f).align(Align.left).padLeft(20);
        newPhaseButton.getLabel().setFontScale(scale);
        mainTable.row().spaceBottom(4);
        mainTable.add(resumeButton).width(buttonWidth).height(buttonHeight * 0.5f).align(Align.left).padLeft(20);
        resumeButton.getLabel().setFontScale(scale);

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

    public void appearanceAnimation() {
        // Creare una schermata nera dinamicamente
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 1); // Nero pieno
        pixmap.fill();
        Texture blackTexture = new Texture(pixmap);
        pixmap.dispose(); // Rilascia il Pixmap per evitare sprechi di memoria

        Image blackOverlay = new Image(blackTexture);
        blackOverlay.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // Copre tutto lo schermo
        blackOverlay.setPosition(0, 0); // Posizionato inizialmente a copertura totale
        stage.addActor(blackOverlay); // Aggiungi la schermata nera allo stage

        // Iniziare l'animazione
        Timer.schedule(new Timer.Task() {
            float elapsed = 0f;
            float duration = 1f; // Durata totale dell'animazione
            float targetHeight = Gdx.graphics.getHeight();

            @Override
            public void run() {
                elapsed += Gdx.graphics.getDeltaTime();
                if (elapsed >= duration) {
                    // Completa l'animazione: rimuovi la schermata nera
                    blackOverlay.remove(); // Rimuovi l'actor dallo stage
                    blackTexture.dispose(); // Rilascia la texture dalla memoria
                    cancel();
                } else {
                    // Calcola l'altezza rimanente
                    float progress = elapsed / duration;
                    float newHeight = targetHeight * (1 - progress);

                    // Aggiorna la dimensione e la posizione per la schermata nera
                    blackOverlay.setSize(blackOverlay.getWidth(), newHeight);
                    blackOverlay.setPosition(0, (Gdx.graphics.getHeight() - newHeight) / 2f); // Centrare
                }
            }
        }, 0, 1 / 60f); // Aggiorna a 60 FPS
    }

    public void animateDisappearance(Runnable onComplete) {
        final float[] stateTime = {0f};
        Actor animationActor = new Actor() {
            @Override
            public void act(float delta) {
                super.act(delta);
                stateTime[0] += delta; // Aggiorna il tempo
            }
            @Override
            public void draw(Batch batch, float parentAlpha) {
                super.draw(batch, parentAlpha);

                TextureRegion currentFrame = animation.getKeyFrame(stateTime[0]);
                batch.draw(currentFrame, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

                if (animation.isAnimationFinished(stateTime[0])) {
                    this.remove();

                    textureAtlas.dispose();

                    if (onComplete != null) {
                        onComplete.run();
                        ((GameScreen)((Dinner)Gdx.app.getApplicationListener()).getScreen()).playerController.releaseAllKeys();
                    }
                }
            }
        };
        stage.addActor(animationActor);
    }
}

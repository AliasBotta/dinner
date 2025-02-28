package io.github.dinner;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.dinner.controller.AudioController;
import io.github.dinner.controller.SettingsController;
import io.github.dinner.model.dialog.Dialogs;
import io.github.dinner.view.screens.GameScreen;
import io.github.dinner.view.screens.MenuScreen;

public class Dinner extends Game {

    public enum GameState {
        RUNNING,
        PAUSED,
        COMPUTER,
        LOADING,
        CUTSCENE
    }

    public static GameState gameState = GameState.RUNNING;
    public SpriteBatch batch;
    public static Skin skin;
    private AssetManager assetManager;
    public ShapeRenderer shapeRenderer;
    public static boolean isNewGame;
    public GameScreen currentGameScreen;

    @Override
    public void create() {
        shapeRenderer = new ShapeRenderer();
        assetManager = new AssetManager();
        batch = new SpriteBatch();
        assetManager.load("uiskin.atlas", TextureAtlas.class);
        assetManager.load("default.fnt", BitmapFont.class);
        assetManager.finishLoading();
        skin = new Skin(Gdx.files.internal("uiskin.json"));
        AudioController.initializeMusic();
        AudioController.initializeSound();
        // Progress.initializeProgressMap();
        Dialogs.initDoorDialogs();
        SettingsController.init(); // Inizializza e applica le impostazioni
        if (Gdx.gl != null) {
            Gdx.gl.glDisable(0x809D); // Disabilita il multisampling
        }
        setScreen(new MenuScreen());

        AudioController.playMusic("MainMenu", true);
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void setScreen(Screen screen) {
        if (this.screen != null) {
            this.screen.hide();
        }

        this.screen = screen;
        if (this.screen != null) {
            this.screen.show();
            //this.screen.resize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        skin.dispose();
    }
}

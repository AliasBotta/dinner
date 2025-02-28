package io.github.dinner.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.scenes.scene2d.Actor;
import io.github.dinner.Dinner;
import io.github.dinner.controller.MenuController;
import com.badlogic.gdx.audio.Music;
import io.github.dinner.controller.SettingsController;
//import io.github.dinner.controller.SettingController;

public class MenuScreen implements Screen {

    MenuController menuController;
    private OrthographicCamera camera;
    Dinner game = (Dinner) Gdx.app.getApplicationListener();
    BitmapFont debugFont;
    SpriteBatch batch;

    public MenuScreen() {

        Pixmap pixmap = new Pixmap(Gdx.files.internal("cursor.png"));
        Cursor cursor = Gdx.graphics.newCursor(pixmap, 0, 0);
        Gdx.graphics.setCursor(cursor);
        pixmap.dispose();

        menuController = new MenuController();
        batch = new SpriteBatch();

        camera = new OrthographicCamera();
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0);
        camera.update();
    }
    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        menuController.getStage().act();
        menuController.getStage().draw();
        Gdx.input.setInputProcessor(menuController.getStage());
        //this.batch.begin();
        //this.batch.end();
    }

    @Override
    public void resize(int width, int height) {
        menuController.getStage().getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        // Libera lo Stage del MenuController
        if (menuController != null && menuController.getStage() != null) {
            menuController.getStage().clear(); // Rimuove tutti gli attori
            menuController.getStage().dispose(); // Libera risorse dello Stage
        }

        // Rimuovi il processore di input per evitare riferimenti pendenti
        if (Gdx.input.getInputProcessor() == menuController.getStage()) {
            Gdx.input.setInputProcessor(null);
        }

        // Libera il font
        if (debugFont != null) {
            debugFont.dispose();
            debugFont = null;
        }

        // Libera il batch
        if (batch != null) {
            batch.dispose();
            batch = null;
        }

        // Log per debug
        Gdx.app.log("MenuScreen", "Risorse liberate correttamente.");
    }

}

package io.github.dinner.model.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import io.github.dinner.controller.MenuController;


public abstract class MenuState implements Disposable {
    protected MenuController loader;
    private TextureAtlas atlas;
    protected Skin skin;
    protected float r, g, b;

    public MenuState(MenuController loader) {
        atlas = new TextureAtlas("custom_ui_assets/skin/SKIN.atlas");
        skin = new Skin(Gdx.files.internal("custom_ui_assets/skin/SKIN.json"), atlas);
        this.loader = loader;
        this.r = 0.255f;
        this.g = 0.365f;
        this.b = 0.263f;
    }

    public abstract Stage getStage();

    @Override
    public void dispose(){
        if (skin != null) {
            skin.dispose();
            skin = null;
        }
        if (atlas != null) {
            atlas.dispose();
            atlas = null;
        }

        Gdx.app.debug("MenuState", "MenuState: Risorse rilasciate.");
    }

    public abstract Table getTable();

    public abstract void drawTable(Table mainTable, float buttonWidthPercentage, float buttonHeightPercentage, float padPercentage, float scale);

    public abstract float getButtonWidthPercentage();

    public abstract float getButtonHeightPercentage();

    public abstract float getButtonPadPercentage();

    public abstract float getScale();
}

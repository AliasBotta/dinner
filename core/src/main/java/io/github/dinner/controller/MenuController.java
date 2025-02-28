package io.github.dinner.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import io.github.dinner.view.menus.MainMenu;
import io.github.dinner.model.states.MenuState;

public class MenuController {
    MenuState currentMenu;

    public MenuController() {
        this.currentMenu = new MainMenu(this);
    }

    //qui dice che il costruttore non viene mai usato, a che serve?
    public MenuController(MenuState menu) {
        this.currentMenu = menu;
    }

    public void changeState(MenuState menu) {

        this.currentMenu = menu;
        // fix viewport dopo back
        currentMenu.getStage().getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        currentMenu.getTable().clear();
        currentMenu.drawTable(currentMenu.getTable(), Gdx.graphics.getWidth() * currentMenu.getButtonWidthPercentage(), Gdx.graphics.getHeight() * currentMenu.getButtonHeightPercentage(), Gdx.graphics.getHeight() * currentMenu.getButtonPadPercentage(), (Gdx.graphics.getHeight() * currentMenu.getButtonHeightPercentage())/currentMenu.getScale());
    }

    public void addInputProcessor(Stage stage){
        if(!Gdx.input.getInputProcessor().equals(stage)){
            ((InputMultiplexer) Gdx.input.getInputProcessor()).addProcessor(stage);
        }
    }

    public void removeInputProcessor(Stage stage) {
        ((InputMultiplexer) Gdx.input.getInputProcessor()).removeProcessor(stage);
    }

    public Stage getStage() {
        return currentMenu.getStage();
    }

    public MenuState getCurrentMenu() {
        return currentMenu;
    }
}

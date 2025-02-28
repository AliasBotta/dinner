package io.github.dinner.controller;

import com.badlogic.gdx.Input.Keys;
import io.github.dinner.Dinner;
import io.github.dinner.Dinner.GameState;
import com.badlogic.gdx.InputAdapter;
import io.github.dinner.view.menus.ComputerMenu;
import io.github.dinner.model.states.MenuState;
import io.github.dinner.view.menus.PauseMenu;
import io.github.dinner.view.screens.GameScreen;

public class PauseController extends InputAdapter {

    Dinner game;
    static MenuState menuState;

    public PauseController(Dinner game) {
        this.game = game;
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode == Keys.ESCAPE && !GameScreen.isDialogueBoxVisible) {
            if(Dinner.gameState.equals(GameState.RUNNING)) {
                pauseGame();
            } else if(Dinner.gameState.equals(GameState.PAUSED) || Dinner.gameState.equals(GameState.COMPUTER)){
                resumeGame();
            }
            else
            {
                return false;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    public static void pauseGame(){
        Dinner.gameState = GameState.PAUSED;
        AudioController.stopAllSounds();
        AudioController.playSound("flush", false);
    }

    public static void resumeGame() {
        AudioController.stopAllSounds();
        menuState = GameScreen.menuController.getCurrentMenu();
        if(menuState instanceof PauseMenu) {
            AudioController.playSound("flush2", false);
            ((PauseMenu) menuState).animateDisappearance(() -> Dinner.gameState = GameState.RUNNING);
        }
        else if(menuState instanceof ComputerMenu)
        {
            AudioController.stopSound("pc_text_printing");
            AudioController.stopSound("pc_noise_1");
            AudioController.stopSound("pc_noise_2");
            AudioController.playSound("pc_turn_off", false);
            ((ComputerMenu) menuState).animateDisappearance(() -> Dinner.gameState = GameState.RUNNING);
        }
        else
        {
            AudioController.playSound("flush2", false);
            Dinner.gameState = GameState.RUNNING;
        }
    }
}

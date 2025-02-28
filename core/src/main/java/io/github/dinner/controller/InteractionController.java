package io.github.dinner.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import io.github.dinner.Dinner;
import io.github.dinner.model.Player;

public class InteractionController extends InputAdapter {
    private InteractableObjectController interactableObjectController;
    private Texture textureInteractionWidget;

    public InteractionController(InteractableObjectController interactableObjectController) {
        this.interactableObjectController = interactableObjectController;
        this.textureInteractionWidget = new Texture(Gdx.files.internal("custom_hud_assets/interaction.png"));
    }

    private void displayWidgetIfNear(SpriteBatch batch, Player player, Rectangle objectBox, float offsetX, float offsetY) {
        if ((player.getBox().x == objectBox.x && player.getBox().y == objectBox.y - 1 && player.getDirection() == Player.PlayerDirection.NORTH) ||
            (player.getBox().x == objectBox.x && player.getBox().y == objectBox.y + 1 && player.getDirection() == Player.PlayerDirection.SOUTH) ||
            (player.getBox().y == objectBox.y && player.getBox().x == objectBox.x - 1 && player.getDirection() == Player.PlayerDirection.EAST) ||
            (player.getBox().y == objectBox.y && player.getBox().x == objectBox.x + 1 && player.getDirection() == Player.PlayerDirection.WEST)) {

            float widgetX = player.getBox().x;
            float widgetY = player.getBox().y;

            if (player.getDirection() == Player.PlayerDirection.NORTH) {
                widgetY -= 0.83f;
            } else {
                widgetY += player.getBox().getHeight();
            }

            batch.draw(textureInteractionWidget, widgetX + offsetX, widgetY + offsetY, 0.5f, 0.5f);
        }

        if (objectBox.height >= 2) {
            if (((player.getBox().y >= objectBox.y && player.getBox().y <= objectBox.y + objectBox.height-1) &&
                player.getBox().x == objectBox.x - 1 && player.getDirection() == Player.PlayerDirection.EAST) ||
                ((player.getBox().y >= objectBox.y && player.getBox().y <= objectBox.y + objectBox.height-1) &&
                    player.getBox().x == objectBox.x + 1 && player.getDirection() == Player.PlayerDirection.WEST)) {

                float widgetX = player.getBox().x;
                float widgetY = player.getBox().y;

                if (player.getDirection() == Player.PlayerDirection.NORTH) {
                    widgetY -= 0.83f;
                } else {
                    widgetY += player.getBox().getHeight();
                }

                batch.draw(textureInteractionWidget, widgetX + offsetX, widgetY + offsetY, 0.5f, 0.5f);
            }
        }

        if (objectBox.width >= 2) {
            if (((player.getBox().x >= objectBox.x && player.getBox().x <= objectBox.x + objectBox.width-1) &&
                player.getBox().y == objectBox.y - 1 && player.getDirection() == Player.PlayerDirection.NORTH) ||
                ((player.getBox().x >= objectBox.x && player.getBox().x <= objectBox.x + objectBox.width-1) &&
                    player.getBox().y == objectBox.y + 1 && player.getDirection() == Player.PlayerDirection.SOUTH)) {

                float widgetX = player.getBox().x;
                float widgetY = player.getBox().y;

                if (player.getDirection() == Player.PlayerDirection.NORTH) {
                    widgetY -= 0.83f;
                } else {
                    widgetY += player.getBox().getHeight();
                }

                batch.draw(textureInteractionWidget, widgetX + offsetX, widgetY + offsetY, 0.5f, 0.5f);
            }
        }
    }




    public void displayInteractionWidget(SpriteBatch batch) {
        Player player = Player.getPlayer();

        interactableObjectController.forEachNpc(npc ->
            displayWidgetIfNear(batch, player, npc.getBox(), 0.25f, 0.25f));

        interactableObjectController.forEachItem(item ->
            displayWidgetIfNear(batch, player, item.getBox(), 0.25f, 0.25f));//da capire a che serve settare gli offset, e perché negli item sono settati diversamente dagli NPC

        interactableObjectController.forEachInteractableObject(interactableObject ->
            displayWidgetIfNear(batch, player, interactableObject.getBox(), 0.25f, 0.25f));
    }

    @Override
    public boolean keyDown(int keycode) {
        if (keycode != Input.Keys.E || Dinner.gameState.equals(Dinner.GameState.PAUSED) || Dinner.gameState.equals(Dinner.GameState.CUTSCENE) || Dinner.gameState.equals(Dinner.GameState.LOADING)) return false; // Early exit if the key is not X
        return interactableObjectController.checkInteractions(Player.getPlayer().getBox());
    }

    public void updateContollers(InteractableObjectController interactableObjectController) {
        this.interactableObjectController = interactableObjectController;
    }
}

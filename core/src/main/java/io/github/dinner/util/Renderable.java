package io.github.dinner.util;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.github.dinner.controller.PlayerController;
import io.github.dinner.model.Player;
import io.github.dinner.model.interactables.Item;
import io.github.dinner.model.interactables.Npc;

public class Renderable {
    float depth;
    Object object;
    float stateTime;

    public Renderable(float depth, Object object, float stateTime) {
        set(depth, object, stateTime);
    }

    public void set(float depth, Object object, float stateTime) {
        this.depth = depth;
        this.object = object;
        this.stateTime = stateTime;
    }

    public void render(SpriteBatch batch) {
        if (object instanceof Player && Player.getPlayer().isVisible()) {
            Player player = (Player) object;
            batch.draw(
                PlayerController.getAnimationToRender().getKeyFrame(stateTime, true),
                player.getBox().x,
                player.getBox().y,
                1, 1.22f
            );
        } else if (object instanceof Npc) {
            Npc npc = (Npc) object;
            batch.draw(
                npc.getIdleAnimation().getKeyFrame(stateTime, true),
                npc.getRenderBox().x,
                npc.getRenderBox().y,
                npc.getRenderBox().width,
                npc.getRenderBox().height
            );
        } else if (object instanceof Item) {
            Item item = (Item) object;
            batch.draw(
                item.getTexture(),
                item.getBox().x,
                item.getBox().y,
                item.getBox().width,
                item.getBox().height
            );
        }
    }

    public float getDepth() {
        return depth;
    }
}



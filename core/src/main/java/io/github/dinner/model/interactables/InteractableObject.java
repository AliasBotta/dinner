package io.github.dinner.model.interactables;

import com.badlogic.gdx.math.Rectangle;
import io.github.dinner.controller.InteractableObjectController;
import io.github.dinner.model.Player;
import io.github.dinner.util.Action;

/*
* InteractableObject implementa il design pattern "Template Method", vi sarà una classe astratta con:
*  - metodi primitivi, che saranno ridefiniti nelle sottoclassi
*  - metodi hook, che *potranno* essere ridefiniti nelle sottoclassi
*  - metodi non sovrascrivibili, che saranno protetti dall'overriding essendo dichiarati con la keyword final
*
* in questo caso specifico non vi sono metodi hook
* */

public abstract class InteractableObject{
    protected Rectangle collidingBox;
    protected boolean showInteractionWidget;
    InteractableObjectController interactableObjectController;

    public InteractableObject(Rectangle box, boolean showInteractionWidget) {
        this.collidingBox = box;
        this.showInteractionWidget = showInteractionWidget;
        this.interactableObjectController = InteractableObjectController.getInteractableObjectController();
    }

    public Rectangle getBox() {
        return this.collidingBox;
    }

    public final boolean getShowInteractionWidget() {
        return showInteractionWidget;
    }

    public abstract void interact(Player player);
}

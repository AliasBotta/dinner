package io.github.dinner.model.interactables;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
//import com.gruppo3.game.model.dialog.Dialog;
import io.github.dinner.controller.SaveController;
import io.github.dinner.model.Player;
import io.github.dinner.model.memento.NotebookMemento;
import io.github.dinner.model.memento.NpcDialogMemento;
import io.github.dinner.util.Action;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

public class Npc extends InteractableObject {
    private Texture texture;
    private Animation<TextureRegion>[] idleAnimation;
    //private Dialog dialog;
    private boolean loopingAnimation;
    public enum Direction { EAST, NORTH, WEST, SOUTH; }
    protected Direction direction;
    private Rectangle collisionBox;
    public static Map<String, Integer> nDialogs = new HashMap<>();

    // Costruttore base senza il parametro 'direction'
    public Npc(Texture texture, Rectangle box, boolean showInteractionWidget) {
        this(texture, box, showInteractionWidget, Direction.SOUTH); // Richiama il costruttore più specifico con il valore di default
    }

    // Costruttore esteso con il parametro 'direction'
    public Npc(Texture texture, Rectangle box, boolean showInteractionWidget, Direction direction) {
        super(box, showInteractionWidget);
        this.texture = texture;
        this.direction = direction; // Imposta la direzione specificata
        this.collisionBox = new Rectangle(box.x, box.y, box.width, 1f);
        initializeAnimations(texture);
    }

    private void initializeAnimations(Texture texture) {
        int frameDimensionX = 18;
        int frameDimensionY = 23;
        int numAnimations = 4;
        int numFrames = 6;
        this.loopingAnimation = true;
        idleAnimation = new Animation[numAnimations];

        for (int i = 0; i < numAnimations; i++) {
            TextureRegion[] framesIdle = new TextureRegion[numFrames];
            for (int j = 0; j < numFrames; j++) {
                framesIdle[j] = new TextureRegion(texture, (i * numFrames + j) * frameDimensionX, 0, frameDimensionX, frameDimensionY);
            }
            idleAnimation[i] = new Animation<>(0.1f, framesIdle);
        }
    }

    @Override
    public Rectangle getBox() {
        // Ritorna il collision box per la gestione delle collisioni
        return collisionBox;
    }

    public Rectangle getRenderBox() {
        // Ritorna il rettangolo originale per il rendering
        return super.getBox();
    }

    @Override
    public void interact(Player player) {
        /*
        if (dialog != null) {
            new DialogController().startDialog(dialog);
        }*/
    }

    // Questo metodo ha lo scopo di recuperare l'animazione corrispondente alla direzione in cui l'NPC è attualmente orientato
    public Animation<TextureRegion> getIdleAnimation() {
        return direction.ordinal() < idleAnimation.length ? idleAnimation[direction.ordinal()] : idleAnimation[0];
    }

    // Questo metodo è usato per ottenere il frame corrente dell'animazione basato sul tempo trascorso, che è essenziale per garantire che l'animazione proceda in modo fluido e coerente con il passare del tempo nel gioco
    public TextureRegion getFrame(float stateTime) {
        return getIdleAnimation().getKeyFrame(stateTime, loopingAnimation);
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public Direction getDirection() {
        return direction;
    }

    public Texture getTexture() {
        return this.texture;
    }

    /*
    public void setDialog(Dialog dialog) {
        this.dialog = dialog;
    }

    public Dialog getDialog() {
        return dialog;
    }

    public void dispose() {
        npcTexture.dispose();
    }*/

    // Metodo helper per settare un valore in nDialogs
    public static void setDialogCount(String key, int count) {
        nDialogs.put(key, count);
    }

    // Metodo helper per incrementare il valore di un dialogo in nDialogs
    public static void incrementDialogCount(String key) {
        nDialogs.put(key, nDialogs.getOrDefault(key, 0) + 1);
    }

    // Metodo helper per ottenere il valore di un dialogo in nDialogs
    public static int getDialogCount(String key) {
        return nDialogs.getOrDefault(key, 0);
    }

    // Metodo per salvare lo stato corrente dei dialoghi degli NPC in un memento
    public static void saveState() {
        SaveController.getInstance().setNpcDialogState(new NpcDialogMemento(new HashMap<>(nDialogs)));
    }

    // Metodo per ripristinare lo stato dei dialoghi degli NPC da un memento
    public static void restoreState(NpcDialogMemento memento) {
        nDialogs = new HashMap<>(memento.getDialogCounts());
    }
}

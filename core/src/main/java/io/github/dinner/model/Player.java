package io.github.dinner.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import io.github.dinner.controller.LevelController;
import io.github.dinner.controller.SaveController;
import io.github.dinner.model.interactables.Item;
import io.github.dinner.model.memento.ItemMemento;
import io.github.dinner.model.memento.PlayerMemento;
import io.github.dinner.view.screens.GameScreen;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Player {
    public enum PlayerDirection {
        EAST,
        NORTH,
        WEST,
        SOUTH
    }

    private Animation<TextureRegion>[] idleAnimation;
    private Animation<TextureRegion>[] walkAnimation;
    private Rectangle playerBox = new Rectangle();
    private PlayerDirection playerDirection = PlayerDirection.NORTH;

    private List<Item> inventory;
    public static Player player;
    private boolean isVisible = true;
    private String playerName;
    private Character gender;

    private Player() {
        this.playerBox = new Rectangle(10, 6, 1, 1);  // Set initial position and size
        this.inventory = new ArrayList<>();
        this.setGender('M'); // gender di default lo mettiamo a 'M'
    }

    private void initializeAnimations() {
        int frameDimensionX = 18;
        int frameDimensionY = 23;
        int numAnimations = 4;
        int numFrames = 6;

        Texture playerIdle;
        Texture playerWalk;

        if (this.gender == 'M') {
            playerIdle = new Texture(Gdx.files.internal("characters/ProtagonistM_Idle.png"));
            playerWalk = new Texture(Gdx.files.internal("characters/ProtagonistM_Walk.png"));
        } else {
            playerIdle = new Texture(Gdx.files.internal("characters/ProtagonistF_Idle.png"));
            playerWalk = new Texture(Gdx.files.internal("characters/ProtagonistF_Walk.png"));
        }

        idleAnimation = new Animation[numAnimations];
        walkAnimation = new Animation[numAnimations];

        for (int i = 0; i < numAnimations; i++) {
            TextureRegion[] framesIdle = new TextureRegion[numFrames];
            TextureRegion[] framesWalk = new TextureRegion[numFrames];
            for (int j = 0; j < numFrames; j++) {
                framesIdle[j] = new TextureRegion(playerIdle, (i * numFrames + j) * frameDimensionX,
                    0, frameDimensionX, frameDimensionY);
                framesWalk[j] = new TextureRegion(playerWalk, (i * numFrames + j) * frameDimensionX,
                    0, frameDimensionX, frameDimensionY);
            }
            idleAnimation[i] = new Animation<>(0.1f, framesIdle);
            walkAnimation[i] = new Animation<>(0.1f, framesWalk);
        }
    }

    public Rectangle getBox() {
        return new Rectangle(playerBox.x, playerBox.y, playerBox.width, playerBox.height);
    }

    public static Player getPlayer() {
        if (player == null)
            player = new Player();

        return player;
    }

    public PlayerDirection getPlayerDirection() {
        return playerDirection;
    }

    public void setPlayerDirection(PlayerDirection direction) {
        this.playerDirection = direction;
    }

    public Animation<TextureRegion> getIdleAnimation(PlayerDirection direction) {
        return idleAnimation[direction.ordinal()];
    }

    public Animation<TextureRegion> getWalkAnimation(PlayerDirection direction) {
        return walkAnimation[direction.ordinal()];
    }

    // metodi per la gestione dell'inventario, idea -> si potrebbe fare una classe a parte nel model chiamata inventory (da vedere)
    public void addItem(Item item) {
        if (!inventory.contains(item))
            inventory.add(item);
    }

    // Metodo per iterare sugli item con un Consumer
    public void forEachItem(Consumer<Item> action) {
        inventory.forEach(action);
    }

    public void removeItem(Item item) { inventory.remove(item); }

    public boolean containsItem(Item item) { return inventory.contains(item); }

    public void setPosition(float x, float y, PlayerDirection playerDirection) {
        this.playerBox.x = x;
        this.playerBox.y = y;
        this.playerDirection = playerDirection;
    }

    public void setPosition(float x, float y) {
        this.playerBox.x = x;
        this.playerBox.y = y;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public boolean isVisible() {
        return isVisible;
    }

    public void setVisible(boolean visible) {
        isVisible = visible;
    }

    public Character getGender() {
        return gender;
    }

    public void setGender(Character gender) {
        this.gender = gender;
        initializeAnimations(); // Initialize animations based on gender
    }

    // Metodo per ripristinare lo stato del Player da un PlayerMemento
    public void restoreState(PlayerMemento memento) {
        this.setGender(memento.getGender());

        setPosition(memento.getXPosition(), memento.getYPosition(), memento.getPlayerDirection());
        this.inventory.clear();
        GameScreen.phase = memento.getPhase();
        this.playerName = memento.getPlayerName();
        memento.forEachItemMemento(itemMemento -> this.addItem(new Item(
            itemMemento.getName(),
            "",
            itemMemento.getTexturePath(),
            new Rectangle(0,0,1,1),
            false
        )));
    }

    //salva il suo stato nel saveController, tuttavia non scrive su disco
    public void saveState(){
        List<ItemMemento> capturedInventory = new ArrayList<>();  // Lista temporanea per catturare l'inventario

        player.forEachItem(item -> capturedInventory.add(new ItemMemento(item.getName(), item.getTexturePath())) );

        PlayerMemento playerState =  new PlayerMemento(
            player.getPlayerDirection(),
            capturedInventory,
            (int) player.getBox().x,
            (int) player.getBox().y,
            LevelController.getLevelController().getCurrentLevelName(),
            GameScreen.phase,
            this.gender,
            this.playerName
        );

        SaveController.getInstance().setPlayerState(playerState);
    }

    public PlayerDirection getDirection(){
        return this.playerDirection;
    }

    public static void setMockPlayerForTesting(Player mockPlayer) {
        player = mockPlayer;
    }

}

package io.github.dinner.controller;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import io.github.dinner.Dinner;
import io.github.dinner.model.Player;
import io.github.dinner.view.screens.GameScreen;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class PlayerController extends InputAdapter {
    Random random = new Random();
    private static Player player = null;
    private final float tileSize = 1f; // Dimensione di un tile in unità di gioco
    private float srcX, srcY; // Posizione di partenza dell'animazione
    private float destX, destY; // Posizione di destinazione dell'animazione
    private static boolean isTransitioning = false; // Indica se il giocatore è in movimento
    private float animTimer = 0f; // Timer per l'animazione
    private final float ANIM_TIME = 1f / 6.75f; // Durata dell'animazione per un tile

    private Player.PlayerDirection currentDirection; // Direzione del movimento attuale
    private final List<Integer> pressedKeys = new LinkedList<>(); // Lista dei tasti premuti in ordine temporale
    private final Map<Integer, Float> keyPressTimers = new HashMap<>(); // Timer per ogni tasto premuto
    private final float TAP_THRESHOLD = 0.08f; // Tempo massimo per considerare un tocco come "tap"

    private int stepCounter = 0;

    private boolean hasCollided = false;

    private int randomN;

    private int i = 0;

    public PlayerController() {
        this.player = Player.getPlayer();
        this.srcX = player.getBox().x;
        this.srcY = player.getBox().y;
        this.destX = srcX;
        this.destY = srcY;
    }

    public PlayerController(Player player) {
        this.player = player;
        this.srcX = player.getBox().x;
        this.srcY = player.getBox().y;
        this.destX = srcX;
        this.destY = srcY;
    }

    @Override
    public boolean keyDown(int keycode) {
        if(!Dinner.gameState.equals(Dinner.GameState.RUNNING)) return false;
        if (!pressedKeys.contains(keycode)) {
            pressedKeys.add(0, keycode); // Aggiungi il tasto in cima alla lista
            keyPressTimers.put(keycode, 0f); // Inizia a tracciare il tempo di pressione
        }
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        if(!Dinner.gameState.equals(Dinner.GameState.RUNNING)) return false;
        pressedKeys.remove((Integer) keycode); // Rimuovi il tasto dalla lista

        // Controlla se il tasto è stato premuto brevemente
        Float pressTime = keyPressTimers.get(keycode);
        if (pressTime != null && pressTime < TAP_THRESHOLD) {
            // Ruota il personaggio senza muoverlo
            Player.PlayerDirection direction = getDirectionForKeycode(keycode);
            if (direction != null) {
                currentDirection = direction;
                player.setPlayerDirection(direction);
            }
        }

        keyPressTimers.remove(keycode); // Rimuovi il timer per il tasto
        return true;
    }

    public void updateInput(float delta) {
        if (!Dinner.gameState.equals(Dinner.GameState.RUNNING)) return; // Ignora l'input durante la transizione

        // Aggiorna i timer per i tasti premuti
        for (Integer key : keyPressTimers.keySet()) {
            keyPressTimers.put(key, keyPressTimers.get(key) + delta);
        }

        if (isTransitioning) {

            // Aggiorna il timer per l'animazione
            animTimer += delta;
            float progress = animTimer / ANIM_TIME;

            float interpolatedX = Interpolation.linear.apply(srcX, destX, progress);
            float interpolatedY = Interpolation.linear.apply(srcY, destY, progress);

            player.setPosition(interpolatedX, interpolatedY);

            if (animTimer >= ANIM_TIME) {
                finishMove();
            }
        } else if (!pressedKeys.isEmpty()) {
            // Avvia un nuovo movimento
            startNextMovement();
        }
    }


    private void startNextMovement() {
        if (pressedKeys.isEmpty()) {
            return; // Nessun tasto premuto
        }

        Integer keycode = pressedKeys.get(0);
        Float pressTime = keyPressTimers.get(keycode);

        // Ottieni la direzione associata al tasto premuto
        Player.PlayerDirection newDirection = getDirectionForKeycode(keycode);

        if (newDirection != null) {
            if (newDirection == currentDirection || (pressTime != null && pressTime >= TAP_THRESHOLD)) {
                // Se il giocatore sta già guardando nella direzione del tasto o il tasto è stato premuto abbastanza a lungo
                currentDirection = newDirection;
                player.setPlayerDirection(newDirection);

                // Imposta la posizione di destinazione
                srcX = player.getBox().x;
                srcY = player.getBox().y;
                destX = srcX;
                destY = srcY;

                switch (currentDirection) {
                    case WEST:
                        destX -= tileSize;
                        break;
                    case EAST:
                        destX += tileSize;
                        break;
                    case NORTH:
                        destY += tileSize;
                        break;
                    case SOUTH:
                        destY -= tileSize;
                        break;
                }

                if (!isColliding(currentDirection, destX, destY)) {
                    isTransitioning = true;
                    animTimer = 0f; // Resetta il timer per l'animazione
                    hasCollided = false;
                } else if (!hasCollided && stepCounter > 0) {
                    AudioController.stopSound("bump");
                    AudioController.playSound("bump", false);
                    hasCollided = true;
                }
            }
        }
    }

    private void finishMove() {
        // Termina il movimento corrente
        player.setPosition(destX, destY);
        isTransitioning = false;
        stepCounter++;
        randomN = random.nextInt(2)+1;

        //Riproduci il suono dei passi coerentemente col tipo di pavimento
        if(GameScreen.levelController.getCurrentLevel().getFloorType().equals("wood") && stepCounter%2 != 0){
            AudioController.playSound("woodFS" + randomN, false);
        } else if(GameScreen.levelController.getCurrentLevel().getFloorType().equals("grass") && stepCounter%2 != 0) {

            AudioController.playSound("grassFS" + randomN + "_" + i, false);
            i = i == 0 ? 1 : 0;
        }

        // Avvia il prossimo movimento, se necessario
        if(!pressedKeys.isEmpty()) {
            startNextMovement();
        } else {
            stepCounter = 0;
        }
    }

    private Player.PlayerDirection getDirectionForKeycode(int keycode) {
        if (keycode == Input.Keys.LEFT || keycode == Input.Keys.A) {
            return Player.PlayerDirection.WEST;
        } else if (keycode == Input.Keys.RIGHT || keycode == Input.Keys.D) {
            return Player.PlayerDirection.EAST;
        } else if (keycode == Input.Keys.UP || keycode == Input.Keys.W) {
            return Player.PlayerDirection.NORTH;
        } else if (keycode == Input.Keys.DOWN || keycode == Input.Keys.S) {
            return Player.PlayerDirection.SOUTH;
        }
        return null; // Nessuna direzione per tasti non validi
    }

    private boolean isColliding(Player.PlayerDirection direction, float targetX, float targetY) {
        MapLayer collisionObjectLayer = GameScreen.levelController.getMap().getLayers().get("Collisioni");
        MapObjects objects = collisionObjectLayer.getObjects();

        Rectangle playerRectangle = new Rectangle(targetX, targetY, player.getBox().width, player.getBox().height);

        for (RectangleMapObject rectangleMapObject : objects.getByType(RectangleMapObject.class)) {
            if (playerRectangle.overlaps(rectangleMapObject.getRectangle())) {
                return true; // Rilevata collisione
            }
        }

        return false;
    }

    public static Animation<TextureRegion> getAnimationToRender() {
        return isTransitioning ? player.getWalkAnimation(player.getPlayerDirection()) : player.getIdleAnimation(player.getPlayerDirection());
    }

    public void releaseAllKeys() {
        // Forza il rilascio dei tasti premuti
        for (int keycode : pressedKeys) {
            keyUp(keycode); // Simula il rilascio del tasto
        }
        pressedKeys.clear(); // Resetta la lista dei tasti premuti
        keyPressTimers.clear(); // Resetta i timer dei tasti
    }
}

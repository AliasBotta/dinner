package io.github.dinner.view.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import io.github.dinner.Dinner;
import io.github.dinner.model.Player;
public class TransitionScreen implements Screen {
    private final String previousLevel; // Livello corrente
    private final String nextLevel;    // Livello successivo
    private final Dinner game;
    private final float playerX, playerY;
    private final Screen oldScreen;      // Riferimento allo screen su cui stavamo prima

    private boolean fadeOut = true; // Flag per indicare se è in fade-out
    private float fadeSpeed = 0.04f; // Velocità del fade
    private float alpha = 0; // Trasparenza per il fade
    private Player.PlayerDirection playerDirection;

    public TransitionScreen(String previousLevel,
                            String nextLevel,
                            Dinner game,
                            float playerX,
                            float playerY,
                            Player.PlayerDirection playerDirection) {
        Dinner.gameState = Dinner.GameState.LOADING;
        this.previousLevel = previousLevel;
        this.nextLevel = nextLevel;
        this.game = game;
        this.playerX = playerX;
        this.playerY = playerY;
        this.playerDirection = playerDirection;

        // Memorizza lo screen su cui stiamo attualmente (di solito un GameScreen)
        // in modo da poterlo disegnare e poi tornarci.

        this.oldScreen = game.getScreen();

        this.alpha = 0;
        this.fadeSpeed = 0.04f;
        //AudioController.stopSound("transition");
        //AudioController.playSound("transition", false);

    }

    public TransitionScreen(String previousLevel,
                            String nextLevel,
                            Dinner game,
                            float playerX,
                            float playerY,
                            Player.PlayerDirection playerDirection,
                            GameScreen gameScreen,
                            float alpha,
                            float fadeSpeed) {
        this.previousLevel = previousLevel;
        this.nextLevel = nextLevel;
        this.game = game;
        this.playerX = playerX;
        this.playerY = playerY;
        this.playerDirection = playerDirection;

        // Memorizza lo screen su cui stiamo attualmente (di solito un GameScreen)
        // in modo da poterlo disegnare e poi tornarci.

        this.oldScreen = gameScreen;

        this.alpha = alpha;
        this.fadeSpeed = fadeSpeed;
    }

    @Override
    public void render(float delta) {
        // 1) Disegna lo screen precedente (quello che stava “dietro”)
        if (oldScreen != null) {
            // Evita loop se oldScreen fosse la stessa TransitionScreen
            if (oldScreen != this) {
                oldScreen.render(delta);
            }
        }

        // 2) Disegna il rettangolo di fade sopra
        Gdx.gl.glEnable(Gdx.gl.GL_BLEND);
        Gdx.gl.glBlendFunc(Gdx.gl.GL_SRC_ALPHA, Gdx.gl.GL_ONE_MINUS_SRC_ALPHA);

        game.shapeRenderer.setColor(0, 0, 0, alpha);
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        game.shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.shapeRenderer.end();

        Gdx.gl.glDisable(Gdx.gl.GL_BLEND);

        // 3) Aggiorna lo stato della transizione
        if (fadeOut) {
            alpha += fadeSpeed; // fade-out => aumenta alpha
            if (alpha >= 1) {
                alpha = 1;
                fadeOut = false; // passa al fade-in

                // A fade-out completato: cambiamo stanza
                GameScreen.levelController.setLevel(nextLevel);

                // Posiziona il player nel nuovo livello
                if ((playerX != 0 || playerY != 0) && this.previousLevel != null) {
                    Player.getPlayer().setPosition(playerX, playerY, playerDirection);
                }
            }
        } else {
            alpha -= fadeSpeed; // fade-in => diminuisce alpha
            if (alpha <= 0) {
                alpha = 0;

                // Fine transizione, torniamo allo screen vecchio
                // (che adesso “punta” al nuovo livello)
                if(this.previousLevel != null){
                    Dinner.gameState = Dinner.GameState.RUNNING;
                }

                // Imposta la direzione del player
                if(this.previousLevel != null){
                    Player.getPlayer().setPlayerDirection(this.playerDirection);
                }

                // Se oldScreen era un GameScreen, rilasciamo i tasti
                if (oldScreen instanceof GameScreen) {
                    ((GameScreen) oldScreen).playerController.releaseAllKeys();
                }

                // Torna al vecchio screen
                game.setScreen(oldScreen);
            }
        }
    }

    @Override
    public void show() {}

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}
}


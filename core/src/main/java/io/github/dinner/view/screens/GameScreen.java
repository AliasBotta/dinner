package io.github.dinner.view.screens;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.dinner.Dinner;
import io.github.dinner.Dinner.GameState;
import io.github.dinner.controller.*;
import io.github.dinner.model.Notebook;
import io.github.dinner.model.Player;
import io.github.dinner.model.Progress;
import io.github.dinner.model.interactables.Item;
import io.github.dinner.model.interactables.Npc;
import io.github.dinner.model.memento.PlayerMemento;
import io.github.dinner.view.menus.ComputerMenu;
import io.github.dinner.model.states.MenuState;
import io.github.dinner.view.menus.PauseMenu;
import io.github.dinner.controller.SaveController.GameData;
import io.github.dinner.view.ui.DialogBox;
import io.github.dinner.view.ui.OptionBox;
import io.github.dinner.util.Renderable;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static io.github.dinner.Dinner.isNewGame;
import static io.github.dinner.model.states.LevelState.unitScale;
//import io.github.dinner.controller.PlayerController;
//import io.github.dinner.controller.PauseController;

public class GameScreen implements Screen {
    private final Dinner game;
    public static OrthographicCamera camera;
    private PauseController pauseController;
    public static  MenuController menuController;
    public static LevelController levelController;
    public PlayerController playerController;
    private Stage stage;
    private Skin skin;
    private Skin skin_hud; //SOSTITUIRE A SKIN APPENA E' TUTTO PRONTO
    private InputMultiplexer multiplexer;
    private Viewport gameViewport;
    private MenuState menuState;
    private TextArea text;
    private static InteractionController interactionController;//NEL DEV ERA MESSO PUBLIC
    private InteractableObjectController interactableObjectController;
    public static Stage stageInventory;

    //Declare Preferences to store data inside while running game.
    private GameData gameData;
    private boolean updated = false; //To manage gameData amd fileN updating
    float stateTime;
    private Table dialogRoot;
    public static Table nameTable;
    public static Label nameLabel;
    private DialogBox dialogBox;
    private OptionBox optionBox;
    public static boolean isDialogNameVisible;
    private ExtendViewport uiViewport;
    public static DialogController dialogController;
    public static final float DEFAULT_ZOOM_FACTOR = 0.7f; // Valore minore di 1 avvicina la telecamera, maggiore allontana
    public static final float GARDEN_ZOOM_FACTOR = 0.3f;
    public static float zoomFactor = DEFAULT_ZOOM_FACTOR;
    public final int numSlots = 6;

    private final List<Item> itemsToRender = new ArrayList<>();
    private final List<Npc> npcsToRender = new ArrayList<>();
    public static boolean restoredPauseMenu = false;
    public static boolean isDialogueBoxVisible = false;
    private final List<Renderable> renderables = new ArrayList<>();
    public static int phase;
    private static Thread currentZoomThread = null;
    private ComputerMenu computerMenu;
    private PauseMenu pauseMenu;


    //New Game constructor
    public GameScreen(final Dinner game, GameData data) {

        Pixmap pixmap = new Pixmap(Gdx.files.internal("cursor.png"));
        Cursor cursor = Gdx.graphics.newCursor(pixmap, 0, 0);
        Gdx.graphics.setCursor(cursor);
        pixmap.dispose();

        this.game = game;
        this.stateTime = 0f;
        this.skin = new Skin(Gdx.files.internal("custom_ui_assets/skin/SKIN.json"));
        this.skin_hud = new Skin(Gdx.files.internal("custom_hud_assets/skin/HUDSkin.json"));
        this.gameViewport = new ScreenViewport();
        nameLabel = new Label("Pino", skin_hud, "name");

        if (data != null) { //Initialize game data
            Player.getPlayer().restoreState((PlayerMemento) data.playerState);
            Progress.restoreState(data.progressState);
            Notebook.restoreState(data.notebookState);
            Npc.restoreState(data.nDialogsState);
        }else {
            GameScreen.phase = 0; // messo qui perché viene inizializzato anche nel restoreState di player
            Progress.initializeProgressMap();
            Notebook.initializeNotebook();
        }

        initUI();
        GameScreen.zoomFactor = GameScreen.DEFAULT_ZOOM_FACTOR;

        // Initialize camera
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 32, 32);

        // Initialize controllers
        //this.playerController = new PlayerController();
        dialogController = new DialogController(dialogBox, optionBox);
        this.pauseController = new PauseController(game);
        menuController = new MenuController();
        menuController.changeState(new PauseMenu(menuController));
        this.playerController = new PlayerController();
        this.interactableObjectController = InteractableObjectController.getInteractableObjectController();
        interactionController = new InteractionController(interactableObjectController);
        // Initialize stage
        //this.stage = new Stage(gameViewport);

        // Initialize input multiplexer
        levelController = LevelController.getLevelController();
        if (data != null){
            isNewGame = false;
            levelController.setLevel(data.playerState.getCurrentRoom());
            AudioController.playMusicOfLevel(levelController.getCurrentLevelName());
            Dinner.gameState = GameState.RUNNING;
            camera.viewportWidth = 32f * zoomFactor;
            camera.viewportHeight = 32f * Gdx.graphics.getHeight() / Gdx.graphics.getWidth() * zoomFactor;
            //DialogController.initializeDialogs(); !!!
        } else {
            DialogController.initializeDialogs();
            isNewGame = true;
            DialogController.resetDialogs();
            levelController.setLevel("newGameCutsceneLevel");
        }
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(0, pauseController);
        multiplexer.addProcessor(1, dialogController);
        multiplexer.addProcessor(2, interactionController);
        multiplexer.addProcessor(3, playerController);
        Gdx.input.setInputProcessor(multiplexer);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        stateTime += delta;

        if (Dinner.gameState.equals(GameState.RUNNING)) {
            if (!dialogBox.isVisible()) {
                playerController.updateInput(delta);
            }
            else
            {
                playerController.releaseAllKeys();
            }

            if (this.updated) {
                this.updated = false;
            }

            multiplexer.removeProcessor(menuController.getStage());
        }

        // Aggiorna la telecamera e il renderer
        updateCameraAndRenderer();

        // Pulisce lo schermo
        ScreenUtils.clear(0, 0, 0, 1);

        // Renderizza il gioco e l'interfaccia utente
        renderGame();
        renderUI();

        if (Dinner.gameState.equals(GameState.PAUSED)) {
            if (!multiplexer.getProcessors().contains(menuController.getStage(), true)) {
                //Gdx.app.debug("GameScreen", "restoredPauseMenu");
                menuController.getStage().dispose();
                this.pauseMenu = new PauseMenu(menuController);
                menuController.changeState(this.pauseMenu);
                multiplexer.addProcessor(4, menuController.getStage());
                this.pauseMenu.appearanceAnimation();
            }

            menuController.getStage().act(delta);
            menuController.getStage().draw();
        }

        if (Dinner.gameState.equals(GameState.COMPUTER)) {
            if (!multiplexer.getProcessors().contains(menuController.getStage(), true)) {
                //Gdx.app.debug("GameScreen", "restoredPauseMenu");
                menuController.getStage().dispose();
                this.computerMenu = new ComputerMenu(menuController);
                menuController.changeState(this.computerMenu);
                multiplexer.addProcessor(4, menuController.getStage());
                computerMenu.appearanceAnimation();
            }

            menuController.getStage().act(delta);
            menuController.getStage().draw();
        }
    }
    public static void updateCameraAndRenderer() {
        if(!Dinner.gameState.equals(GameState.CUTSCENE)) {
            // Ottieni il centro del PlayerBox
            float playerCenterX = Player.getPlayer().getBox().x + Player.getPlayer().getBox().width / 2;
            float playerCenterY = Player.getPlayer().getBox().y + Player.getPlayer().getBox().height / 2;

            // Centra la telecamera sul centro del PlayerBox
            camera.position.set(playerCenterX, playerCenterY, camera.position.z);

            // Arrotonda la posizione della telecamera in base all'unitScale
            float roundedCameraX = Math.round(camera.position.x / unitScale) * unitScale;
            float roundedCameraY = Math.round(camera.position.y / unitScale) * unitScale;

            // Aggiorna la posizione arrotondata
            camera.position.set(roundedCameraX, roundedCameraY, camera.position.z);
            camera.update();
        }
        // Configura il renderer con la nuova vista della telecamera
        OrthogonalTiledMapRenderer mapRenderer = levelController.getRenderer();
        mapRenderer.setView(camera);
    }

    private void renderGame() {
        // Imposta i layer di background da renderizzare
        int[] backgroundLayers = {0, 1};
        OrthogonalTiledMapRenderer mapRenderer = levelController.getRenderer();
        mapRenderer.render(backgroundLayers);

        // Svuota la lista di renderables
        renderables.clear();

        // Aggiungi il giocatore alla lista
        addRenderable(Player.getPlayer().getBox().y, Player.getPlayer(), stateTime);

        // Aggiungi gli NPC alla lista
        npcsToRender.clear();
        npcsToRender.addAll(InteractableObjectController.getInteractableObjectController().getNpcList());
        for (Npc npc : npcsToRender) {
            addRenderable(npc.getBox().y, npc, stateTime);
        }

        // Aggiungi gli oggetti alla lista
        itemsToRender.clear();
        itemsToRender.addAll(InteractableObjectController.getInteractableObjectController().getItemList());
        for (Item item : itemsToRender) {
            addRenderable(item.getBox().y, item);
        }

        // Ordina gli oggetti in base alla loro posizione Y (profondità)
        renderables.sort((a, b) -> Float.compare(b.getDepth(), a.getDepth()));

        // Inizia il rendering
        game.batch.setProjectionMatrix(camera.combined);
        game.batch.begin();

        for (Renderable renderable : renderables) {
            renderable.render(game.batch);
        }

        // Renderizza il widget dell'interazione
        if (!Dinner.gameState.equals(GameState.CUTSCENE)) {
            interactionController.displayInteractionWidget(game.batch);
        }
        game.batch.end();

        // Imposta i layer di foreground da renderizzare
        int[] foregroundLayers = {3};
        mapRenderer.render(foregroundLayers);
    }

    private void addRenderable(float depth, Object object) {
        addRenderable(depth, object, 0);
    }

    private void addRenderable(float depth, Object object, float stateTime) {
        // Crea un nuovo oggetto solo se necessario
        renderables.add(new Renderable(depth, object, stateTime));
    }

    private void initUI() {
        uiViewport = new ExtendViewport(400, 300);
        // INVENTORY UI
        stageInventory = new Stage(new ScreenViewport());
        GameScreen.updateInventoryUI();

        // DIALOG UI
        stage = new Stage(uiViewport);

        stage.getViewport().update(Gdx.graphics.getWidth() / 5, Gdx.graphics.getHeight() / 5, true);

        dialogRoot = new Table();
        dialogRoot.setFillParent(true);
        dialogRoot.padBottom(20);
        stage.addActor(dialogRoot);

        this.dialogBox = new DialogBox(this.skin_hud);
        this.dialogBox.setVisible(false);

        nameTable = new Table(skin_hud);
        GameScreen.nameTable.setVisible(false);
        nameTable.setBackground(skin_hud.getDrawable("DialogBox - Name"));
        nameLabel.setAlignment(Align.center);
        nameTable.add(nameLabel).expandX().center().padLeft(15f).padRight(15f);
        Image buttonToProgress = new Image(new Texture(Gdx.files.internal("custom_hud_assets/interaction.png")));
        buttonToProgress.setHeight(15f);
        buttonToProgress.setWidth(15f);
        dialogBox.add(buttonToProgress)
            .width(15f)
            .height(15f)
            .bottom()
            .pad(2f);

        optionBox = new OptionBox(this.skin_hud);
        optionBox.setVisible(false);

        Table dialogTable = new Table();
        dialogTable.center();

        dialogTable.add(optionBox)
            .expand()
            .align(Align.right)
            .row();
        dialogTable.add(nameTable)
            .expand()
            .minWidth(120f)
            .height(20f)
            .align(Align.left)
            .row();
        dialogTable.add(dialogBox)
            .expand()
            .align(Align.bottom)
            .spaceBottom(8f)
            .spaceLeft(8f)
            .spaceRight(8f)
            .center()
            .row();

        dialogRoot.add(dialogTable).expand().align(Align.bottom);
    }

    private void renderUI() {
        dialogController.update(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));
        stage.draw();


        if(Dinner.gameState.equals(GameState.RUNNING) || Dinner.gameState.equals(GameState.LOADING) || Dinner.gameState.equals(GameState.PAUSED)) {
            stageInventory.act();
            stageInventory.draw();
        }
    }

    public static void updateInventoryUI() {

        stageInventory.clear(); // pulisco

        Skin skin = new Skin(Gdx.files.internal("custom_inventory_assets/skin/InventorySkin.json"));

        // Setting della tabella
        Table container = new Table();
        container.setFillParent(true);
        container.top().left();
        container.padTop(8);
        container.padLeft(8);

        Table inventoryTable = new Table(skin);
        inventoryTable.top().left();
        inventoryTable.setBackground("Inventory");

        // aggiungo gli items
        Player.getPlayer().forEachItem(item -> {
            Table itemTable = new Table();
            Image itemImage = new Image(item.getTexture());

            // Aggiungi l'immagine e adatta il contenitore alle dimensioni scalate
            itemTable.add(itemImage).size(itemImage.getWidth() * 0.2f, itemImage.getHeight() * 0.2f);
            itemTable.padBottom(30).padLeft(5).padTop(1);
            itemTable.row();
            //itemTable.add(new Label(item.getName(), skin)); // Usa il tuo Skin

            // Aggiungi il Table dell'item all'inventario e adatta la cella al contenuto
            inventoryTable.add(itemTable); //aggiustamento dello spazio
            inventoryTable.row();
        });
        container.add(inventoryTable);
        stageInventory.addActor(container);
    }



    @Override
    public void resize(int width, int height) {
        // Configura una nuova dimensione del viewport per avvicinare la telecamera


        camera.viewportWidth = 32f * GameScreen.zoomFactor; // La dimensione "logica" del mondo visibile
        camera.viewportHeight = 32f * height / width * GameScreen.zoomFactor; // Scala mantenendo il rapporto d'aspetto
        camera.update();

        stageInventory.getViewport().update(width, height, true);

        gameViewport.update(width, height, true);
        menuController.getStage().getViewport().update(width, height, true);
    }


    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
        if(levelController != null) { // vedere se togliere il controllo del null
            levelController.dispose();
        }
    }

    public static void updateInteractionController() {
        interactionController.updateContollers(InteractableObjectController.getInteractableObjectController());
    }

    @Override
    public void dispose() {
        // Rilascia il Stage principale
        if (stage != null) {
            stage.clear();
            stage.dispose();
            stage = null;
        }

        // Rilascia il Stage dell'inventario
        if (stageInventory != null) {
            stageInventory.clear();
            stageInventory.dispose();
            stageInventory = null;
        }

        // Rilascia la Skin
        if (skin != null) {
            skin.dispose();
            skin = null;
        }

        // Rilascia il LevelController e il renderer del livello
        if (levelController != null) {
            levelController.getCurrentLevel().dispose();
            levelController.dispose();
            levelController = null;
        }

        // Rilascia il MenuController e il suo Stage
        if (menuController != null) {
            Stage menuStage = menuController.getStage();
            if (menuStage != null) {
                menuStage.clear();
                menuStage.dispose();
            }
            menuController = null;
        }

        // Rimuovi il Multiplexer dal sistema di input
        Gdx.input.setInputProcessor(null);
        multiplexer = null;

        // Termina lo zoomExecutor
        if (!zoomExecutor.isShutdown()) {
            zoomExecutor.shutdownNow();
        }

        if(!LevelController.loadedLevels.isEmpty()) {
            LevelController.loadedLevels.clear();
        }

//        if(Player.player != null) {
//            Player.player = null;
//        } !!! non si può fare questa cosa col singleton, al massimo fai in modo di

        // Log per il debug
        Gdx.app.log("GameScreen", "Risorse rilasciate correttamente.");
    }


    private static final ExecutorService zoomExecutor = Executors.newSingleThreadExecutor();

    public void restorePauseMenu(){
        menuController.getStage().dispose();
        menuController.changeState(new PauseMenu(menuController));
    }

    public static synchronized void smoothZoom(float targetZoom, float duration, float targetX, float targetY) {
        if (targetZoom <= 0 || duration <= 0) return;

        // Interrompe il thread attuale, se presente
        if (currentZoomThread != null && currentZoomThread.isAlive()) {
            currentZoomThread.interrupt();
        }

        final float initialZoom = zoomFactor;
        final float zoomChange = targetZoom - initialZoom;
        final float initialX = camera.position.x;
        final float initialY = camera.position.y;
        final float xChange = targetX - initialX;
        final float yChange = targetY - initialY;

        currentZoomThread = new Thread(() -> {
            float elapsedTime = 0;

            while (elapsedTime < duration) {
                try {
                    Thread.sleep(16); // Aspetta circa 16 ms (~60 FPS)
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt(); // Ripristina lo stato di interruzione
                    break;
                }

                float deltaTime = Gdx.graphics.getDeltaTime();
                elapsedTime += deltaTime;

                float progress = Math.min(elapsedTime / duration, 1.0f);
                GameScreen.zoomFactor = initialZoom + zoomChange * progress;

                // Calcola le nuove coordinate interpolate
                float newX = initialX + xChange * progress;
                float newY = initialY + yChange * progress;

                Gdx.app.postRunnable(() -> {
                    // Aggiorna la camera per lo zoom e il posizionamento
                    camera.viewportWidth = 32f * zoomFactor;
                    camera.viewportHeight = 32f * Gdx.graphics.getHeight() / Gdx.graphics.getWidth() * zoomFactor;
                    camera.position.set(newX, newY, 0);
                    camera.update();
                });

                if (progress >= 1.0f) break;
            }
        });

        currentZoomThread.setDaemon(true); // Imposta il thread come daemon
        currentZoomThread.start();
    }

}

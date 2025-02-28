package io.github.dinner.view.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.github.dinner.Dinner;
import io.github.dinner.controller.AudioController;
import io.github.dinner.controller.MenuController;
import io.github.dinner.model.Notebook;
import io.github.dinner.model.states.MenuState;

import java.util.Iterator;
import java.util.Map;

public class NotebookMenu extends MenuState {

    private Viewport viewport;
    private MenuState backState;
    private Stage stage;
    private Skin skin;
    Dinner game = (Dinner) Gdx.app.getApplicationListener();
    Table mainTable;
    float buttonWidthPercentage;
    float buttonHeightPercentage;
    float buttonPadPercentage;
    float scale;
    boolean isMouseInside;
    TextButton backButton;
    Label terminalLabel;
    Window notebookWindow;
    Array<String> dumpArray;
    List<String> notes;
    ScrollPane scrollPane;
    private static final int CHARS_LIMIT_NOTES = 75;

    Iterator<Map.Entry<String, Boolean>> iterator = Notebook.notes.entrySet().iterator();


    public NotebookMenu(MenuController loader, MenuState backState) {
        super(loader);

        isMouseInside = false;

        AudioController.soundMap.get("pc_noise_1").setOnCompletionListener(music -> {
            AudioController.stopAllSounds();
            AudioController.playSound("pc_noise_2", true);
        });

        AudioController.playSound("pc_text_printing", false);
        AudioController.playSound("pc_noise_1", false);


        this.backState = backState;
        this.viewport = new ScreenViewport();
        this.stage = new Stage(viewport);
        this.skin = new Skin(Gdx.files.internal("computer_ui_assets/skin/uiskin.json"));
        mainTable = new Table();
        mainTable.setBackground(skin.getDrawable("cursor-black"));
        mainTable.setFillParent(true);
        this.buttonWidthPercentage = 0.15f; // 15% della larghezza della finestra
        this.buttonHeightPercentage = 0.05f; // 5% dell'altezza della finestra
        this.buttonPadPercentage = 0.05f; // 20% dell'altezza
        this.scale = 35f;
        this.notebookWindow = new Window("APPUNTI", skin);
        this.dumpArray = new Array<>();
        this.notes = new List(skin);
        this.scrollPane = new ScrollPane(notes, skin);
        scrollPane.setFadeScrollBars(false); //mostra sempre le barre di scorrimento

        this.backButton = new TextButton("INDIETRO", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                AudioController.playSound("click_down", false);
                return super.touchDown(event, x, y, pointer, button);
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                super.touchUp(event, x, y, pointer, button);
                AudioController.playSound("click_up", false);
                loader.removeInputProcessor(stage);
                loader.getStage().dispose();
                loader.changeState(backState);
                loader.addInputProcessor(loader.getStage());
            }
        });

        this.terminalLabel = new Label("\n  >  Software in uso: MainSystem/Blocco_NoteVer1.2.1.exe/BLOCCO NOTE\n  >  Inserimento in testa per i nuovi indizi\n", skin, "default");

        drawTable(mainTable, Gdx.graphics.getWidth() * buttonWidthPercentage, Gdx.graphics.getHeight() * buttonHeightPercentage, Gdx.graphics.getHeight() * buttonPadPercentage, (Gdx.graphics.getHeight() * buttonHeightPercentage) / scale);


    }

    @Override
    public Stage getStage(){
        return this.stage;
    }

    @Override
    public Table getTable(){
        return this.mainTable;
    }

    @Override
    public void drawTable(Table mainTable, float buttonWidth, float buttonHeight, float pad, float scale){
        mainTable.top().left();

        mainTable.row().spaceBottom(2);
        mainTable.add(terminalLabel).colspan(4).align(Align.left);
        terminalLabel.setFontScale(scale);
        mainTable.row().spaceBottom(4);

        mainTable.add(backButton).width(buttonWidth).height(buttonHeight * 0.5f).align(Align.left).padLeft(20);
        backButton.getLabel().setFontScale(scale);
        mainTable.row().spaceBottom(4);

        // Scorri la mappa usando l'iterator aggiungendo alla lista tutte le label
        while (iterator.hasNext()) {
            Map.Entry<String, Boolean> entry = iterator.next();
            String label = entry.getKey() + (entry.getValue() ? " [POTREBBE AIUTARMI A TROVARE IL COLPEVOLE]" : " [NON MI AIUTERA' A TROVARE IL COLPEVOLE]");
            dumpArray.add(label);
        }

        //inverto l'array (per qualche motivo reverse non funziona)
        Array<String> reversedArray = new Array<>();


        for (int i = dumpArray.size - 1; i >= 0; i--) {
            reversedArray.add(dumpArray.get(i));
        }

        notes.clearItems();
        notes.setItems(reversedArray);
        notes.getStyle().font.getData().setScale(scale);
        notes.getStyle().selection.setTopHeight(5f);
        notes.getStyle().selection.setBottomHeight(5f);

        scrollPane.setFadeScrollBars(false);
        scrollPane.setScrollingDisabled(true, false);

        notebookWindow.clear();
        notebookWindow.add(scrollPane).expand().fill();
        mainTable.add(notebookWindow).width(Gdx.graphics.getWidth() * 0.8f).height(Gdx.graphics.getHeight() * 0.5f).align(Align.left).padLeft(20).padTop(20).padBottom(20);


        Label selectedItemLabel;
        if(!notes.getSelection().isEmpty())
            selectedItemLabel = new Label(" >  ./Indizio0.txt\n\n" + NotebookMenu.generateHint(notes.getSelected()), skin); // Inizializza la Label vuota
        else
            selectedItemLabel = new Label("", skin);
        selectedItemLabel.setFontScale(scale);

        notes.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
            if (notes.getSelected() != null) {
                selectedItemLabel.setText(" >  ./Indizio" + notes.getSelectedIndex() + ".txt\n\n" +NotebookMenu.generateHint(notes.getSelected()));
            } else {
                selectedItemLabel.setText("");
            }
            }
        });

        mainTable.row().spaceBottom(4);
        mainTable.add(selectedItemLabel).colspan(4).align(Align.left).padLeft(20).padBottom(10); // Aggiungi la Label sotto la lista



        stage.addActor(mainTable);
    }

    @Override
    public float getButtonWidthPercentage() {
        return this.buttonWidthPercentage;
    }

    @Override
    public float getButtonHeightPercentage() {
        return this.buttonHeightPercentage;
    }

    @Override
    public float getButtonPadPercentage() {
        return this.buttonPadPercentage;
    }

    @Override
    public float getScale() {
        return this.scale;
    }

    public static String generateHint(String text)
    {
        String[] words = text.split(" ");
        StringBuilder formattedDialog = new StringBuilder();
        int charsCounter = 0;
        formattedDialog.append("    ");

        for(int i = 0; i < words.length; i++)
        {
            if(words[i].startsWith("[")){
                formattedDialog.append("\n\n").append("    ");
                charsCounter = 0;
            }

            charsCounter += words[i].length()+1;
            if(charsCounter <= CHARS_LIMIT_NOTES)
            {
                formattedDialog.append(words[i]).append(" ");
            }
            else
            {
                formattedDialog.append("\n").append("    ").append(words[i]).append(" ");
                charsCounter = words[i].length()+1;
            }
        }
        return formattedDialog.toString();
    }


    public void appearanceAnimation() {
        // Creare una schermata nera dinamicamente
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(0, 0, 0, 1); // Nero pieno
        pixmap.fill();
        Texture blackTexture = new Texture(pixmap);
        pixmap.dispose(); // Rilascia il Pixmap per evitare sprechi di memoria

        Image blackOverlay = new Image(blackTexture);
        blackOverlay.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // Copre tutto lo schermo
        blackOverlay.setPosition(0, 0); // Posizionato inizialmente a copertura totale
        stage.addActor(blackOverlay); // Aggiungi la schermata nera allo stage

        // Iniziare l'animazione
        Timer.schedule(new Timer.Task() {
            float elapsed = 0f;
            float duration = 1f; // Durata totale dell'animazione
            float targetHeight = Gdx.graphics.getHeight();

            @Override
            public void run() {
                elapsed += Gdx.graphics.getDeltaTime();
                if (elapsed >= duration) {
                    // Completa l'animazione: rimuovi la schermata nera
                    blackOverlay.remove(); // Rimuovi l'actor dallo stage
                    blackTexture.dispose(); // Rilascia la texture dalla memoria
                    cancel();
                } else {
                    // Calcola l'altezza rimanente
                    float progress = elapsed / duration;
                    float newHeight = targetHeight * (1 - progress);

                    // Aggiorna la dimensione e la posizione per la schermata nera
                    blackOverlay.setSize(blackOverlay.getWidth(), newHeight);
                    blackOverlay.setPosition(0, (Gdx.graphics.getHeight() - newHeight) / 2f); // Centrare
                }
            }
        }, 0, 1 / 60f); // Aggiorna a 60 FPS
    }

    @Override
    public void dispose(){

        // Libera lo Stage
        if (this.stage != null) {
            this.stage.clear(); // Rimuove tutti gli attori
            this.stage.dispose(); // Libera le risorse dello Stage
            this.stage = null;
        }

        // Libera il Viewport (nessun dispose necessario, solo nullify)
        this.viewport = null;

        // Chiamata al dispose della superclasse
        super.dispose();

        // Log per debug
        Gdx.app.log("PauseMenu", "Risorse liberate correttamente.");
    }
}

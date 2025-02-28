package io.github.dinner.controller;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import io.github.dinner.model.dialog.*;
import io.github.dinner.view.screens.GameScreen;
import io.github.dinner.view.ui.DialogBox;
import io.github.dinner.view.ui.OptionBox;
import io.github.dinner.util.Action;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DialogControllerTest {

    @Mock
    private DialogBox dialogBox;

    @Mock
    private OptionBox optionBox;

    @Mock
    private Dialog dialog;

    @Mock
    private LinearDialogNode linearDialogNode;

    @Mock
    private ChoiceDialogNode choiceDialogNode;

    @Mock
    private Action action;

    @Mock
    private DialogTraverser dialogTraverser;

    private DialogController dialogController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        dialogController = new DialogController(dialogBox, optionBox);

        // Stub per GameScreen
        GameScreen.nameLabel = mock(Label.class);
        GameScreen.nameTable = mock(Table.class);
        GameScreen.isDialogueBoxVisible = false;
    }

    @Test
    void testKeyUp_WhenDialogBoxVisible() {
        when(dialogBox.isVisible()).thenReturn(true);
        assertTrue(dialogController.keyUp(Keys.ANY_KEY));
    }

    @Test
    void testKeyUp_WhenDialogBoxNotVisible() {
        when(dialogBox.isVisible()).thenReturn(false);
        assertFalse(dialogController.keyUp(Keys.ANY_KEY));
    }

    @Test
    void testKeyDown_WhenOptionBoxVisible_MoveUp() {
        when(optionBox.isVisible()).thenReturn(true);
        assertTrue(dialogController.keyDown(Keys.UP));
        verify(optionBox).moveUp();
    }

    @Test
    void testKeyDown_WhenOptionBoxVisible_MoveDown() {
        when(optionBox.isVisible()).thenReturn(true);
        assertTrue(dialogController.keyDown(Keys.DOWN));
        verify(optionBox).moveDown();
    }

    @Test
    void testKeyDown_SkipAnimation() {
        when(dialogBox.isVisible()).thenReturn(true);
        when(dialogBox.isFinished()).thenReturn(false);
        assertTrue(dialogController.keyDown(Keys.E));
        verify(dialogBox).skipAnimation();
    }

    @Test
    void testKeyDown_ProgressLinearDialogNode() {
        when(dialog.getNode(0)).thenReturn(linearDialogNode);
        when(dialogTraverser.getNode()).thenReturn(linearDialogNode);
        when(linearDialogNode.getAction()).thenReturn(action);

        dialogController = new DialogController(dialogBox, optionBox);
        dialogController.startDialog(dialog);

        dialogController.keyDown(Keys.E);
        verify(action).action();
    }

    @Test
    void testStartDialog_WithLinearNode() {
        when(dialog.getNode(0)).thenReturn(linearDialogNode);
        when(linearDialogNode.getText()).thenReturn("Hello World!");
        when(linearDialogNode.getSpeakerName()).thenReturn("Speaker");

        dialogController.startDialog(dialog);

        verify(dialogBox).setVisible(true);
        verify(dialogBox).animateText("Hello World!");
        verify(GameScreen.nameLabel).setText("Speaker");
    }


    @Test
    void testStopDialog() {
        dialogController.stopDialog();

        verify(dialogBox).setVisible(false);
        verify(optionBox).setVisible(false);
        verify(GameScreen.nameTable).setVisible(false);
        assertNull(dialogController.getTraverser());
    }

    @Test
    void testIsDialogShowing() {
        when(dialogBox.isVisible()).thenReturn(true);
        assertTrue(dialogController.isDialogShowing());

        when(dialogBox.isVisible()).thenReturn(false);
        assertFalse(dialogController.isDialogShowing());
    }
}

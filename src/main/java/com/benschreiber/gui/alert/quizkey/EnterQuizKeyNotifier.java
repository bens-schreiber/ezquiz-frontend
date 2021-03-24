package com.benschreiber.gui.alert.quizkey;

import com.benschreiber.gui.FXHelper;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Popup stage to alert user for an input.
 */
public class EnterQuizKeyNotifier {

    boolean keyValid;

    public EnterQuizKeyNotifier display() throws IOException {

        Stage stage = FXHelper.getPopupStage(FXHelper.Window.ENTER_KEY, true);

        EnterQuizKeyScreen.stage = stage;
        stage.showAndWait();
        this.keyValid = EnterQuizKeyScreen.response;

        return this;
    }

    public boolean isKeyValid() {
        return keyValid;
    }

    public int getKey() {
        return EnterQuizKeyScreen.key;
    }
}

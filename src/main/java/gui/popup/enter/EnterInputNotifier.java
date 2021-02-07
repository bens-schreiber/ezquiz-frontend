package gui.popup.enter;

import gui.etc.FXHelper;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Popup stage to alert user for an input.
 */
public class EnterInputNotifier {

    boolean keyValid;

    public EnterInputNotifier display() throws IOException {

        Stage stage = FXHelper.getPopupStage(FXHelper.Window.ENTER_KEY, true);

        EnterInputScreen.stage = stage;
        stage.showAndWait();
        this.keyValid = EnterInputScreen.response;

        return this;
    }

    public boolean isKeyValid() {
        return keyValid;
    }

    public int getKey() {
        return EnterInputScreen.key;
    }
}

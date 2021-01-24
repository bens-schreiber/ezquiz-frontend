package gui.popup.enter;

import gui.etc.FXHelper;
import javafx.stage.Stage;

import java.io.IOException;

public class EnterNotifier {

    boolean codeEntered;

    private final FXHelper.Window window;

    public EnterNotifier(FXHelper.Window window) {
        this.window = window;
    }

    public EnterNotifier display() throws IOException {

        Stage stage = FXHelper.getPopupStage(window, true);

        if (window == FXHelper.Window.ENTERKEY) {

            EnterKey.stage = stage;
            stage.showAndWait();
            this.codeEntered = EnterKey.response;

        } else {

            EnterCode.stage = stage;
            stage.showAndWait();
        }

        return this;
    }

    public boolean isCodeEntered() {
        return codeEntered;
    }
}

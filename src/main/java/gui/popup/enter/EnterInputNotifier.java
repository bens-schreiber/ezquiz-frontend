package gui.popup.enter;

import gui.etc.FXHelper;
import javafx.stage.Stage;

import java.io.IOException;

public class EnterInputNotifier {

    boolean codeValid;


    public EnterInputNotifier display() throws IOException {

        Stage stage = FXHelper.getPopupStage(FXHelper.Window.ENTERKEY, true);

        EnterInputScreen.stage = stage;
        stage.showAndWait();
        this.codeValid = EnterInputScreen.response;

        return this;
    }

    public boolean isCodeValid() {
        return codeValid;
    }
}

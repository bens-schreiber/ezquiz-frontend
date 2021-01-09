package gui.popup.confirm;

import gui.etc.FXHelper;
import gui.etc.Window;
import javafx.stage.Stage;

import java.io.IOException;

public class ConfirmNotifier {

    private boolean response;

    public ConfirmNotifier(String prompt) {

        //Pass error to ConfirmScreenController so it knows what to display
        ConfirmScreenController.setPrompt(prompt);

    }

    public ConfirmNotifier display() {
        try {

            Stage stage = FXHelper.getPopupStage(Window.CONFIRM, true);

            //Pass stage to ErrorScreenController so it can close it.
            ConfirmScreenController.setStage(stage);
            stage.showAndWait();

            this.response = ConfirmScreenController.getResponse();


        } catch (IOException e) {
            e.printStackTrace();
        }

        return this;
    }

    public boolean getResponse() {
        return this.response;
    }
}

package gui.popup.confirm;

import gui.etc.FXHelper;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Confirm / Deny popup stage
 */
public class ConfirmNotifier {

    private boolean response;

    public ConfirmNotifier() {
    }

    public ConfirmNotifier(String prompt) {

        //Pass error to ConfirmScreenController so it knows what to display
        ConfirmScreenController.setPrompt(prompt);
    }

    public ConfirmNotifier display() {
        try {

            Stage stage = FXHelper.getPopupStage(FXHelper.Window.CONFIRM, true);

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

    public ConfirmNotifier setPrompt(String prompt) {
        ConfirmScreenController.setPrompt(prompt);
        return this;
    }
}

package gui.popup.error;

import gui.etc.FXHelper;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Displays errors to user.
 */
public class ErrorNotifier {

    private final boolean fatal;
    public ErrorNotifier(String error, boolean fatal) {

        //Pass error to ErrorScreenController so it knows what to display
        ErrorScreenController.setError(error);

        this.fatal = fatal;
    }

    public void display(Stage owner) {
        try {


            Stage stage = FXHelper.getPopupStage(FXHelper.Window.ERROR, true);
            stage.initOwner(owner);

            //Pass stage to ErrorScreenController so it can close it.
            ErrorScreenController.setStage(stage);

            stage.showAndWait();


            if (this.fatal) {
                Platform.exit();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

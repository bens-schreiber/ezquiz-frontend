package gui.popup.notification;

import gui.PrimaryStageHolder;
import gui.etc.FXHelper;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Displays information to user with only OK button. Mainly for Errors.
 */
public class UserNotifier {

    public UserNotifier(String error) {

        //Pass error to ErrorScreenController so it knows what to display
        NotificationScreenController.setError(error);
    }

    public void display() {

        try {

            //Create the popup stage
            Stage stage = FXHelper.getPopupStage(FXHelper.Window.ERROR, true);
            stage.initOwner(PrimaryStageHolder.getPrimaryStage());

            //Pass stage to ErrorScreenController so it can close it.
            NotificationScreenController.setStage(stage);

            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void display(Stage owner) {

        try {

            //Create the popup stage
            Stage stage = FXHelper.getPopupStage(FXHelper.Window.ERROR, true);
            stage.initOwner(owner);

            //Pass stage to ErrorScreenController so it can close it.
            NotificationScreenController.setStage(stage);

            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

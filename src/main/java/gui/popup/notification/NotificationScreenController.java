package gui.popup.notification;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class NotificationScreenController implements Initializable {

    private static String error;

    private static Stage stage;

    public static void setStage(Stage stage) {
        NotificationScreenController.stage = stage;
    }

    public static void setError(String error) {
        NotificationScreenController.error = error;
    }

    @FXML
    Label errorLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        errorLabel.setText(error);
    }

    public void buttonClicked() {
        stage.close();
    }
}

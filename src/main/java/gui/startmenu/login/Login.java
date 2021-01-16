package gui.startmenu.login;

import etc.Constants;
import etc.SHA;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import requests.Database;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class Login implements Initializable {

    @FXML
    TextField passwordField, usernameField;

    @FXML
    Label errorLabel;

    //Since it is a popup window, save the stage here
    private static Stage stage;

    public static void setStage(Stage stage) {
        Login.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //No special characters, size of 8 only
        for (TextField node : Arrays.asList(usernameField, passwordField))
            node.setTextFormatter(new TextFormatter<>(change -> {
                if (change.getText().matches("[ $&+,:;=\\\\?@#|/'<>.^*()%!-]")) {
                    change.setText("");
                } else if (node.getText().length() > Constants.MAX_PASS_USER_SIZE) {
                    change.setText("");
                }

                return change;
            }));
    }

    public void loginButtonClicked() {

        if (usernameField.getText().length() < 3) {

            errorLabel.setText("Username must be at least 3 characters.");

        } else {

            try {
                //If the request is accepted
                boolean correct = Database.verifyLoginCredentials(usernameField.getText(), SHA.encrypt(passwordField.getText()))
                        == Constants.STATUS_ACCEPTED;

                errorLabel.setText(correct ? "Successfully logged in" : "Could not log in. Try again.");

            } catch (Exception e) {
                errorLabel.setText("Error connecting to database.");
            }
        }
    }

    public void exitButtonClicked() {
        stage.close();
    }



}

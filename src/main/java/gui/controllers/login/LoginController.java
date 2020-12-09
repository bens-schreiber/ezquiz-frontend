package gui.controllers.login;

import database.Requests;
import etc.Constants;
import gui.StageHelper;
import gui.popups.ErrorBox;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML
    TextField passwordField, usernameField;

    @FXML
    Label errorLabel;

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

    public void onLoginButton() {

        try {

            boolean correct = Requests.verifyLoginCredentials(usernameField.getText(), passwordField.getText())
                    == Constants.STATUS_ACCEPTED;

            errorLabel.setText(correct ? "Successfully logged in" : "Could not log in");

        } catch (InterruptedException | IOException | JSONException e) {
            e.printStackTrace();
        }

    }

    public void onExitButton() {
        StageHelper.closeStage("Login");
    }

    public void onRegisterButton() {

        try {
            Stage stage = StageHelper.createAndAddStage("Register", "/register.fxml");
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setAlwaysOnTop(true);
            stage.show();
            StageHelper.closeStage("Login");

        } catch (IOException | NullPointerException e) {
            ErrorBox.display("A page failed to load.", false);
            e.printStackTrace();
        }

    }
}

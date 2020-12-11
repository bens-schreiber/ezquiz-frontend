package gui.controllers.login;

import com.google.common.hash.Hashing;
import database.Requests;
import etc.Constants;
import gui.StageHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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

            String sha256hex = Hashing.sha256()
                    .hashString(passwordField.getText(), StandardCharsets.UTF_8)
                    .toString();

            boolean correct = Requests.verifyLoginCredentials(usernameField.getText(), sha256hex)
                    == Constants.STATUS_ACCEPTED;

            if (correct) {
                Constants.USERNAME = usernameField.getText();
                errorLabel.setText("Successfully logged in");
            } else {
                errorLabel.setText("Could not log in. Try again.");
            }


        } catch (InterruptedException | IOException | JSONException e) {
            e.printStackTrace();
        }

    }

    public void onExitButton() {
        StageHelper.closeStage("Login");
    }

}

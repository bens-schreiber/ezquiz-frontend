package gui.controllers.login;

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
import java.util.Arrays;
import java.util.ResourceBundle;


public class RegisterController implements Initializable {

    @FXML
    TextField userField, passField;

    @FXML
    Label errorLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //No special characters, size of 8 only
        for (TextField node : Arrays.asList(userField, passField))
            node.setTextFormatter(new TextFormatter<>(change -> {
                if (change.getText().matches("[ $&+,:;=\\\\?@#|/'<>.^*()%!-]")) {
                    change.setText("");
                } else if (node.getText().length() > Constants.MAX_PASS_USER_SIZE) {
                    change.setText("");
                }

                return change;
            }));
    }

    public void onExitButton() {

        StageHelper.closeStage("Register");

    }

    public void onRegisterButton() {
        try {

            boolean response = Requests.registerUser(userField.getText(), passField.getText())
                    == Constants.STATUS_ACCEPTED;

            if (response) {
                errorLabel.setText("Account successfully created.");
            } else {
                errorLabel.setText("Could not create account.");
            }

        } catch (IOException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }
    }

}

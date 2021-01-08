package gui.controllers.login;

import database.DatabaseRequest;
import etc.Constants;
import etc.SHA;
import gui.StageHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class Register implements Initializable {

    @FXML
    TextField userField, passField;

    @FXML
    Label errorLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //No special characters, size of 8 only
        for (TextField node : List.of(userField, passField))
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

        StageHelper.closeStage(Constants.Window.REGISTER);

    }

    public void onRegisterButton() {

        if (userField.getText().length() < 3) {

            errorLabel.setText("Username must be at least 3 characters.");

        } else {

            try {

                boolean response = DatabaseRequest.registerUser(userField.getText(), SHA.encrypt(passField.getText()))
                        == Constants.STATUS_ACCEPTED;

                if (response) {
                    errorLabel.setText("Account successfully created.");
                } else {
                    errorLabel.setText("Could not create account.");
                }

            } catch (Exception e) {
                errorLabel.setText("Error connecting to database.");
            }

        }
    }
}

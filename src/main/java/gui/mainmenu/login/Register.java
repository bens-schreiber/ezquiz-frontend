package gui.mainmenu.login;

import etc.Constants;
import etc.SHA;
import gui.PrimaryStageHolder;
import gui.etc.FXHelper;
import gui.popup.error.ErrorNotifier;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import requests.DatabaseRequest;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class Register implements Initializable {

    @FXML
    TextField usernameField, passwordField;

    @FXML
    Label errorLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //No special characters, size of 8 only
        for (TextField node : List.of(usernameField, passwordField))
            node.setTextFormatter(new TextFormatter<>(change -> {
                if (change.getText().matches("[ $&+,:;=\\\\?@#|/'<>.^*()%!-]")) {
                    change.setText("");
                } else if (node.getText().length() > Constants.MAX_PASS_USER_SIZE) {
                    change.setText("");
                }

                return change;
            }));
    }

    public void registerButtonClicked() {

        if (usernameField.getText().length() < 3) {

            errorLabel.setText("Username must be at least 3 characters.");

        } else {

            try {

                boolean response = DatabaseRequest.registerUser(usernameField.getText(), SHA.encrypt(passwordField.getText()))
                        == Constants.STATUS_ACCEPTED;

                errorLabel.setText(response ? "Account successfully created." : "Could not create account.");

                if (response) {
                    DatabaseRequest.verifyLoginCredentials(usernameField.getText(), SHA.encrypt(passwordField.getText()));
                    PrimaryStageHolder.getPrimaryStage().setScene(FXHelper.getScene(FXHelper.Window.MAINMENU));
                }

            } catch (Exception e) {
                errorLabel.setText("Error connecting to database.");
            }

        }
    }

    public void loginButtonClicked() {

        try {
            PrimaryStageHolder.getPrimaryStage().setScene(FXHelper.getScene(FXHelper.Window.LOGIN));
        } catch (Exception e) {

            new ErrorNotifier("A page failed to load", true).display(PrimaryStageHolder.getPrimaryStage());

            e.printStackTrace();

        }
    }
}

package gui.mainmenu.login;

import etc.Constants;
import gui.PrimaryStageHolder;
import gui.etc.FXHelper;
import gui.popup.notification.UserNotifier;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import requests.DatabaseRequest;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class Register implements Initializable {

    @FXML
    TextField usernameField;

    @FXML
    PasswordField passwordField;

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

                //Requests return Status Enum from rest server.
                switch (DatabaseRequest.postNewUser(usernameField.getText(), SHA.encrypt(passwordField.getText()))) {

                    case ACCEPTED:
                        DatabaseRequest.verifyLoginCredentials(usernameField.getText(), SHA.encrypt(passwordField.getText()));
                        PrimaryStageHolder.getPrimaryStage().setScene(FXHelper.getScene(FXHelper.Window.MAINMENU));
                        break;

                    case UNAUTHORIZED:
                        errorLabel.setText("Incorrect username or password.");
                        break;

                    case NO_CONNECTION, NO_CONTENT:
                        errorLabel.setText("Error connecting to database.");
                }

            } catch (Exception e) {
                errorLabel.setText("An unknown error occurred.");
            }

        }
    }

    public void loginButtonClicked() {

        try {
            PrimaryStageHolder.getPrimaryStage().setScene(FXHelper.getScene(FXHelper.Window.LOGIN));
        } catch (Exception e) {

            new UserNotifier("A page failed to load").display();

            e.printStackTrace();

        }
    }
}

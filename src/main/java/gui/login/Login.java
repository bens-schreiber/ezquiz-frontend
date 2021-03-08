package gui.login;

import etc.Constants;
import gui.FXController;
import gui.FXHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import requests.DatabaseRequest;
import requests.Status;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class Login extends FXController implements Initializable {

    @FXML
    TextField usernameField;

    @FXML
    PasswordField passwordField;

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

    public void loginButtonClicked() {

        if (usernameField.getText().length() < 3) {

            errorLabel.setText("Username must be at least 3 characters.");

        } else {

            try {

                //Requests return Status Enum from rest server.
                switch (DatabaseRequest.verifyLoginCredentials(usernameField.getText(), SHA.encrypt(passwordField.getText()))) {

                    case ACCEPTED -> {
                        FXController.getPrimaryStage().close();
                        FXController.getPrimaryStage().setScene(FXHelper.getScene(FXHelper.Window.MAIN_MENU));
                        FXController.getPrimaryStage().show();
                    }
                    case UNAUTHORIZED -> errorLabel.setText("Incorrect username or password.");
                    case NO_CONNECTION, NO_CONTENT -> errorHandle(Status.NO_CONNECTION);
                }

            } catch (Exception e) {
                errorHandle();
            }
        }
    }

    public void registerButtonClicked() {

        try {
            FXController.getPrimaryStage().setScene(FXHelper.getScene(FXHelper.Window.REGISTER));
        } catch (Exception e) {

            errorHandle();
            e.printStackTrace();

        }


    }


}

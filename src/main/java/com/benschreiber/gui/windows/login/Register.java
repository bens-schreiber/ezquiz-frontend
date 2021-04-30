package com.benschreiber.gui.windows.login;

import com.benschreiber.gui.Constants;
import com.benschreiber.gui.FXController;
import com.benschreiber.gui.FXHelper;
import com.benschreiber.requests.DatabaseRequest;
import com.benschreiber.requests.Status;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class Register extends FXController implements Initializable {

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

        if (usernameField.getText().length() < 3 || passwordField.getText().length()  < 3) {

            errorLabel.setText("Username and password must be at least 3 characters.");

        } else {

            try {

                //Requests return Status Enum from rest server.
                switch (DatabaseRequest.postNewUser(usernameField.getText(), SHA.encrypt(passwordField.getText()))) {
                    case ACCEPTED -> {
                        DatabaseRequest.verifyLoginCredentials(usernameField.getText(), SHA.encrypt(passwordField.getText()));
                        FXController.getPrimaryStage().close();
                        FXController.getPrimaryStage().setScene(FXHelper.getScene(FXHelper.Window.MAIN_MENU));
                        FXController.getPrimaryStage().show();
                    }
                    case UNAUTHORIZED -> errorLabel.setText("Account name taken.");
                    case NO_CONNECTION, NO_CONTENT -> errorHandle(Status.NO_CONNECTION);
                }

            } catch (Exception e) {
                errorHandle();
            }

        }
    }

    public void loginButtonClicked() {

        try {
            FXController.getPrimaryStage().setScene(FXHelper.getScene(FXHelper.Window.LOGIN));
        } catch (Exception e) {

            userNotifier.setText("A page failed to load").display();

            e.printStackTrace();

        }
    }
}

package gui.startmenu.login;

import etc.Constants;
import etc.SHA;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import requests.DatabaseRequest;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class Register implements Initializable {

    @FXML
    TextField userField, passField;

    @FXML
    Label errorLabel;

    private static Stage stage;

    public static void setStage(Stage stage) {
        Register.stage = stage;
    }

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

    public void exitButtonClicked() {
        stage.close();
    }

    public void registerButtonClicked() {

        if (userField.getText().length() < 3) {

            errorLabel.setText("Username must be at least 3 characters.");

        } else {

            try {

                boolean response = DatabaseRequest.registerUser(userField.getText(), SHA.encrypt(passField.getText()))
                        == Constants.STATUS_ACCEPTED;

                errorLabel.setText(response ? "Account successfully created." : "Could not create account.");

            } catch (Exception e) {
                errorLabel.setText("Error connecting to database.");
            }

        }
    }
}

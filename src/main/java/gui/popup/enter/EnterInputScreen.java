package gui.popup.enter;

import gui.account.Account;
import gui.popup.notification.UserNotifier;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import requests.DatabaseRequest;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class EnterInputScreen implements Initializable {

    @FXML
    TextField codeTextField;

    //Since it is a popup window, save the stage here
    static Stage stage;

    static boolean response;

    public void enterCodeButtonClicked() {
        try {

            switch (DatabaseRequest.getQuizPathFromKey(Integer.parseInt(codeTextField.getText()), Account.getUser())) {
                case ACCEPTED:
                    response = true;
                    break;

                case NO_CONTENT:
                    response = false;
                    new UserNotifier("Could not find Quiz from key.").display(stage);
                    break;

                case NO_CONNECTION:
                    response = false;
                    new UserNotifier("Connection to the server failed.").display(stage);
                    break;
            }

            stage.close();

        } catch (Exception e) {
            response = false;
            e.printStackTrace();
            new UserNotifier("An unknown error occurred.").display(stage);
        }

    }

    public void exitButtonClicked() {
        response = false;
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        UnaryOperator<TextFormatter.Change> modifyChange = c -> {
            if (!c.getText().matches("[\\d]")) {
                c.setText("");
            }
            if (c.isContentChange()) {
                int newLength = c.getControlNewText().length();
                if (newLength > 4) {
                    c.setText("");
                }
            }
            return c;
        };

        codeTextField.setTextFormatter(new TextFormatter<>(modifyChange));
    }


}

package gui.popup.enter;

import gui.popup.error.ErrorNotifier;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import requests.DatabaseRequest;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class EnterKey implements Initializable {

    @FXML
    TextField codeTextField;

    //Since it is a popup window, save the stage here
    static Stage stage;

    static boolean response;

    public void enterCodeButtonClicked() {
        try {

            if (DatabaseRequest.setQuizPathFromKey(Integer.parseInt(codeTextField.getText()))) {
                response = true;
                stage.close();
            } else {
                response = false;
                new ErrorNotifier("Could not find Quiz from key.", false).display(stage);
            }

        } catch (Exception e) {
            response = false;
            e.printStackTrace();
            new ErrorNotifier("An error occurred.", false).display(stage);
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

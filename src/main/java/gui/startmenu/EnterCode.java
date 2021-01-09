package gui.startmenu;

import etc.BitMap;
import gui.popup.error.ErrorNotifier;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import org.json.JSONException;
import quiz.QuizManager;
import requests.DatabaseRequest;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class EnterCode extends StageHolder implements Initializable {

    @FXML TextField codeTextField;

    static Stage stage;

    public void enterCodeButtonClicked() {
        try {

            long bitmap = DatabaseRequest.getTestKey(Integer.parseInt(codeTextField.getText()));
            QuizManager.loadQuestions(new BitMap(bitmap).decodeToList());


            displayQuiz();
            stage.close();

        } catch (NumberFormatException | InterruptedException | IOException | JSONException e) {
            new ErrorNotifier("Could not get test from code.", false).display();
        }

    }

    public void exitButtonClicked() {
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        UnaryOperator<TextFormatter.Change> modifyChange = c -> {
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

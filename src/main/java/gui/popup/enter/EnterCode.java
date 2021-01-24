package gui.popup.enter;

import etc.BitMap;
import gui.popup.error.ErrorNotifier;
import gui.quiz.QuizHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;
import questions.QuizQuestions;
import requests.DatabaseRequest;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class EnterCode implements Initializable {

    @FXML
    TextField codeTextField;
    //Since it is a popup window, save the stage here
    static Stage stage;

    public void enterCodeButtonClicked() {
        try {

            long bitmap = DatabaseRequest.getQuizRetakeCode(Integer.parseInt(codeTextField.getText()));

            QuizQuestions.initializeQuestions(new BitMap(bitmap).decodeToList());

            QuizHelper.startQuiz(false);
            stage.close();

        } catch (Exception e) {
            new ErrorNotifier("Could not get test from code.", false).display(stage);
        }

    }

    public void exitButtonClicked() {
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

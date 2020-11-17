package stages.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.json.JSONException;
import quiz.questions.Question;
import quiz.QuizController;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ResultsController implements Initializable {
    @FXML
    Label resultsArea;

    @FXML
    Label points;

    @FXML

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        List<Question> correctQuestions = null;
        try {
            correctQuestions = QuizController.checkAnswers();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }

        if (correctQuestions == null) resultsArea.setText("No correct answers.");

        else {
            resultsArea.setText("");
            points.setText(points.getText() + String.valueOf(correctQuestions.size()));

            for (Question correctQuestion : correctQuestions) {
                resultsArea.setText(
                        resultsArea.getText() + "\n"
                                + correctQuestion.getPrompt() + " " + correctQuestion.getAnswer().toString()
                );

            }
        }

    }
}

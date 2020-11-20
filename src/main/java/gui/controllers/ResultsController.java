package gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import org.json.JSONException;
import quiz.questions.Question;
import quiz.QuizManager;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


/**
 * Results page controller
 */

public class ResultsController implements Initializable {

    @FXML
    private Label resultsArea, points;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        List<Question> correctQuestions = null;

        try { //Attempt to check answers.

            correctQuestions = QuizManager.checkAnswers();

        } catch (IOException | JSONException e) {

            e.printStackTrace();

        }

        if (correctQuestions == null) {//If no correct answers, display it.

            resultsArea.setText("No correct answers.");

        } else {

            resultsArea.setText("");

            points.setText(points.getText() + correctQuestions.size());

            for (Question correctQuestion : correctQuestions) {//Format the text to display.

                resultsArea.setText(
                        resultsArea.getText() + "\n"
                                + correctQuestion.getPrompt() + " " + correctQuestion.getAnswer().toString()
                );

            }
        }

    }
}

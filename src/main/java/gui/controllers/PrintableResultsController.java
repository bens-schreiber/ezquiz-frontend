package gui.controllers;

import gui.GuiHelper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import org.json.JSONException;
import quiz.QuizManager;
import quiz.questions.nodes.QuizNode;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class PrintableResultsController implements Initializable {

    @FXML
    Label testName;

    @FXML
    Label resultsArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try { //Attempt to check answers.

            QuizManager.checkAnswers();

        } catch (IOException | JSONException e) {

            e.printStackTrace();

        }

        testName.setText(testName.getText() + QuizManager.getPreferences().get("Quiz Name"));

        float correctAnswers = 0;
        for (QuizNode quizNode : QuizManager.getQuizNodes()) {
            if (quizNode.isCorrect()) {
                correctAnswers++;
            }
        }

        resultsArea.setText(
                (int) correctAnswers + " out of " + QuizManager.getQuizNodes().size() + "\n"
                        + correctAnswers / QuizManager.getQuizNodes().size() + "%"
        );

    }

    public void onPrintButton(ActionEvent actionEvent) {
    }

    public void onSeeQuestions() {
        //Grab results fxml
        Parent results = null;
        try {
            results = FXMLLoader.load(getClass().getResource("/questionresults.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        assert results != null;
        Scene scene = new Scene(results);
        GuiHelper.getOpenedWindows().get("Results").setScene(scene);
    }
}

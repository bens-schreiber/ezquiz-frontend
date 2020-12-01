package gui.controllers;

import gui.GuiHelper;
import gui.addons.ErrorBox;
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
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Base64;

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

            ErrorBox.display("The questions failed to be graded.", true);
            e.printStackTrace();

        }

        testName.setText(testName.getText() + QuizManager.getPreferences().get("Quiz Name"));

        float correctAnswers = 0;
        ArrayList<Integer> ids = new ArrayList<>();
        for (QuizNode quizNode : QuizManager.getQuizNodes()) {

            ids.add(quizNode.getQuestion().getID());

            if (quizNode.isCorrect()) {
                correctAnswers++;
            }
        }

        //Create a bitmap data structure
        long bMap = 0;
        for (Integer id : ids) {
            bMap |= (1L << (id - 1));
        }

        //add how many correct out of possible, percentage, and retake code from bitmap to base64
        System.out.println(Base64.getEncoder().withoutPadding().encodeToString(String.valueOf(bMap).getBytes()));
        resultsArea.setText(
                (int) correctAnswers + " out of " + QuizManager.getQuizNodes().size() + "\n"
                        + correctAnswers / QuizManager.getQuizNodes().size() + "%" + "\n"
                        + Base64.getEncoder().withoutPadding().encodeToString(String.valueOf(bMap).getBytes())
        );

    }


    public void onPrintButton(ActionEvent actionEvent) {
    }

    public void onSeeQuestions() {
        //Grab results fxml

        try {
            Parent results = FXMLLoader.load(getClass().getResource("/questionresults.fxml"));
            Scene scene = new Scene(results);
            GuiHelper.getOpenedWindows().get("Results").setScene(scene);
        } catch (IOException | NullPointerException e) {
            ErrorBox.display("A page failed to load.", false);
            e.printStackTrace();
        }
    }
}

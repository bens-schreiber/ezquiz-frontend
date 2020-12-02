package gui.controllers.results;

import gui.StageHelper;
import gui.popups.ErrorBox;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import quiz.QuizManager;
import quiz.questions.nodes.QuizNode;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Base64;

/**
 * Provides methods for ActionEvents on Printable Results Page.
 */

public class PrintableResultsController implements Initializable {

    @FXML
    Label testName;

    @FXML
    Label resultsArea;

    /**
     * Initial startup method.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        QuizManager.checkAnswers();

        testName.setText(testName.getText() + QuizManager.getPreferences().get("Quiz Name"));

        float correctAnswers = 0;
        ArrayList<Integer> ids = new ArrayList<>();
        for (QuizNode quizNode : QuizManager.getQuizNodes()) {

            ids.add(quizNode.getQuestion().getID());

            if (quizNode.isCorrect()) {
                correctAnswers++;
            }
        }

        //Create a bitmap data structure from ids
        long bMap = 0;
        for (Integer id : ids) {
            bMap |= (1L << (id - 1));
        }

        //add how many correct out of possible, percentage, put bitmap to Base64
        System.out.println(Base64.getEncoder().withoutPadding().encodeToString(String.valueOf(bMap).getBytes()));
        resultsArea.setText(
                (int) correctAnswers + " out of " + QuizManager.getQuizNodes().size() + "\n"
                        + correctAnswers / QuizManager.getQuizNodes().size() + "%" + "\n"
                        + Base64.getEncoder().withoutPadding().encodeToString(String.valueOf(bMap).getBytes())
        );

    }


    public void onPrintButton() {
    }

    public void onSeeQuestions() {

        //Try not to reload page if already created
        if (StageHelper.getScenes().containsKey("questionresults")) {

            StageHelper.getStages().get("Quiz").setScene(StageHelper.getScenes().get("questionresults"));

        } else {
            try {

                Parent results = FXMLLoader.load(getClass().getResource("/questionresults.fxml"));
                Scene scene = new Scene(results);
                StageHelper.addScene("questionresults", scene);
                StageHelper.getStages().get("Quiz").setScene(scene);

            } catch (IOException | NullPointerException e) {
                ErrorBox.display("A page failed to load.", false);
                e.printStackTrace();
            }
        }
    }
}

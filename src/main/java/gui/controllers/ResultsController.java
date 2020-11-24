package gui.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.json.JSONException;
import quiz.questions.Question;
import quiz.QuizManager;
import quiz.questions.nodes.QuizNode;

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

    @FXML
    VBox correctAnswersVBox;

    private int correctAnswers = 0;


    @Override
    public void initialize(URL location, ResourceBundle resources) {


        correctAnswersVBox.setSpacing(50);

        try { //Attempt to check answers.

            QuizManager.checkAnswers();

        } catch (IOException | JSONException e) {

            e.printStackTrace();

        }

        for (QuizNode quizNode : QuizManager.getQuestions()) {

            //Make a container for the answered question, add question to it
            VBox answeredQuestion = new VBox(15);
            answeredQuestion.getChildren().add(new Label(quizNode.getQuestion().getPrompt()));
            answeredQuestion.getChildren().add(quizNode.getNode());


            //Make un-intractable.
            quizNode.getNode().setFocusTraversable(false);
            quizNode.getNode().setMouseTransparent(true);

            if (quizNode.isCorrect()) {

                answeredQuestion.setStyle("-fx-background-color: rgba(86, 234, 99, .5);");
                correctAnswers++;

            } else {

                answeredQuestion.setStyle("-fx-background-color: rgba(195, 33, 72, .7);");

            }

            //Add container to correct answers container.
            correctAnswersVBox.getChildren().add(answeredQuestion);
        }

        points.setText("Score " + correctAnswers + "/" + QuizManager.getQuestions().size());
    }

}


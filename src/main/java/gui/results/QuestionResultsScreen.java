package gui.results;

import gui.PrimaryStageHolder;
import gui.etc.FXHelper;
import gui.etc.Window;
import gui.popup.error.ErrorNotifier;
import gui.quiz.Preference;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import questions.QuizQuestions;
import questions.question.QuestionNode;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * Provides methods for ActionEvents on Results page.
 * Shows the missed questions, if enabled.
 */

public class QuestionResultsScreen implements Initializable {

    @FXML
    VBox correctAnswersVBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Create question answers
        int quizNumber = 1;
        for (QuestionNode questionNode : QuizQuestions.getQuestionNodes()) {

            //Make a container for the answered question, add question to it
            VBox answeredQuestion = new VBox(15);

            //Display question number before question, make bold
            Label label = new Label("Question " + quizNumber + ":");
            label.setStyle("-fx-font-weight: bold;");
            answeredQuestion.getChildren().add(label);

            answeredQuestion.getChildren().add(new Label(questionNode.getPrompt()));
            answeredQuestion.getChildren().add(questionNode.getNode());

            //Make un-intractable.
            questionNode.getNode().setFocusTraversable(false);
            questionNode.getNode().setMouseTransparent(true);

            if (questionNode.isCorrect()) {

                //Set background to green with low opacity
                answeredQuestion.setStyle("-fx-background-color: rgba(86, 234, 99, .5);");

            } else {

                //Set background to red with low opacity
                answeredQuestion.setStyle("-fx-background-color: rgba(255, 158, 173, .7);");

                //Show correct answer if desired in preferences
                if (Boolean.parseBoolean(Preference.preferences.get(Preference.SHOWANSWERS))) {

                    answeredQuestion.getChildren().add(new Label("Correct answer: "
                            + questionNode.getAnswer()
                            .toString()
                            .replace("[", "")
                            .replace("]", "")
                    ));
                }

            }
            //Add container to correct answers container.
            correctAnswersVBox.getChildren().add(answeredQuestion);
            quizNumber++;
        }
    }

    public void backButtonClicked() {

        try {

            PrimaryStageHolder.getPrimaryStage().setScene(FXHelper.getScene(Window.PRINTRESULTS));

        } catch (IOException e) {
            new ErrorNotifier("Results could not display.", true).display();
        }
    }
}


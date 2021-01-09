package gui.results;

import etc.Preference;
import gui.etc.FXHelper;
import gui.etc.Window;
import gui.popup.error.ErrorNotifier;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import quiz.QuizManager;
import quiz.nodes.QuizNode;

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

    private static Stage primaryStage;

    public static void setPrimaryStage(Stage primaryStage) {
        QuestionResultsScreen.primaryStage = primaryStage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Create question answers
        int quizNumber = 1;
        for (QuizNode quizNode : QuizManager.getQuizNodes()) {

            //Make a container for the answered question, add question to it
            VBox answeredQuestion = new VBox(15);

            //Display question number before question, make bold
            Label label = new Label("Question " + quizNumber + ":");
            label.setStyle("-fx-font-weight: bold;");
            answeredQuestion.getChildren().add(label);

            answeredQuestion.getChildren().add(new Label(quizNode.getQuestion().getPrompt()));
            answeredQuestion.getChildren().add(quizNode.getNode());

            //Make un-intractable.
            quizNode.getNode().setFocusTraversable(false);
            quizNode.getNode().setMouseTransparent(true);

            if (quizNode.isCorrect()) {

                //Set background to green with low opacity
                answeredQuestion.setStyle("-fx-background-color: rgba(86, 234, 99, .5);");

            } else {

                //Set background to red with low opacity
                answeredQuestion.setStyle("-fx-background-color: rgba(255, 158, 173, .7);");

                //Show correct answer if desired in preferences
                if (Boolean.parseBoolean(QuizManager.getPreferences().get(Preference.SHOWANSWERS))) {

                    answeredQuestion.getChildren().add(new Label("Correct answer: "
                            + quizNode.getQuestion().getAnswer()
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
            primaryStage.setScene(FXHelper.getScene(Window.PRINTRESULTS));
            PrintResultsScreen.setPrimaryStage(primaryStage);
        } catch (IOException e) {
            new ErrorNotifier("Results could not display.", true).display();
        }
    }
}


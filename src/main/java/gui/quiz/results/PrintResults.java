package gui.quiz.results;

import gui.FXController;
import gui.account.Account;
import gui.etc.FXHelper;
import gui.quiz.QuizHelper;
import gui.quiz.QuizQuestions;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import questions.question.QuestionNode;
import requests.DatabaseRequest;

import javax.imageio.ImageIO;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Scene containing results you can print.
 */
public class PrintResults extends FXController implements Initializable {

    @FXML
    Label outOfLabel, percentageLabel;

    @FXML
    VBox questionsVBox;

    /**
     * Initial startup method.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Check all answers
        QuizQuestions.gradeAnswers();

        //todo: make all this better now
        loadQuestions();

        //Get the amount of correct answers, get ID's for bitmap storage
        int correctAnswers = 0;
        for (QuestionNode questionNode : QuizQuestions.getQuestionNodes()) {
            if (questionNode.isCorrect()) {
                correctAnswers++;
            }
        }

        try {

            switch (DatabaseRequest.postQuizScore(correctAnswers, Account.getUser(), Account.getQuiz())) {

                case NO_CONNECTION -> userNotifier.setText(AlertText.NO_CONNECTION).display();

                case NO_CONTENT -> userNotifier.setText(AlertText.EXTERNAL_ERROR);

            }

        } catch (Exception e) {
            e.printStackTrace();
            userNotifier.setText(AlertText.INTERNAL_ERROR).display();
        }

        //add how many correct out of possible, percentage, put bitmap to Base64
        outOfLabel.setText(correctAnswers + " out of " + QuizQuestions.getQuestionNodes().length);
        percentageLabel.setText(((double) correctAnswers / (double) QuizQuestions.getQuestionNodes().length * 100) + "%");

    }

    public void backToMainMenuClicked() {
        try {

            FXController.setPrimaryStage(FXHelper.getPopupStage(FXHelper.Window.MAIN_MENU, false));

        } catch (Exception e) {
            e.printStackTrace();
            userNotifier.setText(AlertText.INTERNAL_ERROR).display();
        }
    }

    public void screenshotButtonClicked() {

        try {

            Scene scene = FXController.getPrimaryStage().getScene();
            WritableImage writableImage = scene.snapshot(null);

            DirectoryChooser directoryChooser = new DirectoryChooser();
            File file = directoryChooser.showDialog(FXController.getPrimaryStage());

            ImageIO.write(
                    SwingFXUtils.fromFXImage(writableImage, null),
                    "png", new File(file.getAbsolutePath() + "/screenshot")
            );

        } catch (Exception e) {
            e.printStackTrace();
            userNotifier.setText(AlertText.INTERNAL_ERROR).display();
        }
    }

    public void printButtonClicked() {

    }


    public void loadQuestions() {
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
                if (Boolean.parseBoolean(QuizHelper.Preference.getPreferences().get(QuizHelper.Preference.SHOWANSWERS))) {

                    answeredQuestion.getChildren().add(new Label("Correct answer: "
                            + questionNode.getAnswer()
                            .toString()
                            .replace("[", "")
                            .replace("]", "")
                    ));
                }

            }
            //Add container to correct answers container.
            questionsVBox.getChildren().add(answeredQuestion);
            quizNumber++;
        }
    }

}

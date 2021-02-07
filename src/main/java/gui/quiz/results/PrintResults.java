package gui.quiz.results;

import gui.PrimaryStageHolder;
import gui.account.Account;
import gui.etc.FXHelper;
import gui.popup.notification.UserNotifier;
import gui.quiz.QuizQuestions;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.stage.DirectoryChooser;
import questions.question.QuestionNode;
import requests.DatabaseRequest;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Scene containing results you can print.
 */
public class PrintResults implements Initializable {

    @FXML
    Label testName, outOfLabel, percentageLabel;

    @FXML
    Button seeQuestionsButton;

    /**
     * Initial startup method.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Set test name
        testName.setText(Account.getQuiz().getName());

        //Check all answers
        QuizQuestions.gradeAnswers();

        //Get the amount of correct answers, get ID's for bitmap storage
        int correctAnswers = 0;
        for (QuestionNode questionNode : QuizQuestions.getQuestionNodes()) {
            if (questionNode.isCorrect()) {
                correctAnswers++;
            }
        }

        try {

            switch (DatabaseRequest.postQuizScore(correctAnswers, Account.getUser(), Account.getQuiz())) {

                case NO_CONNECTION -> new UserNotifier("Connection to the server failed.").display();

                case NO_CONTENT -> new UserNotifier("An error occurred while uploading score to server.");

            }

        } catch (Exception e) {
            e.printStackTrace();
            new UserNotifier("An unknown error occurred.").display();
        }

        //add how many correct out of possible, percentage, put bitmap to Base64
        outOfLabel.setText(correctAnswers + " out of " + QuizQuestions.getQuestionNodes().length);
        percentageLabel.setText(((double) correctAnswers / (double) QuizQuestions.getQuestionNodes().length * 100) + "%");

    }

    public void backToMainMenuClicked() {
        try {

            PrimaryStageHolder.getPrimaryStage().close();
            PrimaryStageHolder.setPrimaryStage(FXHelper.getPopupStage(FXHelper.Window.MAIN_MENU, false));
            PrimaryStageHolder.getPrimaryStage().show();

        } catch (IOException e) {
            new UserNotifier("Results could not display.").display();
            e.printStackTrace();
        }
    }

    public void screenshotButtonClicked() {

        try {
            Scene scene = PrimaryStageHolder.getPrimaryStage().getScene();
            WritableImage writableImage = scene.snapshot(null);

            DirectoryChooser directoryChooser = new DirectoryChooser();
            File file = directoryChooser.showDialog(PrimaryStageHolder.getPrimaryStage());

            ImageIO.write(
                    SwingFXUtils.fromFXImage(writableImage, null),
                    "png", new File(file.getAbsolutePath() + "/screenshot")
            );

        } catch (IOException ex) {

            new UserNotifier("An error occurred trying to screenshot").display();
            ex.printStackTrace();

        }
    }

    public void copyRetakeButtonClicked() {

    }

    public void printButtonClicked() {

    }

    public void seeQuestionsButtonClicked() {

        try {

            PrimaryStageHolder.getPrimaryStage().setScene(FXHelper.getScene(FXHelper.Window.QUESTION_RESULTS));

        } catch (IOException e) {
            new UserNotifier("Results could not display.").display();
            e.printStackTrace();
        }
    }

}

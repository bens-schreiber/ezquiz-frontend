package gui.mainmenu;

import gui.PrimaryStageHolder;
import gui.etc.Account;
import gui.etc.FXHelper;
import gui.popup.confirm.ConfirmNotifier;
import gui.popup.error.ErrorNotifier;
import gui.quiz.QuizHelper;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.json.JSONException;
import questions.QuizQuestions;
import requests.QuestionJSONRequest;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenu implements Initializable {

    @FXML
    AnchorPane mainDisplay;

    @FXML
    VBox buttonContainer;

    @FXML
    HBox adminContainer;

    @FXML
    Label usernameLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        quizzesButtonClicked();

        usernameLabel.setText(Account.getUsername());

        if (!Account.isAdmin()) {
            buttonContainer.getChildren().remove(adminContainer);
        }

    }


    public void randomQuizClicked() {

        if (Account.getQuiz() != null) {
            if (new ConfirmNotifier("Are you sure you want to take: " + Account.getQuiz().getName()).display().getResponse())
                QuizHelper.startQuiz(true);
        } else new ErrorNotifier("You have not selected a Quiz", false).display(PrimaryStageHolder.getPrimaryStage());

    }

    public void quizzesButtonClicked() {
        try {

            mainDisplay.getChildren().setAll(FXHelper.getPane(FXHelper.Window.SAVEDQUIZZES));

        } catch (Exception e) {
            new ErrorNotifier("Page failed to load", false).display(PrimaryStageHolder.getPrimaryStage());
        }
    }

    public void adminButtonClicked() {

        try {
            mainDisplay.getChildren().setAll(FXHelper.getPane(FXHelper.Window.ADMINMENU));
        } catch (Exception e) {
            e.printStackTrace();
            new ErrorNotifier("Page failed to load", false).display(PrimaryStageHolder.getPrimaryStage());
        }

    }

    public void takeQuizClicked() {

        if (Account.getQuiz() != null) {

            if (new ConfirmNotifier("Are you sure you want to take: " + Account.getQuiz().getName())
                    .display().getResponse()) {
                try {

                    QuizQuestions.initializeQuestions(0, new QuestionJSONRequest());

                    QuizHelper.startQuiz(false);
                } catch (JSONException | IOException | InterruptedException e) {

                    new ErrorNotifier("Quiz failed to load", false).display(PrimaryStageHolder.getPrimaryStage());

                }
            }

        } else new ErrorNotifier("You have not selected a Quiz", false).display(PrimaryStageHolder.getPrimaryStage());

    }

    public void logoutButtonClicked() {

        try {

            Account.logout();

            PrimaryStageHolder.getPrimaryStage().close();

            PrimaryStageHolder.setPrimaryStage(FXHelper.getPopupStage(FXHelper.Window.LOGIN, false));

            PrimaryStageHolder.getPrimaryStage().show();


        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void exitButtonClicked() {
        Platform.exit();
    }

}


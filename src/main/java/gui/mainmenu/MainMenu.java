package gui.mainmenu;

import etc.Constants;
import gui.FXController;
import gui.account.Account;
import gui.etc.FXHelper;
import gui.quiz.QuizHelper;
import gui.quiz.QuizQuestions;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenu extends FXController implements Initializable {

    @FXML
    Pane mainDisplay;

    @FXML
    VBox buttonContainer;

    @FXML
    HBox adminContainer;

    @FXML
    Label usernameLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        quizzesButtonClicked();

        usernameLabel.setText(Account.getUser().getUsername());

        if (!Account.getUser().isAdmin()) {
            buttonContainer.getChildren().remove(adminContainer);
        }

    }


    public void randomQuizClicked() {

        if (Account.getQuiz() != null) {

            if (confirmNotifier.setPrompt("Are you sure you want to take: " + Account.getQuiz().getName()).display().getResponse()) {

                try {

                    QuizHelper.startQuiz(true);

                } catch (Exception e) {
                    e.printStackTrace();
                    userNotifier.setText(AlertText.INTERNAL_ERROR).display();

                }

            }

        } else userNotifier.setText("You have not selected a Quiz").display();

    }

    public void quizzesButtonClicked() {

        try {

            mainDisplay.getChildren().setAll(FXHelper.getPane(FXHelper.Window.QUIZZES_MENU));

        } catch (Exception e) {

            e.printStackTrace();
            userNotifier.setText(AlertText.INTERNAL_ERROR).display();

        }
    }

    public void adminButtonClicked() {

        try {

            mainDisplay.getChildren().setAll(FXHelper.getPane(FXHelper.Window.ADMIN_MENU));

        } catch (Exception e) {

            e.printStackTrace();
            userNotifier.setText(AlertText.INTERNAL_ERROR).display();

        }

    }

    public void takeQuizClicked() {

        if (Account.getQuiz() != null) {

            if (confirmNotifier.setPrompt("Are you sure you want to take: " + Account.getQuiz().getName()).display().getResponse()) {
                try {

                    QuizQuestions.initializeQuiz(Constants.MAXIMUM_QUESTION_AMOUNT);

                    QuizHelper.startQuiz(false);

                } catch (Exception e) {

                    e.printStackTrace();
                    userNotifier.setText(AlertText.INTERNAL_ERROR).display();

                }
            }

        } else userNotifier.setText("You have not selected a Quiz").display();

    }

    public void logoutButtonClicked() {

        try {

            Account.logout();
            FXController.getPrimaryStage().close();
            FXController.setPrimaryStage(FXHelper.getPopupStage(FXHelper.Window.LOGIN, false));
            FXController.getPrimaryStage().show();


        } catch (Exception e) {
            e.printStackTrace();
            userNotifier.setText(AlertText.INTERNAL_ERROR).display();
        }


    }

    public void exitButtonClicked() {
        Platform.exit();
    }

}


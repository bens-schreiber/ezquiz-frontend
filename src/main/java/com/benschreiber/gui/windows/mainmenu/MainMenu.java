package com.benschreiber.gui.windows.mainmenu;

import com.benschreiber.etc.Account;
import com.benschreiber.gui.Constants;
import com.benschreiber.gui.FXController;
import com.benschreiber.gui.FXHelper;
import com.benschreiber.gui.windows.quiz.QuizHelper;
import com.benschreiber.gui.windows.quiz.QuizQuestionHelper;
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

    public void takeQuizClicked() {

        if (Account.getQuiz() != null) {

            if (confirmNotifier.setPrompt("Are you sure you want to take: " + Account.getQuiz().getName()).display().getResponse()) {
                try {

                    QuizQuestionHelper.initializeQuiz(Constants.MAXIMUM_QUESTION_AMOUNT);

                    QuizHelper.startQuiz(false);

                } catch (Exception e) {

                    e.printStackTrace();
                    errorHandle();

                }
            }

        } else userNotifier.setText("You have not selected a Quiz").display();

    }

    public void quizzesButtonClicked() {

        try {

            mainDisplay.getChildren().setAll(FXHelper.getPane(FXHelper.Window.QUIZZES_MENU));

        } catch (Exception e) {

            e.printStackTrace();
            errorHandle();

        }
    }

    public void helpButtonClicked() {
        try {

            mainDisplay.getChildren().setAll(FXHelper.getPane(FXHelper.Window.HELP_MENU));

        } catch (Exception e) {

            e.printStackTrace();
            errorHandle();
        }
    }

    public void adminButtonClicked() {

        try {

            mainDisplay.getChildren().setAll(FXHelper.getPane(FXHelper.Window.ADMIN_MENU));

        } catch (Exception e) {

            e.printStackTrace();
            errorHandle();

        }

    }

    public void logoutButtonClicked() {

        try {

            Account.logout();
            FXController.getPrimaryStage().close();
            FXController.setPrimaryStage(FXHelper.getPopupStage(FXHelper.Window.LOGIN, false));
            FXController.getPrimaryStage().show();


        } catch (Exception e) {
            e.printStackTrace();
            errorHandle();
        }


    }

    public void exitButtonClicked() {
        Platform.exit();
    }

}


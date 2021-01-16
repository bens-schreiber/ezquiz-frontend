package gui.startmenu;

import etc.Constants;
import gui.PrimaryStageHelper;
import gui.etc.FXHelper;
import gui.etc.Window;
import gui.popup.error.ErrorNotifier;
import gui.startmenu.login.Login;
import gui.startmenu.login.Register;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import quiz.QuestionManager;
import requests.Account;

import java.net.URL;
import java.util.ResourceBundle;

public class StartPage extends PrimaryStageHelper implements Initializable {

    @FXML
    VBox buttonVBox;

    @FXML
    Label usernameLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (!Account.isLoggedIn()) {
            //Disable every button but Login and Register, so the user must login.
            buttonVBox.getChildren().forEach(button -> button.setDisable(true));
            buttonVBox.getChildren().get(0).setDisable(false);
            buttonVBox.getChildren().get(1).setDisable(false);
        } else {

            //Remove the Login and Register buttons
            buttonVBox.getChildren().remove(0);
            buttonVBox.getChildren().remove(0);

            usernameLabel.setText(Account.getUsername());
        }

    }

    public void registerButtonClicked() {

        try {

            //Make a popup stage that overlays over Primary Stage. Pass it into Register so it can be closed remotely
            Stage stage = FXHelper.getPopupStage(Window.REGISTER, true);
            Register.setStage(stage);
            stage.showAndWait();

        } catch (Exception e) {

            new ErrorNotifier("A page failed to load", true).display();

            e.printStackTrace();

        }

    }

    public void loginButtonClicked() {

        try {

            Stage stage = FXHelper.getPopupStage(Window.LOGIN, true);
            Login.setStage(stage);
            stage.showAndWait();

            if (Account.isLoggedIn()) {

                //Enable all buttons.
                buttonVBox.getChildren().forEach(button -> button.setDisable(!button.isDisable()));

                //Remove the Login and Register buttons
                buttonVBox.getChildren().remove(0);
                buttonVBox.getChildren().remove(0);

                usernameLabel.setText(Account.getUsername());
            }

        } catch (Exception e) {

            new ErrorNotifier("A page failed to load", true).display();

            e.printStackTrace();

        }

    }

    public void defaultQuizButtonClicked() {

        QuestionManager.loadQuestions(Constants.DEFAULT_QUESTION_AMOUNT, null, null); //Load default quiz.

        //Start test
        displayQuiz(true);

    }

    public void quizzesButtonClicked() {

        try {

            Stage stage = FXHelper.getPopupStage(Window.PREMADEQUIZES, false);
            PremadeQuizzes.stage = stage;
            stage.showAndWait();

        } catch (Exception e) {

            new ErrorNotifier("A page failed to load", true).display();

            e.printStackTrace();

        }

    }

    public void enterCodeButtonClicked() {
        try {

            Stage stage = FXHelper.getPopupStage(Window.ENTERCODE, true);
            EnterCode.stage = stage;
            stage.showAndWait();

        } catch (Exception e) {

            new ErrorNotifier("A page failed to load", true).display();

            e.printStackTrace();

        }

    }

    public void exitButtonClicked() {
        Platform.exit();
    }
}

package gui.startmenu;

import gui.etc.Account;
import gui.etc.FXHelper;
import gui.popup.error.ErrorNotifier;
import gui.quiz.QuizHelper;
import gui.startmenu.login.Login;
import gui.startmenu.login.Register;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class StartPage implements Initializable {

    @FXML
    VBox buttonVBox;

    @FXML
    Button uploadQuiz, takeQuiz, login, register, quizKey;

    @FXML
    Label usernameLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (!Account.isLoggedIn()) {
            //Disable every button but Login and Register, so the user must login.
            buttonVBox.getChildren().forEach(button -> button.setDisable(true));
            login.setDisable(false);
            register.setDisable(false);
        } else {

            //Remove the Login and Register buttons
            buttonVBox.getChildren().remove(login);
            buttonVBox.getChildren().remove(register);

            usernameLabel.setText(Account.getUsername());
        }

    }

    public void registerButtonClicked() {

        try {

            //Make a popup stage that overlays over Primary Stage. Pass it into Register so it can be closed remotely
            Stage stage = FXHelper.getPopupStage(FXHelper.Window.REGISTER, true);
            Register.setStage(stage);
            stage.showAndWait();

        } catch (Exception e) {

            new ErrorNotifier("A page failed to load", true).display();

            e.printStackTrace();

        }

    }

    public void loginButtonClicked() {

        try {

            Stage stage = FXHelper.getPopupStage(FXHelper.Window.LOGIN, true);
            Login.setStage(stage);
            stage.showAndWait();

            if (Account.isLoggedIn()) {


                //Remove the Login and Register buttons
                buttonVBox.getChildren().remove(login);
                buttonVBox.getChildren().remove(register);
                quizKey.setDisable(false);


                if (!Account.isAdmin()) {
                    buttonVBox.getChildren().remove(uploadQuiz);
                } else {
                    uploadQuiz.setDisable(false);
                }

                usernameLabel.setText(Account.getUsername());
            }

        } catch (Exception e) {

            new ErrorNotifier("A page failed to load", true).display();

            e.printStackTrace();

        }

    }

    public void defaultQuizButtonClicked() {

        //Start test
        QuizHelper.startQuiz(true);

    }

    public void enterQuizButtonClicked() {
        try {

            Stage stage = FXHelper.getPopupStage(FXHelper.Window.ENTERKEY, true);
            EnterKey.stage = stage;
            stage.showAndWait();

            if (Account.getQuizPath() != null) {
                takeQuiz.setDisable(false);
            }

        } catch (Exception e) {

            new ErrorNotifier("A page failed to load", true).display();

            e.printStackTrace();

        }

    }

    public void uploadQuizButtonClicked() {
        try {

            Stage stage = FXHelper.getPopupStage(FXHelper.Window.UPLOADQUIZ, false);
            UploadQuiz.stage = stage;
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

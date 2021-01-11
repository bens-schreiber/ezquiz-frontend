package gui;

import etc.Constants;
import gui.etc.FXHelper;
import gui.etc.Window;
import gui.popup.error.ErrorNotifier;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import quiz.QuizManager;

import java.io.IOException;

public class PrimaryStageHelper {

    protected static Stage primaryStage;

    /**
     * @param loadNormal if the test should be default questions. False if otherwise.
     */
    public static void displayQuiz(boolean loadNormal) {
        try {

            primaryStage.close();

            if (loadNormal) {
                QuizManager.loadQuestions(Constants.DEFAULT_QUESTION_AMOUNT, null, null);
            }

            //Make a PopupStage because the test window shares their qualities.
            primaryStage = FXHelper.getPopupStage(Window.QUIZ, false);

            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.show();

        } catch (Exception e) {
            new ErrorNotifier("A page failed to load", true).display();
            e.printStackTrace();
        }

    }

    //Ends the entire test and begins the results page
    public static void endTest() {

        try {

            primaryStage.setScene(FXHelper.getScene(Window.PRINTRESULTS));

        } catch (IOException e) {
            new ErrorNotifier("Results could not display.", true).display();
        }

    }
}

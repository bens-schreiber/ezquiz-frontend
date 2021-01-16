package gui;

import etc.Constants;
import gui.etc.FXHelper;
import gui.etc.Window;
import gui.popup.error.ErrorNotifier;
import gui.startmenu.PremadeQuizzes;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import quiz.QuestionManager;

public class PrimaryStageHelper {

    protected static Stage primaryStage;

    /**
     * @param loadDefault if the test should be default questions. False if otherwise.
     */
    protected static void displayQuiz(boolean loadDefault) {
        try {

            primaryStage.close();

            if (PremadeQuizzes.stage != null) {
                PremadeQuizzes.stage.close();
            }

            if (loadDefault) {
                QuestionManager.loadQuestions(Constants.DEFAULT_QUESTION_AMOUNT, null, null);
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
}

package gui.startmenu;

import etc.Constants;
import gui.etc.FXHelper;
import gui.etc.Window;
import gui.popup.error.ErrorNotifier;
import gui.quiz.QuizController;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import quiz.QuizManager;

public class StageHolder {

    private static Stage primaryStage;

    public void displayQuiz() {
        try {

            primaryStage.close();

            QuizManager.loadQuestions(Constants.DEFAULT_QUESTION_AMOUNT, null, null);

            primaryStage = FXHelper.getPopupStage(Window.QUIZ, false);
            primaryStage.initStyle(StageStyle.UNDECORATED);
            QuizController.setPrimaryStage(primaryStage);
            primaryStage.show();

        } catch (Exception e) {
            new ErrorNotifier("A page failed to load", true).display();
            e.printStackTrace();
        }

    }

    public static void setPrimaryStage(Stage primaryStage) {
        StageHolder.primaryStage = primaryStage;
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}

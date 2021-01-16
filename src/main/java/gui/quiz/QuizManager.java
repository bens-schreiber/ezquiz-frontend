package gui.quiz;

import etc.Constants;
import gui.PrimaryStageHolder;
import gui.etc.FXHelper;
import gui.etc.Window;
import gui.popup.error.ErrorNotifier;
import gui.startmenu.PremadeQuizzes;
import javafx.stage.StageStyle;
import questions.nodes.QuizQuestions;
import requests.QuestionRequest;

import java.io.IOException;

/**
 * Starts and ends quizzes
 */
public class QuizManager {

    //private static String quizAuthToken;

    /**
     * @param loadDefault if the test should be default questions. False if otherwise.
     */
    public static void startQuiz(boolean loadDefault) {
        try {

            PrimaryStageHolder.getPrimaryStage().close();

            if (PremadeQuizzes.stage != null) {
                PremadeQuizzes.stage.close();
            }

            if (loadDefault) {
                QuizQuestions.loadQuestions(Constants.DEFAULT_QUESTION_AMOUNT, new QuestionRequest());
            }

            //Make a PopupStage because the test window shares their qualities.
            PrimaryStageHolder.setPrimaryStage(FXHelper.getPopupStage(Window.QUIZ, false));

            PrimaryStageHolder.getPrimaryStage().initStyle(StageStyle.UNDECORATED);
            PrimaryStageHolder.getPrimaryStage().show();

        } catch (Exception e) {
            new ErrorNotifier("A page failed to load", true).display();
            e.printStackTrace();
        }

    }


    //Ends the entire test and begins the results page
    public static void endQuiz() {
        try {
            PrimaryStageHolder.getPrimaryStage().setScene(FXHelper.getScene(Window.PRINTRESULTS));
        } catch (IOException e) {
            new ErrorNotifier("Results could not display.", true).display();
        }

    }

}

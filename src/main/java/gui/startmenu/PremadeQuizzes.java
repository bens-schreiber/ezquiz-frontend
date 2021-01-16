package gui.startmenu;


//todo: make dis work :(

import gui.PrimaryStageHelper;
import gui.etc.FXHelper;
import gui.etc.Window;
import gui.popup.error.ErrorNotifier;
import javafx.scene.Scene;
import javafx.stage.Stage;
import quiz.Preference;
import quiz.QuestionManager;
import quiz.question.Question;

public class PremadeQuizzes extends PrimaryStageHelper {

    //Since it is a popup window, save the stage here
    public static Stage stage;

    public void networkDesignButtonClicked() {
        QuestionManager.loadQuestions(0, null, Question.Subject.NETWORKDESIGN);

        Preference.preferences.put(Preference.QUIZNAME, "Network Design Quiz");

        stage.close();

        displayQuiz(false);
    }

    public void introToBusButtonClicked() {

        QuestionManager.loadQuestions(0, null, Question.Subject.INTBUS);

        Preference.preferences.put(Preference.QUIZNAME, "Intro to Business Quiz");

        stage.close();

        displayQuiz(false);
    }

    public void busMathButtonClicked() {

        QuestionManager.loadQuestions(0, null, Question.Subject.BUSMATH);

        Preference.preferences.put(Preference.QUIZNAME, "Business Math Quiz");

        stage.close();

        displayQuiz(false);
    }

    public void marketingButtonClicked() {

        QuestionManager.loadQuestions(0, null, Question.Subject.MARKETING);

        Preference.preferences.put(Preference.QUIZNAME, "Marketing Quiz");

        stage.close();

        displayQuiz(false);
    }

    public void customButtonClicked() {

        try {

            Scene scene = FXHelper.getScene(Window.CUSTOMQUIZ);

            stage.close();

            primaryStage.setScene(scene);

        } catch (Exception e) {

            new ErrorNotifier("A page failed to load", true).display();

            e.printStackTrace();

        }

    }

}

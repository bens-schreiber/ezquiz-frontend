package gui.startmenu;


//todo: make dis work :(

import gui.PrimaryStageHolder;
import gui.etc.FXHelper;
import gui.etc.Window;
import gui.popup.error.ErrorNotifier;
import gui.quiz.Preference;
import gui.quiz.QuizManager;
import javafx.scene.Scene;
import javafx.stage.Stage;
import questions.Question;
import questions.nodes.QuizQuestions;

public class PremadeQuizzes {

    //Since it is a popup window, save the stage here
    public static Stage stage;

    public void networkDesignButtonClicked() {
        QuizQuestions.loadQuestions(0, null, Question.Subject.NETWORKDESIGN);

        Preference.preferences.put(Preference.QUIZNAME, "Network Design Quiz");

        stage.close();

        QuizManager.startQuiz(false);
    }

    public void introToBusButtonClicked() {

        QuizQuestions.loadQuestions(0, null, Question.Subject.INTBUS);

        Preference.preferences.put(Preference.QUIZNAME, "Intro to Business Quiz");

        stage.close();

        QuizManager.startQuiz(false);
    }

    public void busMathButtonClicked() {

        QuizQuestions.loadQuestions(0, null, Question.Subject.BUSMATH);

        Preference.preferences.put(Preference.QUIZNAME, "Business Math Quiz");

        stage.close();

        QuizManager.startQuiz(false);
    }

    public void marketingButtonClicked() {

        QuizQuestions.loadQuestions(0, null, Question.Subject.MARKETING);

        Preference.preferences.put(Preference.QUIZNAME, "Marketing Quiz");

        stage.close();

        QuizManager.startQuiz(false);
    }

    public void customButtonClicked() {

        try {

            Scene scene = FXHelper.getScene(Window.CUSTOMQUIZ);

            stage.close();

            PrimaryStageHolder.getPrimaryStage().setScene(scene);

        } catch (Exception e) {

            new ErrorNotifier("A page failed to load", true).display();

            e.printStackTrace();

        }

    }

}

package gui.startmenu;


//todo: make dis work :(

import gui.PrimaryStageHolder;
import gui.etc.FXHelper;
import gui.etc.Window;
import gui.popup.error.ErrorNotifier;
import gui.quiz.Preference;
import gui.quiz.QuizHelper;
import javafx.scene.Scene;
import javafx.stage.Stage;
import questions.QuizQuestions;
import questions.question.Question;
import requests.QuestionRequest;

public class PremadeQuizzes {

    //Since it is a popup window, save the stage here
    public static Stage stage;

    public void networkDesignButtonClicked() {
        QuizQuestions.initializeQuestions(0, new QuestionRequest(Question.Subject.NETWORKDESIGN));

        Preference.preferences.put(Preference.QUIZNAME, "Network Design Quiz");

        QuizHelper.startQuiz(false);
    }

    public void introToBusButtonClicked() {

        QuizQuestions.initializeQuestions(0, new QuestionRequest(Question.Subject.INTBUS));

        Preference.preferences.put(Preference.QUIZNAME, "Intro to Business Quiz");

        QuizHelper.startQuiz(false);
    }

    public void busMathButtonClicked() {

        QuizQuestions.initializeQuestions(0, new QuestionRequest(Question.Subject.BUSMATH));

        Preference.preferences.put(Preference.QUIZNAME, "Business Math Quiz");

        QuizHelper.startQuiz(false);
    }

    public void marketingButtonClicked() {

        QuizQuestions.initializeQuestions(0, new QuestionRequest(Question.Subject.MARKETING));

        Preference.preferences.put(Preference.QUIZNAME, "Marketing Quiz");

        QuizHelper.startQuiz(false);
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

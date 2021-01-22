package gui.startmenu;


//todo: make dis work :(

import gui.PrimaryStageHolder;
import gui.etc.FXHelper;
import gui.popup.error.ErrorNotifier;
import gui.quiz.QuizHelper;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.json.JSONException;
import questions.QuizQuestions;
import questions.question.Question;
import requests.QuestionJSONRequest;

import java.io.IOException;

public class PremadeQuizzes {

    //Since it is a popup window, save the stage here
    public static Stage stage;

    public void networkDesignButtonClicked() {
        try {
            QuizQuestions.initializeQuestions(0, new QuestionJSONRequest(Question.Subject.NETWORKDESIGN));

            QuizHelper.Preference.preferences.put(QuizHelper.Preference.QUIZNAME, "Network Design Quiz");

            QuizHelper.startQuiz(false);

        } catch (InterruptedException | IOException | JSONException e) {

            new ErrorNotifier("A question failed to be created.", true).display(stage);
            e.printStackTrace();
        }
    }

    public void introToBusButtonClicked() {

        try {

            QuizQuestions.initializeQuestions(0, new QuestionJSONRequest(Question.Subject.INTBUS));

            QuizHelper.Preference.preferences.put(QuizHelper.Preference.QUIZNAME, "Intro to Business Quiz");

            QuizHelper.startQuiz(false);

        } catch (InterruptedException | IOException | JSONException e) {

            new ErrorNotifier("A question failed to be created.", true).display(stage);
            e.printStackTrace();
        }
    }

    public void busMathButtonClicked() {

        try {

            QuizQuestions.initializeQuestions(0, new QuestionJSONRequest(Question.Subject.BUSMATH));

            QuizHelper.Preference.preferences.put(QuizHelper.Preference.QUIZNAME, "Business Math Quiz");

            QuizHelper.startQuiz(false);

        } catch (InterruptedException | IOException | JSONException e) {

            new ErrorNotifier("A question failed to be created.", true).display(stage);
            e.printStackTrace();
        }
    }

    public void marketingButtonClicked() {

        try {

            QuizQuestions.initializeQuestions(0, new QuestionJSONRequest(Question.Subject.MARKETING));

            QuizHelper.Preference.preferences.put(QuizHelper.Preference.QUIZNAME, "Marketing Quiz");

            QuizHelper.startQuiz(false);

        } catch (InterruptedException | IOException | JSONException e) {

            new ErrorNotifier("A question failed to be created.", true).display(stage);
            e.printStackTrace();
        }
    }

    public void customButtonClicked() {

        try {

            Scene scene = FXHelper.getScene(FXHelper.Window.CUSTOMQUIZ);

            stage.close();

            PrimaryStageHolder.getPrimaryStage().setScene(scene);

        } catch (Exception e) {

            new ErrorNotifier("A page failed to load", true).display(stage);

            e.printStackTrace();

        }

    }

}

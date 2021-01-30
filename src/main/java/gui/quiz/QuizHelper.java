package gui.quiz;

import etc.Constants;
import gui.PrimaryStageHolder;
import gui.etc.FXHelper;
import gui.popup.notification.UserNotifier;
import javafx.stage.StageStyle;
import org.json.JSONException;
import org.json.JSONObject;
import questions.QuizQuestions;

import java.io.IOException;
import java.util.HashMap;

/**
 * Starts and ends quizzes
 */
public class QuizHelper {

    private QuizHelper() {
    }

    //private static String quizAuthToken;

    /**
     * @param loadDefault if the test should be default questions. False if otherwise.
     */
    public static void startQuiz(boolean loadDefault) {
        try {

            PrimaryStageHolder.getPrimaryStage().close();

            if (loadDefault) {
                QuizQuestions.initializeQuiz(Constants.DEFAULT_QUESTION_AMOUNT);
            }

            //Make a PopupStage because the test window shares their qualities.
            PrimaryStageHolder.setPrimaryStage(FXHelper.getPopupStage(FXHelper.Window.QUIZ, false));

            PrimaryStageHolder.getPrimaryStage().initStyle(StageStyle.UNDECORATED);
            PrimaryStageHolder.getPrimaryStage().show();

        } catch (Exception e) {
            new UserNotifier("A page failed to load").display();
            e.printStackTrace();
        }

    }


    //Ends the entire test and begins the results page
    static void endQuiz() {
        try {
            PrimaryStageHolder.getPrimaryStage().setScene(FXHelper.getScene(FXHelper.Window.PRINTRESULTS));
        } catch (IOException e) {
            new UserNotifier("Results could not display.").display();
        }

    }

    /**
     * Preferences for the quiz, determines what tools are displayed
     */
    public enum Preference {
        //todo: make quiz name a preference
        NOTEPAD,
        CALCULATOR,
        DRAWINGPAD,
        QUIZNAME,
        SHOWANSWERS,
        TIME;

        //Hashmap of user preferences, initialize with default preferences
        private static final HashMap<Preference, String> preferences = new HashMap<>();

        public static void initializePreferences(JSONObject jsonObject) throws JSONException {

            System.out.println(validateJSON(jsonObject));
            if (validateJSON(jsonObject)) {
                preferences.put(CALCULATOR, jsonObject.get("calculator").toString());
                preferences.put(SHOWANSWERS, jsonObject.get("answers").toString());
                preferences.put(DRAWINGPAD, jsonObject.get("drawingpad").toString());
                preferences.put(NOTEPAD, jsonObject.get("notepad").toString());

                System.out.println(preferences.get(NOTEPAD));
            }

        }

        private static boolean validateJSON(JSONObject jsonObject) {
            return jsonObject.has("answers") && jsonObject.has("calculator") && jsonObject.has("notepad")
                    && jsonObject.has("drawingpad");
        }

        public static HashMap<Preference, String> getPreferences() {
            return preferences;
        }
    }
}

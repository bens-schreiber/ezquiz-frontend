package gui.quiz;

import etc.Constants;
import gui.StageHolder;
import gui.etc.FXHelper;
import gui.popup.notification.UserNotifier;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Starts and ends quizzes
 */
public class QuizHelper {

    private QuizHelper() {
    }

    /**
     * @param randomQuestions if the test should be random questions. False otherwise.
     */
    public static void startQuiz(boolean randomQuestions) {
        try {

            if (randomQuestions) {
                QuizQuestions.initializeQuiz(Constants.DEFAULT_QUESTION_AMOUNT);
            }

            //Make a PopupStage because the test window shares their qualities.
            StageHolder.setPrimaryStage(FXHelper.getSecureStage(FXHelper.Window.QUIZ));

        } catch (Exception e) {
            new UserNotifier("A page failed to load").display();
            e.printStackTrace();
        }

    }


    //Ends the entire test and shows the results page
    static void endQuiz() {
        try {

            StageHolder.setPrimaryStage(FXHelper.getSecureStage(FXHelper.Window.PRINT_RESULTS));

        } catch (Exception e) {
            new UserNotifier("A page failed to load").display();
            e.printStackTrace();
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
        SHOWANSWERS,
        TIME;

        //Hashmap of user preferences, initialize with default preferences
        private static final HashMap<Preference, String> preferences = new HashMap<>();

        public static void initializePreferences(JSONObject jsonObject) throws JSONException {

            if (validateJSON(jsonObject)) {
                preferences.put(CALCULATOR, jsonObject.get("calculator").toString());
                preferences.put(SHOWANSWERS, jsonObject.get("answers").toString());
                preferences.put(DRAWINGPAD, jsonObject.get("drawingpad").toString());
                preferences.put(NOTEPAD, jsonObject.get("notepad").toString());
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

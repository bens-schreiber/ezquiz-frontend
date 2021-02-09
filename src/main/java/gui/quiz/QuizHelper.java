package gui.quiz;

import etc.Constants;
import gui.FXController;
import gui.etc.FXHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

/**
 * Starts and ends quizzes
 */
public class QuizHelper extends FXController {

    private QuizHelper() {
    }

    /**
     * @param randomQuestions if the test should be random questions. False otherwise.
     */
    public static void startQuiz(boolean randomQuestions) throws InterruptedException, IOException, JSONException {

        if (randomQuestions) {
            QuizQuestions.initializeQuiz(Constants.DEFAULT_QUESTION_AMOUNT);
        }

        //Make a PopupStage because the test window shares their qualities.
        FXController.setPrimaryStage(FXHelper.getSecureStage(FXHelper.Window.QUIZ));

    }


    //Ends the entire test and shows the results page
    static void endQuiz() throws IOException {

        FXController.setPrimaryStage(FXHelper.getSecureStage(FXHelper.Window.PRINT_RESULTS));
    }

    /**
     * Preferences for the quiz, determines what tools are displayed
     */
    public enum Preference {

        //todo: time
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

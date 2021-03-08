package gui.quiz;

import etc.Constants;
import gui.FXController;
import gui.FXHelper;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Starts and ends quizzes
 */
public class QuizHelper extends FXController {

    private QuizHelper() {
    }


    private static Preference preferences;


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
    public static void endQuiz() throws IOException {

        FXController.setPrimaryStage(FXHelper.getSecureStage(FXHelper.Window.PRINT_RESULTS));

    }

    /**
     * Load JSONObject containing preferences into QuizHelper.preferences
     */
    public static void initializePreferences(JSONObject jsonObject) throws JSONException {
        if (validateJSON(jsonObject)) {
            preferences = new Preference(
                    Boolean.parseBoolean(jsonObject.get("calculator").toString()),
                    Boolean.parseBoolean(jsonObject.get("answers").toString()),
                    Boolean.parseBoolean(jsonObject.get("drawingpad").toString()),
                    Boolean.parseBoolean(jsonObject.get("notepad").toString()),
                    1800
            );
        }
    }

    //Validate all required json is there.
    private static boolean validateJSON(JSONObject jsonObject) {
        return jsonObject.has("answers") && jsonObject.has("calculator") && jsonObject.has("notepad")
                && jsonObject.has("drawingpad");
    }

    public static Preference getPreferences() {
        return preferences;
    }

    /**
     * Preferences for the quiz, determines what tools are displayed
     */
    public static class Preference {

        private final boolean calculator, showAnswers, drawingPad, notePad;
        private final int time;

        private Preference(boolean calculator, boolean showAnswers, boolean drawingPad, boolean notePad, int time) {
            this.calculator = calculator;
            this.showAnswers = showAnswers;
            this.drawingPad = drawingPad;
            this.notePad = notePad;
            this.time = time;
        }

        public boolean isCalculator() {
            return calculator;
        }

        public boolean isShowAnswers() {
            return showAnswers;
        }

        public boolean isDrawingPad() {
            return drawingPad;
        }

        public boolean isNotePad() {
            return notePad;
        }

        public int getTime() {
            return time;
        }
    }
}

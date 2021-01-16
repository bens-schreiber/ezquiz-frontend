package gui.quiz;

import java.util.HashMap;

/**
 * Preferences for the quiz
 */
public enum Preference {
    NOTEPAD, CALCULATOR, DRAWINGPAD, QUIZNAME, SHOWANSWERS, TIME;

    //Hashmap of user preferences, initialize default preferences
    public static HashMap<Preference, String> preferences = new HashMap<>();

    static {
        preferences.put(Preference.NOTEPAD, "true");
        preferences.put(Preference.CALCULATOR, "true");
        preferences.put(Preference.DRAWINGPAD, "true");
        preferences.put(Preference.QUIZNAME, "FBLA QUIZ - 5 Questions, Random");
        preferences.put(Preference.SHOWANSWERS, "true");
        preferences.put(Preference.TIME, "1800");
    }
}
package etc;

/**
 * Contains constants for use across the program
 */
public class Constants {

    public enum Window {
        STARTPAGE, PREMADEQUIZES, LOGIN, REGISTER, QUIZ, ENTERCODE,
        CALCULATOR, DRAWINGPAD, NOTEPAD, PRINTRESULTS, SEERESULTS,
        CUSTOMQUIZ
    }

    public static String USERNAME = "";

    public static int STATUS_ACCEPTED = 202;

    public static String DEFAULT_PATH = "http://localhost:7080/api/database/questions/";


    public static int DEFAULT_QUESTION_AMOUNT = 5;

    public static int DATABASE_SIZE = 50;



    public static int MAX_NOTEPAD_TEXT = 250;

    public static int MAX_PASS_USER_SIZE = 8;

    public static int MAX_BRUSH_SIZE = 1;


    public static String FLAGGED_COLOR = "#fb8804;";

    public static String BORDER_COLOR = "#303f9f;";

    public static String FLAGGED_COLOR_FX = "-fx-background-color: " + FLAGGED_COLOR;

    public static String SELECTED_COLOR_FX = "-fx-background-color: #56ea63;";

    public static String UNSELECTED_COLOR_FX = "-fx-background-color: #8797f1;";


}

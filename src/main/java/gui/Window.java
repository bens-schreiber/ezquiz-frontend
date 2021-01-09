package gui;

import java.util.HashMap;

//Window names, hashmap of window names and their corresponding fxml location
public enum Window {
    STARTPAGE, PREMADEQUIZES, LOGIN, REGISTER, QUIZ, ENTERCODE,
    CALCULATOR, DRAWINGPAD, NOTEPAD, PRINTRESULTS, SEERESULTS,
    CUSTOMQUIZ;

    public static HashMap<Window, String> fxmlTable = new HashMap<>();

    static {
        fxmlTable.put(Window.STARTPAGE, "/start.fxml");
        fxmlTable.put(Window.PREMADEQUIZES, "/premadequizes.fxml");
        fxmlTable.put(Window.LOGIN, "/login.fxml");
        fxmlTable.put(Window.REGISTER, "/register.fxml");
        fxmlTable.put(Window.QUIZ, "/quiz.fxml");
//        fxmlTable.put(Window.ENTERCODE, "/quiz.fxml");
        fxmlTable.put(Window.CALCULATOR, "/calculator.fxml");
        fxmlTable.put(Window.DRAWINGPAD, "/drawingpad.fxml");
        fxmlTable.put(Window.NOTEPAD, "/notepad.fxml");
        fxmlTable.put(Window.PRINTRESULTS, "/printableresults.fxml");
        fxmlTable.put(Window.SEERESULTS, "/questionresults.fxml");
        fxmlTable.put(Window.CUSTOMQUIZ, "/customquiz.fxml");
    }
}

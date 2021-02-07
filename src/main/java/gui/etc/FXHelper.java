package gui.etc;

import gui.PrimaryStageHolder;
import gui.popup.notification.UserNotifier;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class FXHelper {

    private FXHelper() {
    }

    /**
     * Opened instances of the Quiz.tools fx scenes.
     */
    private static final Set<Window> openedInstances = new HashSet<>();

    public static Set<Window> getOpenedInstances() {
        return openedInstances;
    }

    //Get a Scene from Window
    public static Scene getScene(Window window) throws IOException {
        return new Scene(FXMLLoader.load(FXHelper.class.getResource(window.getPath())));
    }

    //Get a Tab from a Window.
    public static Tab getTab(Window window) throws IOException {
        return FXMLLoader.load(FXHelper.class.getResource(window.getPath()));
    }

    public static Pane getPane(Window window) throws IOException {
        return FXMLLoader.load(FXHelper.class.getResource(window.getPath()));
    }

    /**
     * @param requireResponse if the stage should take priority out of all others.
     * @return Stage with values to make a popup.
     */
    //Get a stage from a window.
    public static Stage getPopupStage(Window window, boolean requireResponse) throws IOException {

        //Set stage, take ownership from primary stage so it stays on the same window
        Stage stage = new Stage();
        stage.initOwner(PrimaryStageHolder.getPrimaryStage());

        //Make stage required to be answered if needed
        if (requireResponse) {
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
        }
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);

        Scene scene = new Scene(FXMLLoader.load(UserNotifier.class.getResource(window.getPath())));
        stage.setScene(scene);

        return stage;

    }


    //FXML Window names and locations.
    public enum Window {
        MAIN_MENU("/fxml/mainmenu/mainmenu.fxml"),
        QUIZ_UPLOAD("/fxml/mainmenu/quizupload.fxml"),
        LOGIN("/fxml/mainmenu/login.fxml"),
        REGISTER("/fxml/mainmenu/register.fxml"),
        QUIZ("/fxml/quiz/quiz.fxml"),
        QUIZZES_MENU("/fxml/mainmenu/quizzesmenu.fxml"),
        ENTER_KEY("/fxml/popup/enterkey.fxml"),
        CALCULATOR("/fxml/quiz/tools/calculator.fxml"),
        DRAWING_PAD("/fxml/quiz/tools/drawingpad.fxml"),
        NOTEPAD("/fxml/quiz/tools/notepad.fxml"),
        PRINT_RESULTS("/fxml/results/printresults.fxml"),
        QUESTION_RESULTS("/fxml/results/questionresults.fxml"),
        ERROR("/fxml/popup/error.fxml"),
        CONFIRM("/fxml/popup/confirm.fxml"),
        ADMIN_MENU("/fxml/mainmenu/adminmenu.fxml"),
        QUIZ_BUILDER("/fxml/mainmenu/quizbuilder/quizbuildertool.fxml"),
        QUESTION_TAB("/fxml/mainmenu/quizbuilder/questiondisplaytab.fxml");

        private final String path;

        Window(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }
}

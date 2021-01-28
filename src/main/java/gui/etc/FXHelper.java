package gui.etc;

import gui.PrimaryStageHolder;
import gui.popup.notification.UserNotifier;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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

    //Get a Pane from a Window.
    public static Pane getPane(Window window) throws IOException {
        return FXMLLoader.load(FXHelper.class.getResource(window.getPath()));
    }

    /**
     * @param requireResponse if the stage should take priority out of all others.
     * @return Stage with unique values to make for a popup.
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
        MAINMENU("/fxml/mainmenu/mainmenu.fxml"),
        LOGIN("/fxml/mainmenu/login.fxml"),
        REGISTER("/fxml/mainmenu/register.fxml"),
        QUIZ("/fxml/quiz/quiz.fxml"),
        SAVEDQUIZZES("/fxml/mainmenu/quizzesmenu.fxml"),
        ENTERKEY("/fxml/popup/enterkey.fxml"),
        CALCULATOR("/fxml/quiz/tools/calculator.fxml"),
        DRAWINGPAD("/fxml/quiz/tools/drawingpad.fxml"),
        NOTEPAD("/fxml/quiz/tools/notepad.fxml"),
        PRINTRESULTS("/fxml/results/printableresults.fxml"),
        SEERESULTS("/fxml/results/questionresults.fxml"),
        ERROR("/fxml/popup/errorscreen.fxml"),
        CONFIRM("/fxml/popup/confirmscreen.fxml"),
        ADMINMENU("/fxml/mainmenu/adminmenu.fxml");

        private final String path;

        Window(String path) {
            this.path = path;
        }

        public String getPath() {
            return path;
        }
    }
}

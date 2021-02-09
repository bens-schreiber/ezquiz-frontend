package gui.etc;

import gui.FXController;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class FXHelper {

    private FXHelper() {
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
        stage.initOwner(FXController.getPrimaryStage());

        //Make stage required to be answered if needed
        if (requireResponse) {
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
        }
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);

        Scene scene = getScene(window);
        stage.setScene(scene);

        return stage;

    }

    public static Stage getSecureStage(Window window) throws IOException {

        Stage stage = new Stage();

        //Secure the stage
        stage.setAlwaysOnTop(true);
        stage.setFullScreen(true);
        stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
        stage.setFullScreenExitHint(null);
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        stage.setHeight(visualBounds.getHeight());
        stage.setWidth(visualBounds.getWidth());
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);

        stage.setScene(getScene(window));
        return stage;
    }


    //FXML Window names and locations.
    public enum Window {
        MAIN_MENU("/fxml/mainmenu/mainmenu.fxml"),
        QUIZ_UPLOAD("/fxml/mainmenu/quizupload.fxml"),
        LOGIN("/fxml/login/login.fxml"),
        REGISTER("/fxml/login/register.fxml"),
        QUIZ("/fxml/quiz/quiz.fxml"),
        QUIZZES_MENU("/fxml/mainmenu/quizzesmenu.fxml"),
        ENTER_KEY("/fxml/popup/enterkey.fxml"),
        CALCULATOR("/fxml/quiz/tools/calculator.fxml"),
        DRAWING_PAD("/fxml/quiz/tools/drawingpad.fxml"),
        NOTEPAD("/fxml/quiz/tools/notepad.fxml"),
        PRINT_RESULTS("/fxml/results/printresults.fxml"),
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

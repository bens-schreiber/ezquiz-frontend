package gui.controllers.startpage;

import etc.Constants;
import gui.StageHelper;
import gui.popups.ErrorBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

class StartQuiz {
    void displayTest() {

        try {

            //Attempt to load scene and set it to stage
            Stage stage = StageHelper.createAndAddStage(Constants.Window.QUIZ, "/quiz.fxml");
            stage.setAlwaysOnTop(true);
            stage.initStyle(StageStyle.UNDECORATED);

            //Close stage helper resources
            StageHelper.clearScenes();
            StageHelper.closeStage(Constants.Window.STARTPAGE);

            //Display Quiz
            stage.show();

        } catch (IOException | NullPointerException e) {
            ErrorBox.display("A page failed to load.", false);
            e.printStackTrace();
        }
    }
}

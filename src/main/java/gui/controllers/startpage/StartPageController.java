package gui.controllers.startpage;

import gui.StageHelper;
import gui.popups.ErrorBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

class StartPageController {
    void displayTest() {

        try {

            //Attempt to load scene and set it to stage
            Stage stage = StageHelper.createAndAddStage("Quiz", "/quiz.fxml");
            stage.setAlwaysOnTop(true);
            stage.initStyle(StageStyle.UNDECORATED);

            //Close stage helper resources
            StageHelper.clearScenes();
            StageHelper.closeStage("StartupPage");

            //Display Quiz
            stage.show();

        } catch (IOException | NullPointerException e) {
            ErrorBox.display("A page failed to load.", false);
            e.printStackTrace();
        }
    }
}

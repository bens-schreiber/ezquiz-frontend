package gui.controllers.startpage;

import gui.StageHelper;
import gui.popups.ErrorBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import quiz.QuizManager;

import java.io.IOException;

public class OtherQuizesController {

    public void onNetworkDesign() {

        QuizManager.loadQuestions(10, "Network%20Design", null);

        StageHelper.closeAllStages();
        StageHelper.clearScenes();

        displayTest(); //Initialize the test.
    }

    public void onIntroBus() {
        QuizManager.loadQuestions(10, "Intro%20To%20Business", null);

        StageHelper.closeStage("StartupPage");
        StageHelper.clearScenes();

        displayTest(); //Initialize the test.
    }

    public void onBusMath() {
        QuizManager.loadQuestions(10, "Business%20Math", null);

        StageHelper.closeStage("StartupPage");
        StageHelper.clearScenes();

        displayTest(); //Initialize the test.
    }

    public void onMarketing() {
        QuizManager.loadQuestions(10, "Marketing", null);

        StageHelper.closeStage("StartupPage");
        StageHelper.clearScenes();

        displayTest(); //Initialize the test.
    }

    public void onCustom() {

        //Close this scene
        StageHelper.getStages().get("otherquizes").close();

        //Save start page
        StageHelper.addScene("Start", StageHelper.getStages().get("StartupPage").getScene());

        //Try to not reload scene if scene its already been loaded.
        if (StageHelper.getScenes().containsKey("custom")) {


            StageHelper.getStages().get("StartupPage").setScene(StageHelper.getScenes().get("custom"));

        } else {

            try {

                //Attempt to load in customquiz options scene
                Parent root = FXMLLoader.load(getClass().getResource("/customquiz.fxml"));
                Scene scene = new Scene(root);

                //Add scene to StageHelper so it can be used again
                StageHelper.addScene("custom", scene);

                //Display this scene
                StageHelper.getStages().get("StartupPage").setScene(scene);

            } catch (IOException | NullPointerException e) {
                ErrorBox.display("A page failed to load.", false);
                e.printStackTrace();
            }

        }
    }

    private void displayTest() {

        try {

            Stage stage = StageHelper.createAndAddStage("Quiz", "/quiz.fxml");

            stage.setAlwaysOnTop(true);
            stage.initStyle(StageStyle.UNDECORATED);

            StageHelper.closeAllStages();
            StageHelper.clearScenes();

            stage.show();

        } catch (IOException | NullPointerException e) {
            ErrorBox.display("A page failed to load.", false);
            e.printStackTrace();
        }
    }

}

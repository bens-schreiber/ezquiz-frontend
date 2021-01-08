package gui.controllers.startpage;

import gui.StageHelper;
import gui.popups.ErrorBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import quiz.QuizManager;
import quiz.questions.Question;

import java.io.IOException;

public class OtherQuizesController extends StartPage {

    public void onNetworkDesign() {

        QuizManager.loadQuestions(10, null, Question.Subject.NETWORKDESIGN);

        StageHelper.closeAllStages();
        StageHelper.clearScenes();

        displayTest(); //Initialize the test.
    }

    public void onIntroBus() {
        QuizManager.loadQuestions(10, null, Question.Subject.INTBUS);

        StageHelper.closeStage("StartupPage");
        StageHelper.clearScenes();

        displayTest(); //Initialize the test.
    }

    public void onBusMath() {
        QuizManager.loadQuestions(10, null, Question.Subject.BUSMATH);

        StageHelper.closeStage("StartupPage");
        StageHelper.clearScenes();

        displayTest(); //Initialize the test.
    }

    public void onMarketing() {
        QuizManager.loadQuestions(10, null, Question.Subject.MARKETING);

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

}

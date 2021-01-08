package gui.controllers.startpage;

import etc.Constants;
import gui.StageHelper;
import gui.popups.ErrorBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import quiz.QuizManager;
import quiz.questions.Question;

import java.io.IOException;

//todo: make dis work :(

public class PremadeQuizSelect extends StartQuiz {

    public void onNetworkDesign() {

        QuizManager.loadQuestions(10, null, Question.Subject.NETWORKDESIGN);

        StageHelper.closeAllStages();
        StageHelper.clearScenes();

        displayTest(); //Initialize the test.
    }

    public void onIntroBus() {
        QuizManager.loadQuestions(10, null, Question.Subject.INTBUS);

        StageHelper.closeStage(Constants.Window.STARTPAGE);
        StageHelper.clearScenes();

        displayTest(); //Initialize the test.
    }

    public void onBusMath() {
        QuizManager.loadQuestions(10, null, Question.Subject.BUSMATH);

        StageHelper.closeStage(Constants.Window.STARTPAGE);
        StageHelper.clearScenes();

        displayTest(); //Initialize the test.
    }

    public void onMarketing() {
        QuizManager.loadQuestions(10, null, Question.Subject.MARKETING);

        StageHelper.closeStage(Constants.Window.STARTPAGE);
        StageHelper.clearScenes();

        displayTest(); //Initialize the test.
    }

    public void onCustom() {

        //Close this scene
        StageHelper.getStages().get(Constants.Window.PREMADEQUIZES).close();

        //Save start page
        StageHelper.addScene(Constants.Window.STARTPAGE, StageHelper.getStages().get(Constants.Window.STARTPAGE).getScene());

        //Try to not reload scene if scene its already been loaded.
        if (StageHelper.getScenes().containsKey(Constants.Window.CUSTOMQUIZ)) {

            StageHelper.getStages().get(Constants.Window.STARTPAGE).setScene(StageHelper.getScenes().get(Constants.Window.CUSTOMQUIZ));

        } else {

            try {

                //Attempt to load in customquiz options scene
                Parent root = FXMLLoader.load(getClass().getResource("/customquiz.fxml"));
                Scene scene = new Scene(root);

                //Add scene to StageHelper so it can be used again
                StageHelper.addScene(Constants.Window.CUSTOMQUIZ, scene);

                //Display this scene
                StageHelper.getStages().get(Constants.Window.STARTPAGE).setScene(scene);

            } catch (IOException | NullPointerException e) {
                ErrorBox.display("A page failed to load.", false);
                e.printStackTrace();
            }

        }
    }

}

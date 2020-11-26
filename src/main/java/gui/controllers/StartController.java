package gui.controllers;

import etc.Constants;
import gui.GuiHelper;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONException;
import quiz.QuizManager;

import java.io.IOException;

public class StartController {

    /**
     * Start screen button controller
     */

    public void onDefaultButton() throws IOException, JSONException {

        QuizManager.loadQuestions(Constants.DEFAULT_QUESTION_AMOUNT, null, null); //Load default quiz.

        Stage stage = new Stage();
        stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/quiz.fxml"))));

        stage.setAlwaysOnTop(true);
        stage.initStyle(StageStyle.UNDECORATED);

        GuiHelper.addWindow("Quiz", stage);
        GuiHelper.closeWindow("Start");
        stage.show();


    }

    public void onCustomButton() throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/customquiz.fxml"));
        Scene scene = new Scene(root);

        GuiHelper.getOpenedWindows().get("Start").setScene(scene);

    }

    public void onExitButton() {
        Platform.exit();
    }

}

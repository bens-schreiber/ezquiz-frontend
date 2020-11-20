package gui.controllers;

import etc.Constants;
import gui.GuiHelper;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONException;
import quiz.QuizManager;

import java.awt.event.ActionEvent;
import java.io.IOException;

public class StartController {

    /**
     * Start screen button controller
     */

    public void onDefaultButton(MouseEvent mouseEvent) throws IOException, JSONException {

        QuizManager.loadQuestions(Constants.defaultQuestionAmount, null, null); //Load default test.

        GuiHelper.closeWindow("Start");

        displayTest();
    }

    public void onCustomButton(MouseEvent mouseEvent) throws IOException {

        GuiHelper.closeWindow("Start");

        displayCustomOptions();
    }

    public void onExitButton() {
        Platform.exit();
    }

    private void displayTest() throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/test.fxml"));

        Scene scene = new Scene(root);

        Stage stage = new Stage();

        stage.setAlwaysOnTop(true);

        stage.initStyle(StageStyle.UNDECORATED);

        stage.setScene(scene);

        stage.show();

        GuiHelper.addWindow("Quiz", stage);
    }

    private void displayCustomOptions() throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/custom.fxml"));

        Scene scene = new Scene(root);

        Stage stage = new Stage();

        stage.setScene(scene);

        stage.show();

        GuiHelper.addWindow("Custom", stage);
    }

}

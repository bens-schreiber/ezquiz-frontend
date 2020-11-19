package gui.controllers;

import etc.Constants;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONException;
import quiz.QuizManager;

import java.io.IOException;

public class StartController implements Exitable {

    public void onDefaultButton(MouseEvent mouseEvent) throws IOException, JSONException {

        QuizManager.loadQuestions(Constants.defaultQuestionAmount, null, null); //Load default test.

        exit(mouseEvent);

        displayTest();
    }

    public void onCustomButton(MouseEvent mouseEvent) throws IOException {

        exit(mouseEvent);

        displayCustomOptions();
    }

    private void displayTest() throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/test.fxml"));

        Scene scene = new Scene(root);

        Stage stage = new Stage();

        stage.setAlwaysOnTop(true);

        stage.initStyle(StageStyle.UNDECORATED);

        stage.setScene(scene);

        stage.show();
    }

    private void displayCustomOptions() throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/custom.fxml"));

        Scene scene = new Scene(root);

        Stage stage = new Stage();

        stage.setScene(scene);

        stage.show();
    }

}

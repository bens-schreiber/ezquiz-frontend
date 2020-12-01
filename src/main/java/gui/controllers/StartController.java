package gui.controllers;

import etc.Constants;
import gui.GuiHelper;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONException;
import quiz.QuizManager;

import java.io.IOException;
import java.util.*;

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

    public void onEnterCode() {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Custom Quiz");
        window.setMinWidth(250);
        Label label = new Label("Enter the quiz code");

        BorderPane layout = new BorderPane();
        layout.setTop(label);

        TextField textField = new TextField();

        Button startButton = new Button("Start");
        startButton.setOnAction(event -> {

            ArrayList<Integer> ids = new ArrayList<>();
            long bMap = Long.parseLong(new String(Base64.getDecoder().decode(textField.getText())));

            for (int i = 63; i >= 0; --i) {
                if ((bMap < 0 && i == 63) || (bMap & 1L << i) > 0) {
                    ids.add(i + 1);
                }
            }

            QuizManager.loadQuestions(ids);

            Stage stage = new Stage();
            try {
                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/quiz.fxml"))));
                stage.setAlwaysOnTop(true);
                stage.initStyle(StageStyle.UNDECORATED);

                GuiHelper.addWindow("Quiz", stage);
                GuiHelper.closeWindow("Start");
                stage.show();
                window.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        Button exitButton = new Button("Exit");
        exitButton.setOnAction(event -> window.close());

        HBox hbox = new HBox(5);
        hbox.getChildren().add(startButton);
        hbox.getChildren().add(exitButton);

        VBox vBox = new VBox(15);
        vBox.getChildren().add(textField);
        vBox.getChildren().add(hbox);

        layout.setCenter(vBox);

        Scene scene = new Scene(layout);
        window.initStyle(StageStyle.UNDECORATED);
        window.setScene(scene);
        window.showAndWait();

    }


    public void onExitButton() {
        Platform.exit();
    }

}

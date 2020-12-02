package gui.controllers.startup;

import etc.Constants;
import gui.StageHelper;
import gui.popups.ErrorBox;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
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
import quiz.QuizManager;

import java.io.IOException;
import java.util.*;

/**
 * Provides methods for ActionEvents on Startup Screen.
 */

public class StartController {


    public void onDefaultButton() {

        try {
            QuizManager.loadQuestions(Constants.DEFAULT_QUESTION_AMOUNT, null, null); //Load default quiz.

            Stage stage = new Stage();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/quiz.fxml"))));

            stage.setAlwaysOnTop(true);
            stage.initStyle(StageStyle.UNDECORATED);

            StageHelper.addWindow("Quiz", stage);
            StageHelper.closeWindow("Start");
            stage.show();
        } catch (IOException | NullPointerException e) {
            ErrorBox.display("A page failed to load.", false);
            e.printStackTrace();
        }


    }

    public void onCustomButton() {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/customquiz.fxml"));
            Scene scene = new Scene(root);
            StageHelper.getOpenedWindows().get("Start").setScene(scene);
        } catch (IOException | NullPointerException e) {
            ErrorBox.display("A page failed to load.", false);
            e.printStackTrace();
        }

    }

    /**
     * On Enter Code button clicked, decodes question id's from bitmap, creates Quiz
     */

    public void onEnterCode() {

        //Set stage style
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Custom Quiz");
        window.setMinWidth(250);

        //Add directions and borderpane box to put nodes in
        BorderPane layout = new BorderPane();
        Label label = new Label("Enter the quiz code");
        layout.setTop(label);

        //Text field to enter quiz code
        TextField textField = new TextField();

        //Start button to begin test
        Button startButton = new Button("Start");

        //On start button clicked
        startButton.setOnAction(event -> {

            //Get ID's from Base64 bitmap
            ArrayList<Integer> ids = new ArrayList<>();
            long bMap = Long.parseLong(new String(Base64.getDecoder().decode(textField.getText())));

            for (int i = 63; i >= 0; --i) {
                if ((bMap < 0 && i == 63) || (bMap & 1L << i) > 0) {
                    ids.add(i + 1);
                }
            }

            //Load ID's into the QuizManager
            QuizManager.loadQuestions(ids);

            //Start test
            try {
                Stage stage = new Stage();
                stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/quiz.fxml"))));
                stage.setAlwaysOnTop(true);
                stage.initStyle(StageStyle.UNDECORATED);

                StageHelper.addWindow("Quiz", stage);
                StageHelper.closeWindow("Start");
                stage.show();
                window.close();
            } catch (IOException | NullPointerException e) {
                ErrorBox.display("A page failed to load.", false);
                e.printStackTrace();
            }
        });

        //Add exit button for leaving code page
        Button exitButton = new Button("Exit");
        exitButton.setOnAction(event -> window.close());

        //Add buttons to horizontal node
        HBox hbox = new HBox(5);
        hbox.getChildren().add(startButton);
        hbox.getChildren().add(exitButton);

        //add everything to vertical node
        VBox vBox = new VBox(15);
        vBox.getChildren().add(textField);
        vBox.getChildren().add(hbox);

        //Make vertical node centered
        layout.setCenter(vBox);

        //Display stage
        Scene scene = new Scene(layout);
        window.initStyle(StageStyle.UNDECORATED);
        window.setScene(scene);
        window.showAndWait();

    }


    public void onExitButton() {
        Platform.exit();
    }

}

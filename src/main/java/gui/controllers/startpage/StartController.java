package gui.controllers.startpage;

import etc.BitMap;
import etc.Constants;
import gui.StageHelper;
import gui.popups.ErrorBox;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import quiz.QuizManager;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Provides methods for ActionEvents on Startup Screen.
 */
public class StartController implements Initializable {

    @FXML
    private VBox buttonBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

//        buttonBox.getChildren().forEach(button -> button.setDisable(true));
//        buttonBox.getChildren().get(0).setDisable(false);


    }

    public void onLoginButton() {

        try {

            Stage stage = StageHelper.createAndAddStage("Login", "/login.fxml");
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setAlwaysOnTop(true);
            stage.show();

        } catch (IOException | NullPointerException e) {
            ErrorBox.display("A page failed to load.", false);
            e.printStackTrace();
        }


    }

    public void onDefaultButton() {
        try {
            //Load in questions with no null limiters
            QuizManager.loadQuestions(Constants.DEFAULT_QUESTION_AMOUNT, null, null); //Load default quiz.

            //Attempt to load scene and set it to stage
            Stage stage = StageHelper.createAndAddStage("Quiz", "/quiz.fxml");
            stage.setAlwaysOnTop(true);
            stage.initStyle(StageStyle.UNDECORATED);

            //Close stage helper resources
            StageHelper.clearScenes();
            StageHelper.closeStage("Start");

            //Display Quiz
            stage.show();

        } catch (IOException | NullPointerException e) {
            ErrorBox.display("A page failed to load.", false);
            e.printStackTrace();
        }


    }

    public void onCustomButton() {

        //Try to not reload scene if scene its already been loaded.
        if (StageHelper.getScenes().containsKey("custom")) {

            StageHelper.getStages().get("Start").setScene(StageHelper.getScenes().get("custom"));

        } else {
            try {

                //Attempt to load in customquiz options scene
                Parent root = FXMLLoader.load(getClass().getResource("/customquiz.fxml"));
                Scene scene = new Scene(root);

                //Add scene to StageHelper so it can be used again
                StageHelper.addScene("custom", scene);

                //Display this scene
                StageHelper.getStages().get("Start").setScene(scene);

            } catch (IOException | NullPointerException e) {
                ErrorBox.display("A page failed to load.", false);
                e.printStackTrace();
            }

        }
    }

    /**
     * On Enter Code button clicked, decodes question id's from bitmap, creates Quiz
     */

    public void onEnterCode() {
        //todo: make this an fxml scene
        //Try to not reload scene if scene already exists.
        if (StageHelper.getStages().containsKey("code prompt")) {
            StageHelper.getStages().get("code prompt").showAndWait();
        } else {

            //Establish window
            Stage window = new Stage();

            //Set stage style
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
                try {
                    ArrayList<Integer> ids = new BitMap(textField.getText()).decodeToArray();
                    QuizManager.loadQuestions(ids);

                    //Start test
                    try {

                        Stage stage = StageHelper.createAndAddStage("Quiz", "/quiz.fxml");
                        stage.setAlwaysOnTop(true);
                        stage.initStyle(StageStyle.UNDECORATED);

                        //Close resources that arent needed anymore
                        StageHelper.closeAllStages();
                        StageHelper.clearScenes();

                        //Add new window
                        stage.show();
                        window.close();

                    } catch (IOException | NullPointerException e) {
                        ErrorBox.display("A page failed to load.", false);
                        e.printStackTrace();
                    }

                } catch (IllegalArgumentException e) {
                    ErrorBox.display("Illegal characters.", false);
                } finally {
                    ErrorBox.display("Error attempting to use code.", false);
                }

            });


            //Add exit button for leaving code page
            Button exitButton = new Button("Exit");
            exitButton.setOnAction(event -> window.close());

            //Add buttons to horizontal node
            HBox hbox = new HBox(5);
            hbox.getChildren().add(startButton);
            hbox.getChildren().add(exitButton);
            hbox.setAlignment(Pos.CENTER);

            //add everything to vertical node
            VBox vBox = new VBox(15);
            vBox.getChildren().add(textField);
            vBox.getChildren().add(hbox);

            //Make vertical node centered
            layout.setCenter(vBox);

            //Display stage
            Scene scene = new Scene(layout);
            scene.getStylesheets().add("css/standard_button.css");
            window.initStyle(StageStyle.UNDECORATED);
            window.setScene(scene);
            window.showAndWait();

            //Put code prompt in stage helper so it does not need to be reloaded
            StageHelper.addStage("code prompt", window);

        }
    }


    public void onExitButton() {
        Platform.exit();
    }

}

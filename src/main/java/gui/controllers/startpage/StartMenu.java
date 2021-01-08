package gui.controllers.startpage;

import database.DatabaseRequest;
import database.QuestionRequest;
import etc.BitMap;
import etc.Constants;
import gui.StageHelper;
import gui.popups.ErrorBox;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONException;
import quiz.QuizManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

/**
 * Provides methods for ActionEvents on Startup Screen.
 */
public class StartMenu extends StartQuiz implements Initializable {

    @FXML
    private VBox buttonBox;

    @FXML
    Label loggedInLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Disable all buttons until the user has logged in.
        buttonBox.getChildren().forEach(button -> button.setDisable(true));
        buttonBox.getChildren().get(0).setDisable(false);
        buttonBox.getChildren().get(1).setDisable(false);


    }

    public void onRegisterButton() {

        try {
            Stage stage = StageHelper.createAndAddStage(Constants.Window.REGISTER, "/register.fxml");
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setAlwaysOnTop(true);
            stage.show();

        } catch (Exception e) {
            ErrorBox.display("A page failed to load.", false);
            e.printStackTrace();
        }

    }

    public void onLoginButton() {

        try {

            Stage stage = StageHelper.createAndAddStage(Constants.Window.LOGIN, "/login.fxml");
            stage.initStyle(StageStyle.UNDECORATED);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setAlwaysOnTop(true);
            stage.showAndWait();

            //If an AUTH token has been received, the user is logged in. Enable all buttons.
            if (!QuestionRequest.AUTH_TOKEN.isEmpty()) {
                buttonBox.getChildren().forEach(button -> button.setDisable(!button.isDisable()));
                buttonBox.getChildren().remove(0);
                buttonBox.getChildren().remove(0);
                loggedInLabel.setText(Constants.USERNAME);
            }

        } catch (Exception e) {
            ErrorBox.display("A page failed to load.", false);
            e.printStackTrace();
        }


    }

    public void onDefaultButton() {

        //Load in questions with no limiters
        QuizManager.loadQuestions(Constants.DEFAULT_QUESTION_AMOUNT, null, null); //Load default quiz.

        displayTest();
    }

    public void onOtherButton() {

        try {

            Stage stage = StageHelper.createAndAddStage(Constants.Window.PREMADEQUIZES, "/premadequizes.fxml");
            stage.setAlwaysOnTop(true);
            stage.initStyle(StageStyle.UNDECORATED);

            stage.show();

        } catch (Exception e) {
            ErrorBox.display("A page failed to load.", false);
            e.printStackTrace();
        }

    }


    /**
     * On Enter Code button clicked, decodes question id's from bitmap, creates Quiz
     */

    public void onEnterCode() {
        //todo: make this an fxml scene
        //Try to not reload scene if scene already exists.
        if (StageHelper.getStages().containsKey(Constants.Window.ENTERCODE)) {
            StageHelper.getStages().get(Constants.Window.ENTERCODE).showAndWait();
        } else {

            //Establish window
            Stage window = new Stage();

            //Set stage style
            window.initModality(Modality.APPLICATION_MODAL);
            window.setMinWidth(250);

            //Add directions and borderpane box to put nodes in
            BorderPane layout = new BorderPane();
            Label label = new Label("Enter the quiz code");
            layout.setTop(label);

            //Text field to enter quiz code
            TextField textField = new TextField();

            UnaryOperator<TextFormatter.Change> modifyChange = c -> {
                if (c.isContentChange()) {
                    int newLength = c.getControlNewText().length();
                    if (newLength > 4) {
                        c.setText("");
                    }
                }
                return c;
            };

            textField.setTextFormatter(new TextFormatter<>(modifyChange));

            //Start button to begin test
            Button startButton = new Button("Start");

            //On start button clicked
            startButton.setOnAction(event -> {

                //Get ID's from Base64 bitmap
                try {

                    long bitmap = DatabaseRequest.getTestKey(Integer.parseInt(textField.getText()));
                    QuizManager.loadQuestions(new BitMap(bitmap).decodeToList());

                    //Start test
                    try {

                        //Attempt to load scene and set it to stage
                        Stage stage = StageHelper.createAndAddStage(Constants.Window.QUIZ, "/quiz.fxml");
                        stage.setAlwaysOnTop(true);
                        stage.initStyle(StageStyle.UNDECORATED);

                        //Close stage helper resources
                        StageHelper.clearScenes();
                        StageHelper.closeStage(Constants.Window.STARTPAGE);

                        //Display Quiz
                        window.close();
                        stage.show();

                    } catch (IOException | NullPointerException e) {
                        ErrorBox.display("A page failed to load.", false);
                        e.printStackTrace();
                    }
                } catch (NumberFormatException | InterruptedException | IOException | JSONException e) {
                    ErrorBox.display("Could not get test from code.", false);
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
            StageHelper.addStage(Constants.Window.ENTERCODE, window);

        }
    }


    public void onExitButton() {
        Platform.exit();
    }

}

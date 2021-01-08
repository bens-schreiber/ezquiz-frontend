package gui.controllers.quiz;

import etc.Constants;
import gui.StageHelper;
import gui.popups.ConfirmBox;
import gui.popups.ErrorBox;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import quiz.QuizManager;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * Main controller for test.
 */
public class QuizController implements Initializable {

    @FXML
    Button backButton, nextButton, notePadButton, calculatorButton, drawingPadButton;

    @FXML
    Label questionPrompt, questionDirections, quizName, quizTimer, currQuestionLabel, userLabel, subjAndQuestion;

    @FXML
    VBox questionArea;

    @FXML
    AnchorPane questionPane;

    @FXML
    HBox questionHBox;

    //Drawing pad canvas is apart of test stage, utilized by DrawingPadController
    @FXML
    Canvas paintCanvas;

    private static GraphicsContext gc;

    //Default test is 30 minutes
    private Integer seconds = 1800;


    /**
     * Initial run method
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Establish preferences
        if (QuizManager.getPreferences().isEmpty()) {
            //Calculator, notepad and drawing pad are enabled by default

            //Set default quiz name.
            quizName.setText("FBLA - Default 5 Question Quiz");
            QuizManager.getPreferences().put("Quiz Name", quizName.getText());

            //Show correct answers in results by default
            QuizManager.getPreferences().put("Show Correct Answers", "true");

        } else {

            //Get preferences and apply them, if any.
            notePadButton.setVisible(Boolean.parseBoolean(QuizManager.getPreferences().get("Notepad")));

            calculatorButton.setVisible(Boolean.parseBoolean(QuizManager.getPreferences().get("Calculator")));

            drawingPadButton.setVisible(Boolean.parseBoolean(QuizManager.getPreferences().get("Drawing Pad")));

            quizName.setText(QuizManager.getPreferences().get("Quiz Name"));

            seconds = Integer.parseInt(QuizManager.getPreferences().get("seconds"));

        }


        //Establish flag button amount
        //Whenever the button is clicked use the individualQuestionClicked handler
        for (int i = 0; i < QuizManager.getQuizNodes().length; i++) {

            FlagButton button = new FlagButton();

            button.setOnMouseClicked(this::individualQuestionClicked);

            questionHBox.getChildren().add(button);

        }


        //Display the new question.
        displayNewQuestion();

        //Color the question that is currently selected
        selectCurrentQuizButton();

        //Begin the test timer
        startTimer();

        //Disable back button by default.
        backButton.setDisable(true);

        userLabel.setText(userLabel.getText() + Constants.USERNAME);

        //Establish canvas properties
        paintCanvas.setDisable(true);
        gc = paintCanvas.getGraphicsContext2D();
        gc.setStroke(Color.WHITE);

    }

    /**
     * Timer
     */
    private void startTimer() {

        Timeline time = new Timeline();

        time.setCycleCount(Timeline.INDEFINITE);

        KeyFrame frame = new KeyFrame(Duration.seconds(1), event -> {

            if (seconds == -1) {

                time.stop();

                endTest();


            } else {

                int hours;
                int minutes;
                int second;
                hours = seconds / 3600;
                minutes = (seconds % 3600) / 60;
                second = seconds % 60;

                //Format in hours:minutes:seconds
                quizTimer.setText(String.format("%02d:%02d:%02d", hours, minutes, second));
            }

            seconds--;

        });

        time.getKeyFrames().add(frame);
        time.playFromStart();
    }


    /**
     * Methods for Button Actions
     */

    //When next button is clicked
    public void onNextButton() {

        if (QuizManager.getCurrNum() < QuizManager.getQuizNodes().length - 1) {

            QuizManager.nextQuestion(); //Change to the next question.

            displayNewQuestion(); //Display the new question.
        }

        //Disable/enable next and back based on position
        backButton.setDisable(QuizManager.getCurrNum() == 0);
        nextButton.setDisable(QuizManager.getCurrNum() + 1 == QuizManager.getQuizNodes().length);

        //Color the question button that is currently selected
        selectCurrentQuizButton();

    }

    //When back button is clicked
    public void onBackButton() {

        if (QuizManager.getCurrNum() > 0) {
            QuizManager.prevQuestion(); //Goto previous question.
            displayNewQuestion(); //Load previous question.
        }

        //Disable/enable next and back based on position
        backButton.setDisable(QuizManager.getCurrNum() == 0);
        nextButton.setDisable(QuizManager.getCurrNum() + 1 == QuizManager.getQuizNodes().length);

        //Color the question button that is currently selected
        selectCurrentQuizButton();

    }

    //On an individual question clicked
    private void individualQuestionClicked(MouseEvent mouseEvent) {

        //Grab the spot of the question
        int questionSpot = questionHBox.getChildren().indexOf(mouseEvent.getSource());

        //Set current question to the spot
        QuizManager.setCurrNum(questionSpot);

        //Display the current question.
        displayNewQuestion();

        //Color the flag button that is currently selected if not flagged
        if (!((FlagButton) getCurrentButton()).isFlagged()) {
            selectCurrentQuizButton();
        }

        backButton.setDisable(questionSpot == 0);
        nextButton.setDisable(questionSpot + 1 == QuizManager.getQuizNodes().length);

    }

    //On Flag Question clicked
    public void onFlagQuestion() {

        //Un-flag if already flagged.
        if (((FlagButton) getCurrentButton()).isFlagged()) {

            ((FlagButton) getCurrentButton()).setFlagged(false);
            questionPrompt.setStyle("-fx-text-fill: black");

        } else {
            ((FlagButton) getCurrentButton()).setFlagged(true);
            questionPrompt.setStyle("-fx-text-fill: " + Constants.FLAGGED_COLOR);
        }
    }

    //When submit button is clicked
    public void onSubmitButton() {

        //If any questions are flagged
        if (questionHBox.getChildren().stream().anyMatch(node -> ((FlagButton) node).isFlagged())) {
            if (ConfirmBox.display("Some questions are flagged. Are you sure you want to submit?")) {
                endTest();
            }
        }

        //If all questions are answered.
        else if (QuizManager.allResponded()) {

            if (ConfirmBox.display("Are you sure you want to submit?")) {
                endTest();
            }

        }

        //If all questions aren't answered
        else if (ConfirmBox.display("Some answers are unfinished. Are sure you want to submit?")) {
            endTest();
        }

    }

    //When the calculator button is clicked
    public void onCalculatorButton() {

        //Make sure one calculator only is open.
        if (StageHelper.getStages().containsKey("calculator")) {

            //If already open, close all
            StageHelper.closeStage("calculator");

        } else {

            try {

                Stage stage = StageHelper.createAndAddStage("calculator", "/calculator.fxml");

                stage.setAlwaysOnTop(true); //Keep notepad on test.
                stage.initStyle(StageStyle.UTILITY);//Get rid of minimize
                stage.resizableProperty().setValue(false);

                stage.setOnCloseRequest(e -> StageHelper.closeStage("calculator"));


                stage.show();

            } catch (IOException | NullPointerException e) {
                ErrorBox.display("A page failed to load.", false);
                e.printStackTrace();
            }

        }
    }

    //When the notepad button is clicked
    public void onNotepadButton() {

        if (StageHelper.getStages().containsKey("notepad")) {

            //If already open, close all.
            StageHelper.closeStage("notepad");

        } else {//Make sure only one notepad is open.

            try {

                //Establish scene and stage
                Stage stage = StageHelper.createAndAddStage("notepad", "/notepad.fxml");

                stage.setAlwaysOnTop(true); //Keep notepad on test.
                stage.initStyle(StageStyle.UTILITY);//Get rid of minimize
                stage.resizableProperty().setValue(false); //Make non resizeable

                stage.setOnCloseRequest(e -> StageHelper.closeStage("notepad"));

                stage.show();

            } catch (IOException | NullPointerException e) {
                ErrorBox.display("A page failed to load.", false);
                e.printStackTrace();
            }

        }

    }

    //On drawing button clicked
    public void onDrawingPadButton() {

        //Make sure only one drawingpad is open
        if (StageHelper.getStages().containsKey("drawingpad")) {

            //If already open, close all.
            paintCanvas.setDisable(true);

            //Change cursor back to default
            StageHelper.getStages().get("Quiz").getScene().setCursor(Cursor.DEFAULT);

            gc.clearRect(0, 0, 1920, 1080);

            StageHelper.closeStage("drawingpad");

        } else {

            try {

                //Change cursor to crosshair to show drawing mode is on
                StageHelper.getStages().get("Quiz").getScene().setCursor(Cursor.CROSSHAIR);

                Stage stage = StageHelper.createAndAddStage("drawingpad", "/drawingpad.fxml");

                stage.setAlwaysOnTop(true); //Keep pad on test.
                stage.initStyle(StageStyle.UTILITY);
                stage.resizableProperty().setValue(false);

                //Make sure if X is pressed it removes from stages, clears drawings
                stage.setOnCloseRequest(e -> {

                    //Disable canvas so other parts can be used
                    paintCanvas.setDisable(true);

                    gc.clearRect(0, 0, 1920, 1080); //Clear canvas

                    //Change cursor back to default
                    StageHelper.getStages().get("Quiz").getScene().setCursor(Cursor.DEFAULT);

                    StageHelper.closeStage("drawingpad");

            });

                paintCanvas.setDisable(false); //Enable the canvas

                stage.show();

            } catch (IOException | NullPointerException e) {
                ErrorBox.display("A page failed to load.", false);
                e.printStackTrace();
            }

        }

    }


    /**
     * helper methods
     */
    //Ends the entire test and begins the results page
    private void endTest() {

        //Grab results fxml
        try {

            Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/printableresults.fxml")));

            StageHelper.getStages().get("Quiz").setScene(scene);

            StageHelper.getScenes().put("PrintableResults", scene);

        } catch (IOException | NullPointerException e) {
            ErrorBox.display("A page failed to load.", false);
            e.printStackTrace();
        }

    }

    //Display the new question
    private void displayNewQuestion() {

        //Clear previous question from questionArea
        questionArea.getChildren().clear();

        //Set new question to questionArea
        questionArea.getChildren().add(QuizManager.getCurrNode().getNode());

        // Set prompt
        questionPrompt.setText(QuizManager.getCurrNum() + 1 + ".) " + QuizManager.getCurrQuestion().getPrompt());

        //Set directions
        questionDirections.setText(QuizManager.getCurrQuestion().getDirections());

        //Change top right label to current question num / question amount
        currQuestionLabel.setText((QuizManager.getCurrNum() + 1)
                + " out of "
                + QuizManager.getQuizNodes().length
        );

        //If the button is selected
        questionHBox.getChildren().forEach(button -> {
            if (button.getStyle().equals(Constants.SELECTED_COLOR_FX)) {
                button.setStyle(Constants.UNSELECTED_COLOR_FX);
            }
        });

        questionPrompt.setStyle(((FlagButton) getCurrentButton()).isFlagged() ?
                "-fx-text-fill: " + Constants.FLAGGED_COLOR : "-fx-text-fill: black");

        subjAndQuestion.setText(QuizManager.getCurrQuestion().getSubject() + " QID:" + QuizManager.getCurrQuestion().getID());

    }

    //Color box to show it is selected
    private void selectCurrentQuizButton() {
        getCurrentButton().setStyle(Constants.SELECTED_COLOR_FX);
    }

    private Node getCurrentButton() {
        return questionHBox.getChildren().get(QuizManager.getCurrNum());
    }


    /**
     * Paint Canvas
     */
    //When pressing mouse
    public void canvasOnPressed(MouseEvent e) {
        //begin drawing
        gc.beginPath();
        gc.lineTo(e.getX(), e.getY());
        gc.stroke();
    }

    //When dragging mouse
    public void canvasOnDragged(MouseEvent event) {

        gc.lineTo(event.getX(), event.getY());

        gc.stroke();
    }

    public static void changeColor(Color color) {
        gc.setStroke(color);
    }

    public static void changeWidth(Double width) {
        gc.setLineWidth(width);
    }

}

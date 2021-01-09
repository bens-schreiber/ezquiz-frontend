package gui.quiz;

import etc.Constants;
import etc.Preference;
import gui.etc.FXHelper;
import gui.etc.Window;
import gui.popup.confirm.ConfirmNotifier;
import gui.popup.error.ErrorNotifier;
import gui.results.PrintResultsScreen;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
import javafx.util.Duration;
import quiz.QuizManager;
import quiz.nodes.QuizNode;

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
    VBox questionArea, addonVBox;

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

    private static Stage primaryStage;

    public static void setPrimaryStage(Stage primaryStage) {
        QuizController.primaryStage = primaryStage;
    }


    /**
     * Initial run method
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Establish preferences

        //Get preferences and apply them, if any.
        if (!Boolean.parseBoolean(QuizManager.getPreferences().get(Preference.NOTEPAD)))
            addonVBox.getChildren().remove(notePadButton);

        if (!Boolean.parseBoolean(QuizManager.getPreferences().get(Preference.CALCULATOR))) {
            addonVBox.getChildren().remove(calculatorButton);
        }

        if (!Boolean.parseBoolean(QuizManager.getPreferences().get(Preference.DRAWINGPAD))) {
            addonVBox.getChildren().remove(drawingPadButton);
        }

        quizName.setText(QuizManager.getPreferences().get(Preference.QUIZNAME));

        seconds = Integer.parseInt(QuizManager.getPreferences().get(Preference.TIME));

        //Disable back button by default.
        backButton.setDisable(true);

        userLabel.setText(userLabel.getText() + Constants.USERNAME);

        //Establish canvas properties
        paintCanvas.setDisable(true);
        gc = paintCanvas.getGraphicsContext2D();
        gc.setStroke(Color.WHITE);


        //Establish flag button amount
        //Whenever the button is clicked use the individualQuestionClicked handler
        for (QuizNode node : QuizManager.getQuizNodes()) {

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

    }

    //On an individual question clicked
    private void individualQuestionClicked(MouseEvent mouseEvent) {

        //Grab the spot of the question
        int questionSpot = questionHBox.getChildren().indexOf(mouseEvent.getSource());

        //Set current question to the spot
        QuizManager.setCurrentNum(questionSpot);

        //Display the current question.
        displayNewQuestion();

        //Color the flag button that is currently selected if not flagged
        if (!((FlagButton) getCurrentButton()).isFlagged()) {
            selectCurrentQuizButton();
        }

        backButton.setDisable(questionSpot == 0);
        nextButton.setDisable(questionSpot + 1 == QuizManager.getQuizNodes().length);

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
    public void nextButtonClicked() {

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
    public void backButtonClicked() {

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

    //On Flag Question clicked
    public void flagButtonClicked() {

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
    public void submitButtonClicked() {

        //If any questions are flagged
        if (questionHBox.getChildren().stream().anyMatch(node -> ((FlagButton) node).isFlagged())) {
            if (new ConfirmNotifier("Some questions are flagged. Are you sure you want to submit?").display().getResponse()) {
                endTest();
            }
        }

        //If all questions are answered.
        else if (QuizManager.allResponded()) {

            if (new ConfirmNotifier("Are you sure you want to submit?").display().getResponse()) {
                endTest();
            }

        }

        //If all questions aren't answered
        else if (new ConfirmNotifier("Some answers are unfinished. Are sure you want to submit?").display().getResponse()) {
            endTest();
        }

    }

    //todo: make drawingpad and notepad objects
    //When the calculator button is clicked
    public void calculatorButtonClicked() {

        try {

            if (FXHelper.getSavedStages().contains(Window.CALCULATOR)) {
                CalculatorController.getStage().close();
            }

            else {

                Stage stage = FXHelper.getPopupStage(Window.CALCULATOR, false);
                CalculatorController.setStage(stage);
                stage.show();

            }

        } catch (Exception e) {

            new ErrorNotifier("A page failed to load", true).display();

            e.printStackTrace();

        }

    }

    //When the notepad button is clicked
    public void notepadButtonClicked() {

        try {
            if (FXHelper.getSavedStages().contains(Window.NOTEPAD)) {
                NotePadController.getStage().close();
            } else {

                Stage stage = FXHelper.getPopupStage(Window.NOTEPAD, false);
                NotePadController.setStage(stage);
                stage.show();

            }

        } catch (Exception e) {

            new ErrorNotifier("A page failed to load", true).display();

            e.printStackTrace();

        }
    }


    //On drawing button clicked
    public void drawingPadButtonClicked() {

            try {

                if (FXHelper.getSavedStages().contains(Window.DRAWINGPAD)) {
                    paintCanvas.setDisable(true);
                    DrawingPadController.getStage().close();
                }

                else {

                    Stage stage = FXHelper.getPopupStage(Window.DRAWINGPAD, false);
                    DrawingPadController.setStage(stage);
                    stage.show();

                }

            } catch (Exception e) {

                new ErrorNotifier("A page failed to load", true).display();

                e.printStackTrace();

            }

    }


    /**
     * helper methods
     */
    //Ends the entire test and begins the results page
    private void endTest() {

        try {
            primaryStage.setScene(FXHelper.getScene(Window.PRINTRESULTS));
            PrintResultsScreen.setPrimaryStage(primaryStage);
        } catch (IOException e) {
            new ErrorNotifier("Results could not display.", true).display();
        }

    }

    //Display the new question
    private void displayNewQuestion() {

        //Clear previous question from questionArea
        questionArea.getChildren().clear();

        //Set new question to questionArea
        questionArea.getChildren().add(QuizManager.getCurrentNode().getNode());

        // Set prompt
        questionPrompt.setText(QuizManager.getCurrNum() + 1 + ".) " + QuizManager.getCurrentQuestion().getPrompt());

        //Set directions
        questionDirections.setText(QuizManager.getCurrentQuestion().getDirections());

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

        subjAndQuestion.setText(QuizManager.getCurrentQuestion().getSubject() + " QID:" + QuizManager.getCurrentQuestion().getID());

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

    /**
     * Addon to buttons that allows them to be considered 'flagged' or not.
     * For use in QuizController.
     */
    static class FlagButton extends Button {

        private boolean flagged;

        public void setFlagged(boolean flagged) {

            this.flagged = flagged;

            this.setStyle(flagged ? Constants.FLAGGED_COLOR_FX : Constants.UNSELECTED_COLOR_FX);

        }

        public boolean isFlagged() {
            return flagged;
        }
    }

}

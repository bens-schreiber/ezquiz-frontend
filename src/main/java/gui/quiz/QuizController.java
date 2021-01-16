package gui.quiz;

import etc.Constants;
import gui.PrimaryStageHelper;
import gui.etc.FXHelper;
import gui.etc.Window;
import gui.popup.confirm.ConfirmNotifier;
import gui.popup.error.ErrorNotifier;
import gui.quiz.tools.CalculatorController;
import gui.quiz.tools.DrawingPadController;
import gui.quiz.tools.NotePadController;
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
import quiz.Preference;
import quiz.QuestionManager;
import quiz.question.nodes.QuestionNode;
import requests.Account;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Main controller for test.
 */
public class QuizController extends PrimaryStageHelper implements Initializable {

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

    static GraphicsContext gc;

    //Default test is 30 minutes
    static Integer seconds = 1800;

    //Index of the QuizNodes the quiz is currently showing
    static int currentQuestionIndex = 0;

    /**
     * Initial run method
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Establish preferences
        if (!Boolean.parseBoolean(Preference.preferences.get(Preference.NOTEPAD))) {
            addonVBox.getChildren().remove(notePadButton);
        }

        if (!Boolean.parseBoolean(Preference.preferences.get(Preference.CALCULATOR))) {
            addonVBox.getChildren().remove(calculatorButton);
        }

        if (!Boolean.parseBoolean(Preference.preferences.get(Preference.DRAWINGPAD))) {
            addonVBox.getChildren().remove(drawingPadButton);
        }

        quizName.setText(Preference.preferences.get(Preference.QUIZNAME));

        seconds = Integer.parseInt(Preference.preferences.get(Preference.TIME));

        //Disable back button by default.
        backButton.setDisable(true);

        userLabel.setText(userLabel.getText() + Account.getUsername());

        //Establish canvas properties
        paintCanvas.setDisable(true);
        gc = paintCanvas.getGraphicsContext2D();
        gc.setStroke(Color.WHITE);

        //Establish flag button amount
        //Whenever the button is clicked use the individualQuestionClicked handler
        for (int i = 0; i < QuestionManager.getQuestionNodes().length; i++) {

            FlagButton button = new FlagButton();

            button.setOnMouseClicked(this::individualQuestionClicked);

            questionHBox.getChildren().add(button);

        }

        displayNewQuestion();

    }


    /**
     * change display to current question
     */
    private void displayNewQuestion() {

        QuestionNode currentQuestionNode = QuestionManager.getQuestionNodes()[currentQuestionIndex];

        questionArea.getChildren().clear();

        //Set new question to questionArea
        questionArea.getChildren().add(currentQuestionNode.getNode());

        //Set prompt, determine text color based on if the question is flagged or not
        questionPrompt.setText(currentQuestionIndex + 1 + ".) " + currentQuestionNode.getQuestion().getPrompt());
        questionPrompt.setStyle(currentFlagButton().isFlagged() ? "-fx-text-fill: " + Constants.FLAGGED_COLOR : "-fx-text-fill: black");

        //Set directions
        questionDirections.setText(currentQuestionNode.getQuestion().getDirections());

        //Change top right label to current question num / question amount
        currQuestionLabel.setText(currentQuestionIndex + 1 + " / " + QuestionManager.getQuestionNodes().length);

        subjAndQuestion.setText(currentQuestionNode.getQuestion().getSubject() + " QID:" + currentQuestionNode.getQuestion().getID());

        highlightSelected();

    }

    /**
     * on buttons clicked
     */
    //On an individual question clicked
    private void individualQuestionClicked(MouseEvent mouseEvent) {

        //Grab the spot of the question
        //Set current question to the spot
        currentQuestionIndex = questionHBox.getChildren().indexOf(mouseEvent.getSource());

        //display the new question
        displayNewQuestion();

        backButton.setDisable(currentQuestionIndex == 0);
        nextButton.setDisable(currentQuestionIndex + 1 == QuestionManager.getQuestionNodes().length);

    }

    //When next button is clicked
    public void nextButtonClicked() {

        currentQuestionIndex++;
        displayNewQuestion();

        //Disable/enable next and back based on position
        backButton.setDisable(currentQuestionIndex == 0);
        nextButton.setDisable(currentQuestionIndex + 1 == QuestionManager.getQuestionNodes().length);

    }

    //When back button is clicked
    public void backButtonClicked() {

        currentQuestionIndex--;
        displayNewQuestion();

        //Disable/enable next and back based on position
        backButton.setDisable(currentQuestionIndex == 0);
        nextButton.setDisable(currentQuestionIndex + 1 == QuestionManager.getQuestionNodes().length);

    }

    public void flagButtonClicked() {

        //Un-flag if already flagged.
        if (currentFlagButton().isFlagged()) {

            currentFlagButton().setFlagged(false);
            highlightSelected();
            questionPrompt.setStyle("-fx-text-fill: black");

        } else {
            currentFlagButton().setFlagged(true);
            questionPrompt.setStyle("-fx-text-fill: " + Constants.FLAGGED_COLOR);
        }
    }

    public void submitButtonClicked() {

        //If any questions are flagged
        if (questionHBox.getChildren().stream().anyMatch(node -> ((FlagButton) node).isFlagged())) {
            if (new ConfirmNotifier("Some questions are flagged. Are you sure you want to submit?").display().getResponse()) {
                endTest();
            }
        }

        //If all questions are answered.
        else if (List.of(QuestionManager.getQuestionNodes()).stream().allMatch(QuestionNode::isAnswered)) {

            if (new ConfirmNotifier("Are you sure you want to submit?").display().getResponse()) {
                endTest();
            }

        }

        //If all questions aren't answered
        else if (new ConfirmNotifier("Some answers are unfinished. Are sure you want to submit?").display().getResponse()) {
            endTest();
        }

    }


    /**
     * Test Tools
     */
    public void calculatorButtonClicked() {

        try {

            //If an instance of Calculator is already open
            if (FXHelper.getOpenedInstances().contains(Window.CALCULATOR)) {

                //Remove from stages
                FXHelper.getOpenedInstances().remove(Window.CALCULATOR);

                //Close
                CalculatorController.getStage().close();
            } else {

                Stage stage = FXHelper.getPopupStage(Window.CALCULATOR, false);

                //If X button is clicked
                stage.setOnCloseRequest(e -> FXHelper.getOpenedInstances().remove(Window.CALCULATOR));

                //Add to saved stages
                FXHelper.getOpenedInstances().add(Window.CALCULATOR);
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
            if (FXHelper.getOpenedInstances().contains(Window.NOTEPAD)) {
                FXHelper.getOpenedInstances().remove(Window.NOTEPAD);
                NotePadController.getStage().close();
            } else {

                Stage stage = FXHelper.getPopupStage(Window.NOTEPAD, false);

                //If X button is clicked
                stage.setOnCloseRequest(e -> FXHelper.getOpenedInstances().remove(Window.NOTEPAD));

                FXHelper.getOpenedInstances().add(Window.NOTEPAD);
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

            if (FXHelper.getOpenedInstances().contains(Window.DRAWINGPAD)) {

                paintCanvas.setDisable(true);

                FXHelper.getOpenedInstances().remove(Window.DRAWINGPAD);
                DrawingPadController.getStage().close();
            } else {

                paintCanvas.setDisable(false);

                Stage stage = FXHelper.getPopupStage(Window.DRAWINGPAD, false);

                //If X button is clicked
                stage.setOnCloseRequest(e -> {
                    FXHelper.getOpenedInstances().remove(Window.DRAWINGPAD);
                    paintCanvas.setDisable(true);
                });

                FXHelper.getOpenedInstances().add(Window.DRAWINGPAD);
                DrawingPadController.setStage(stage);

                stage.show();

            }

        } catch (Exception e) {

            new ErrorNotifier("A page failed to load", true).display();

            e.printStackTrace();

        }

    }

    private FlagButton currentFlagButton() {
        return (FlagButton) questionHBox.getChildren().get(currentQuestionIndex);
    }

    private void highlightSelected() {
        //unhighlight all nodes unless flagged
        for (Node node : questionHBox.getChildren()) {
            if (!((FlagButton) node).isFlagged()) node.setStyle(Constants.UNSELECTED_COLOR_FX);
        }

        //highlight unless flagged
        if (!currentFlagButton().isFlagged()) {
            currentFlagButton().setStyle(Constants.SELECTED_COLOR_FX);
        }
    }

    //Ends the entire test and begins the results page
    private static void endTest() {
        try {
            primaryStage.setScene(FXHelper.getScene(Window.PRINTRESULTS));
        } catch (IOException e) {
            new ErrorNotifier("Results could not display.", true).display();
        }

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


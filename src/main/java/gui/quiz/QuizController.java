package gui.quiz;

import etc.Constants;
import gui.PrimaryStageHolder;
import gui.etc.Account;
import gui.etc.FXHelper;
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
import questions.QuizQuestions;
import questions.question.QuestionNode;

import java.net.URL;
import java.util.List;
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

    static GraphicsContext gc;

    //Default test is 30 minutes
    static Integer seconds = 1800;

    //Index of the QuizNodes the quiz is currently showing
    static int currentQuestionIndex;

    /**
     * Initial run method
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        currentQuestionIndex = 0;

        //Establish preferences
        //todo: preferences
        if (!Boolean.parseBoolean(QuizHelper.Preference.preferences.get(QuizHelper.Preference.NOTEPAD))) {
            addonVBox.getChildren().remove(notePadButton);
        }

        if (!Boolean.parseBoolean(QuizHelper.Preference.preferences.get(QuizHelper.Preference.CALCULATOR))) {
            addonVBox.getChildren().remove(calculatorButton);
        }

        if (!Boolean.parseBoolean(QuizHelper.Preference.preferences.get(QuizHelper.Preference.DRAWINGPAD))) {
            addonVBox.getChildren().remove(drawingPadButton);
        }

        quizName.setText(Account.getQuiz().getName());

        seconds = Integer.parseInt(QuizHelper.Preference.preferences.get(QuizHelper.Preference.TIME));

        userLabel.setText(userLabel.getText() + Account.getUsername());

        //Disable back button by default.
        backButton.setDisable(true);

        //Establish canvas properties
        paintCanvas.setDisable(true);
        gc = paintCanvas.getGraphicsContext2D();
        gc.setStroke(Color.WHITE);

        //Establish flag button amount
        //Whenever the button is clicked use the individualQuestionClicked handler
        for (int i = 0; i < QuizQuestions.getQuestionNodes().length; i++) {

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

        QuestionNode currentQuestionNode = QuizQuestions.getQuestionNodes()[currentQuestionIndex];

        questionArea.getChildren().clear();

        //Set new question to questionArea
        questionArea.getChildren().add(currentQuestionNode.getNode());

        //Set prompt, determine text color based on if the question is flagged or not
        questionPrompt.setText(currentQuestionIndex + 1 + ".) " + currentQuestionNode.getPrompt());
        questionPrompt.setStyle(currentFlagButton().isFlagged() ? "-fx-text-fill: " + Constants.FLAGGED_COLOR : "-fx-text-fill: black");

        //Set directions
        questionDirections.setText(currentQuestionNode.getDirections());

        //Change top right label to current question num / question amount
        currQuestionLabel.setText(currentQuestionIndex + 1 + " / " + QuizQuestions.getQuestionNodes().length);

        subjAndQuestion.setText(currentQuestionNode.getSubject() + " QID:" + currentQuestionNode.getID());

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
        nextButton.setDisable(currentQuestionIndex + 1 == QuizQuestions.getQuestionNodes().length);

    }


    //When next button is clicked
    public void nextButtonClicked() {

        currentQuestionIndex++;
        displayNewQuestion();

        //Disable/enable next and back based on position
        backButton.setDisable(currentQuestionIndex == 0);
        nextButton.setDisable(currentQuestionIndex + 1 == QuizQuestions.getQuestionNodes().length);

    }


    //When back button is clicked
    public void backButtonClicked() {

        currentQuestionIndex--;
        displayNewQuestion();

        //Disable/enable next and back based on position
        backButton.setDisable(currentQuestionIndex == 0);
        nextButton.setDisable(currentQuestionIndex + 1 == QuizQuestions.getQuestionNodes().length);

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

        if (!List.of(QuizQuestions.getQuestionNodes()).stream().allMatch(QuestionNode::isAnswered)) {
            if (new ConfirmNotifier("Some answers are unfinished. Are sure you want to submit?").display().getResponse()) {
                QuizHelper.endQuiz();
            }
        }

        //If any questions are flagged
        else if (questionHBox.getChildren().stream().anyMatch(node -> ((FlagButton) node).isFlagged())) {
            if (new ConfirmNotifier("Some questions are flagged. Are you sure you want to submit?").display().getResponse()) {
                QuizHelper.endQuiz();
            }
        }


        //If all questions are answered.
        else {
            if (new ConfirmNotifier("Are you sure you want to submit?").display().getResponse()) {
                QuizHelper.endQuiz();
            }
        }
    }


    /**
     * Test Tools
     */
    public void calculatorButtonClicked() {

        try {

            //If an instance of Calculator is already open
            if (FXHelper.getOpenedInstances().contains(FXHelper.Window.CALCULATOR)) {

                //Remove from stages
                FXHelper.getOpenedInstances().remove(FXHelper.Window.CALCULATOR);

                //Close
                CalculatorController.getStage().close();
            } else {

                Stage stage = FXHelper.getPopupStage(FXHelper.Window.CALCULATOR, false);

                stage.setOnCloseRequest(e -> FXHelper.getOpenedInstances().remove(FXHelper.Window.CALCULATOR));

                //Add to saved stages
                FXHelper.getOpenedInstances().add(FXHelper.Window.CALCULATOR);
                CalculatorController.setStage(stage);
                stage.show();

            }

        } catch (Exception e) {

            new ErrorNotifier("A page failed to load", true).display(PrimaryStageHolder.getPrimaryStage());

            e.printStackTrace();

        }

    }


    //When the notepad button is clicked
    public void notepadButtonClicked() {

        try {
            if (FXHelper.getOpenedInstances().contains(FXHelper.Window.NOTEPAD)) {
                FXHelper.getOpenedInstances().remove(FXHelper.Window.NOTEPAD);
                NotePadController.getStage().close();
            } else {

                Stage stage = FXHelper.getPopupStage(FXHelper.Window.NOTEPAD, false);

                stage.setOnCloseRequest(e -> FXHelper.getOpenedInstances().remove(FXHelper.Window.NOTEPAD));

                FXHelper.getOpenedInstances().add(FXHelper.Window.NOTEPAD);
                NotePadController.setStage(stage);
                stage.show();

            }

        } catch (Exception e) {

            new ErrorNotifier("A page failed to load", true).display(PrimaryStageHolder.getPrimaryStage());

            e.printStackTrace();

        }
    }


    //On drawing button clicked
    public void drawingPadButtonClicked() {

        try {

            if (FXHelper.getOpenedInstances().contains(FXHelper.Window.DRAWINGPAD)) {

                paintCanvas.setDisable(true);

                FXHelper.getOpenedInstances().remove(FXHelper.Window.DRAWINGPAD);
                DrawingPadController.getStage().close();
            } else {

                paintCanvas.setDisable(false);

                Stage stage = FXHelper.getPopupStage(FXHelper.Window.DRAWINGPAD, false);
                FXHelper.getOpenedInstances().add(FXHelper.Window.DRAWINGPAD);
                DrawingPadController.setStage(stage);

                stage.setOnCloseRequest(e -> {
                    FXHelper.getOpenedInstances().remove(FXHelper.Window.DRAWINGPAD);
                    paintCanvas.setDisable(true);
                });

                stage.show();

            }

        } catch (Exception e) {

            new ErrorNotifier("A page failed to load", true).display(PrimaryStageHolder.getPrimaryStage());

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


    public static void clearCanvas() {
        gc.clearRect(0, 0, 1920, 1080);
    }


    //Special button that has the property of a flagged boolean
    static class FlagButton extends Button {

        private boolean flagged;

        public void setFlagged(boolean flagged) {

            this.flagged = flagged;

            //Change style (color) of button according to flagged boolean
            this.setStyle(flagged ? Constants.FLAGGED_COLOR_FX : Constants.UNSELECTED_COLOR_FX);

        }

        public boolean isFlagged() {
            return flagged;
        }
    }
}
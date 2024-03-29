package com.benschreiber.gui.windows.quiz;

import com.benschreiber.etc.Account;
import com.benschreiber.gui.Constants;
import com.benschreiber.gui.FXController;
import com.benschreiber.gui.FXHelper;
import com.benschreiber.gui.windows.quiz.tools.CalculatorController;
import com.benschreiber.gui.windows.quiz.tools.DrawingPadController;
import com.benschreiber.gui.windows.quiz.tools.NotePadController;
import com.benschreiber.gui.windows.quiz.tools.QuizTimer;
import com.benschreiber.gui.fxobjs.QuestionNode;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TabPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Stage for Quiz.
 */
public class QuizController extends FXController implements Initializable {

    @FXML
    Button backButton, nextButton, notePadButton, calculatorButton, drawingPadButton;

    @FXML
    Label quizName, quizTimer, currQuestionLabel, userLabel, subjAndQuestion;

    @FXML
    VBox addonVBox;

    @FXML
    TabPane tabWizard;

    @FXML
    AnchorPane questionPane;

    @FXML
    HBox questionHBox;

    private Stage drawingPad = new Stage();
    private Stage calculator = new Stage();
    private Stage notepad = new Stage();

    /**
     * Initial run method
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        addonVBox.getChildren().remove(QuizHelper.getPreferences().isCalculator() ? null : calculatorButton);
        addonVBox.getChildren().remove(QuizHelper.getPreferences().isDrawingPad() ? null : drawingPadButton);
        addonVBox.getChildren().remove(QuizHelper.getPreferences().isNotePad() ? null : notePadButton);

        //Set up permanent labels
        quizName.setText(Account.getQuiz().getName());
        userLabel.setText(userLabel.getText() + Account.getUser().getUsername());

        QuizTimer timer = new QuizTimer(quizTimer, QuizHelper.getPreferences().getTime());
        timer.startTimer();

        //Load all questions into the tabWizard
        for (QuestionNode questionNode : QuizHelper.getQuestionNodes()) {

            //Add every questionNode to the tab wizard to be displayed.
            tabWizard.getTabs().add(questionNode.getAsTab());

            //Establish flag button
            //Whenever the button is clicked use the individualQuestionClicked handler
            FlagButton button = new FlagButton();
            button.setOnMouseClicked(this::individualQuestionClicked);
            questionHBox.getChildren().add(button);
        }

        //Sets the questionPane to the currently selected classes.question.
        displayCurrentQuestion();

    }


    /**
     * on buttons clicked
     */
    //On an individual classes.question clicked
    private void individualQuestionClicked(MouseEvent mouseEvent) {

        //Grab the spot of the classes.question
        //Set current classes.question to the spot
        tabWizard.getSelectionModel().select(questionHBox.getChildren().indexOf(mouseEvent.getSource()));

        //display the new classes.question
        displayCurrentQuestion();

    }


    //When next button is clicked
    public void nextButtonClicked() {

        //Display the next classes.question
        tabWizard.getSelectionModel().selectNext();
        displayCurrentQuestion();

    }


    //When back button is clicked
    public void backButtonClicked() {

        tabWizard.getSelectionModel().selectPrevious();
        displayCurrentQuestion();

    }


    public void flagButtonClicked() {

        //Un-flag if the button selected is already flagged
        if (currentlySelectedFlagButton().isFlagged()) {

            currentlySelectedFlagButton().setFlagged(false);
            highlightSelected();

        } else {
            currentlySelectedFlagButton().setFlagged(true);
        }
    }


    public void submitButtonClicked() {

        try {
            if (!List.of(QuizHelper.getQuestionNodes()).stream().allMatch(QuestionNode::isAnswered)) {
                if (confirmNotifier.setPrompt("Some answers are unfinished. Are sure you want to submit?").display().getResponse()) {
                    QuizHelper.endQuiz();
                }
            }

            //If any questions are flagged
            else if (questionHBox.getChildren().stream().anyMatch(node -> ((FlagButton) node).isFlagged())) {
                if (confirmNotifier.setPrompt("Some questions are flagged. Are you sure you want to submit?").display().getResponse()) {
                    QuizHelper.endQuiz();
                }
            }


            //If all questions are answered.
            else {
                if (confirmNotifier.setPrompt("Are you sure you want to submit?").display().getResponse()) {
                    QuizHelper.endQuiz();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            errorHandle();
        }
    }


    /**
     * Test Tools
     */
    public void calculatorButtonClicked() {

        try {

            if (!calculator.isShowing()) {

                calculator = FXHelper.getPopupStage(FXHelper.Window.CALCULATOR, false);
                calculator.initOwner(FXController.getPrimaryStage());
                calculator.setAlwaysOnTop(true);

                CalculatorController.setStage(calculator);

                calculator.show();

            } else {

                calculator.close();

            }

        } catch (Exception e) {

            e.printStackTrace();
            errorHandle();

        }

    }

    //When the notepad button is clicked
    public void notepadButtonClicked() {

        try {

            if (!notepad.isShowing()) {

                notepad = FXHelper.getPopupStage(FXHelper.Window.NOTEPAD, false);
                notepad.initOwner(FXController.getPrimaryStage());
                notepad.setAlwaysOnTop(true);

                NotePadController.setStage(notepad);

                notepad.show();

            } else {
                notepad.close();
            }

        } catch (Exception e) {

            e.printStackTrace();
            errorHandle();

        }
    }

    //On drawing button clicked
    public void drawingPadButtonClicked() {

        try {

            if (!drawingPad.isShowing()) {


                DrawingPadController.setPane(questionPane);

                drawingPad = FXHelper.getPopupStage(FXHelper.Window.DRAWING_PAD, false);
                drawingPad.setOnCloseRequest(e -> questionPane.getChildren().remove(1));
                drawingPad.initOwner(FXController.getPrimaryStage());
                drawingPad.setAlwaysOnTop(true);

                DrawingPadController.setStage(drawingPad);

                DrawingPadController.getStage().setAlwaysOnTop(true);

                drawingPad.show();

            } else {

                //Disable the drawing pad canvas added by DrawingPadController
                questionPane.getChildren().remove(1);
                drawingPad.close();

            }

        } catch (Exception e) {

            e.printStackTrace();
            errorHandle();

        }

    }

    /**
     * Helper methods
     */
    //Displays whichever classes.question the tabWizard is on
    private void displayCurrentQuestion() {

        int currentQuestionIndex = tabWizard.getSelectionModel().getSelectedIndex();

        //Disable/enable next and back based on position
        backButton.setDisable(currentQuestionIndex == 0);
        nextButton.setDisable(currentQuestionIndex + 1 == QuizHelper.getQuestionNodes().length);

        QuestionNode currentQuestionNode = QuizHelper.getQuestionNodes()[currentQuestionIndex];

        //Change top right label to current classes.question num / classes.question amount
        currQuestionLabel.setText(currentQuestionIndex + 1 + " / " + QuizHelper.getQuestionNodes().length);
        subjAndQuestion.setText(" QID:" + currentQuestionNode.getID());

        highlightSelected();

    }

    private FlagButton currentlySelectedFlagButton() {
        return (FlagButton) questionHBox.getChildren().get(tabWizard.getSelectionModel().getSelectedIndex());
    }

    private void highlightSelected() {

        //unhighlight all nodes unless flagged
        for (Node node : questionHBox.getChildren()) {
            if (!((FlagButton) node).isFlagged()) node.setStyle(Constants.UNSELECTED_COLOR_FX);
        }

        //highlight unless flagged
        if (!currentlySelectedFlagButton().isFlagged()) {
            currentlySelectedFlagButton().setStyle(Constants.SELECTED_COLOR_FX);
        }
    }


    //Special button that has the property of a flagged boolean
    private static class FlagButton extends Button {

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
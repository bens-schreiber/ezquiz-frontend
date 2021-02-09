package gui.quiz;

import etc.Constants;
import gui.FXController;
import gui.account.Account;
import gui.etc.FXHelper;
import gui.quiz.tools.CalculatorController;
import gui.quiz.tools.DrawingPadController;
import gui.quiz.tools.NotePadController;
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
import javafx.stage.Stage;
import questions.question.QuestionNode;

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

    //Default test is 30 minutes
    static Integer seconds = 1800;

    private Stage drawingPad = new Stage();
    private Stage calculator = new Stage();
    private Stage notepad = new Stage();

    static QuizController quizController;

    /**
     * Initial run method
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Establish preferences
        if (!Boolean.parseBoolean(QuizHelper.Preference.getPreferences().get(QuizHelper.Preference.NOTEPAD))) {
            addonVBox.getChildren().remove(notePadButton);
        }

        if (!Boolean.parseBoolean(QuizHelper.Preference.getPreferences().get(QuizHelper.Preference.CALCULATOR))) {
            addonVBox.getChildren().remove(calculatorButton);
        }

        if (!Boolean.parseBoolean(QuizHelper.Preference.getPreferences().get(QuizHelper.Preference.DRAWINGPAD))) {
            addonVBox.getChildren().remove(drawingPadButton);
        }

        //Set up permanent labels
        quizName.setText(Account.getQuiz().getName());
        userLabel.setText(userLabel.getText() + Account.getUser().getUsername());

        //Disable back button by default.
        backButton.setDisable(true);

        //Load all questions into the tabWizard
        for (QuestionNode questionNode : QuizQuestions.getQuestionNodes()) {

            tabWizard.getTabs().add(questionNode.getTab());

            //Establish flag button
            //Whenever the button is clicked use the individualQuestionClicked handler
            FlagButton button = new FlagButton();
            button.setOnMouseClicked(this::individualQuestionClicked);
            questionHBox.getChildren().add(button);
        }

        quizController = this;

        displayNewQuestion();

    }

    /**
     * change display to current question
     */
    private void displayNewQuestion() {

        int currentQuestionIndex = tabWizard.getSelectionModel().getSelectedIndex();

        //Disable/enable next and back based on position
        backButton.setDisable(currentQuestionIndex == 0);
        nextButton.setDisable(currentQuestionIndex + 1 == QuizQuestions.getQuestionNodes().length);

        QuestionNode currentQuestionNode = QuizQuestions.getQuestionNodes()[currentQuestionIndex];

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
        tabWizard.getSelectionModel().select(questionHBox.getChildren().indexOf(mouseEvent.getSource()));

        //display the new question
        displayNewQuestion();

    }


    //When next button is clicked
    public void nextButtonClicked() {

        //Display the next question
        tabWizard.getSelectionModel().selectNext();
        displayNewQuestion();

    }


    //When back button is clicked
    public void backButtonClicked() {

        tabWizard.getSelectionModel().selectPrevious();
        displayNewQuestion();

    }


    public void flagButtonClicked() {

        //Un-flag if already flagged.
        if (currentFlagButton().isFlagged()) {

            currentFlagButton().setFlagged(false);
            highlightSelected();

        } else {
            currentFlagButton().setFlagged(true);
        }
    }


    public void submitButtonClicked() {

        try {
            if (!List.of(QuizQuestions.getQuestionNodes()).stream().allMatch(QuestionNode::isAnswered)) {
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
            userNotifier.setText(AlertText.INTERNAL_ERROR).display();
        }
    }


    /**
     * Test Tools
     */
    public void calculatorButtonClicked() {

        try {

            if (!calculator.isShowing()) {

                calculator = FXHelper.getPopupStage(FXHelper.Window.CALCULATOR, false);

                CalculatorController.setStage(calculator);

                calculator.show();

            } else {

                calculator.close();

            }

        } catch (Exception e) {

            e.printStackTrace();
            userNotifier.setText(AlertText.INTERNAL_ERROR).display();

        }

    }


    //When the notepad button is clicked
    public void notepadButtonClicked() {

        try {

            if (!notepad.isShowing()) {

                notepad = FXHelper.getPopupStage(FXHelper.Window.NOTEPAD, false);

                NotePadController.setStage(notepad);

                notepad.show();

            } else {

                notepad.close();

            }

        } catch (Exception e) {

            e.printStackTrace();
            userNotifier.setText(AlertText.INTERNAL_ERROR).display();

        }
    }


    //On drawing button clicked
    public void drawingPadButtonClicked() {

        try {

            if (!drawingPad.isShowing()) {


                drawingPad = FXHelper.getPopupStage(FXHelper.Window.DRAWING_PAD, false);

                drawingPad.setOnCloseRequest(e -> questionPane.getChildren().remove(1));

                DrawingPadController.setStage(drawingPad);

                drawingPad.show();

            } else {

                //Disable the drawing pad canvas added by DrawingPadController
                questionPane.getChildren().remove(1);

                drawingPad.close();

            }

        } catch (Exception e) {

            e.printStackTrace();
            userNotifier.setText(AlertText.INTERNAL_ERROR).display();

        }

    }


    private FlagButton currentFlagButton() {
        return (FlagButton) questionHBox.getChildren().get(tabWizard.getSelectionModel().getSelectedIndex());
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

    public static AnchorPane getQuestionPane() {
        return quizController.questionPane;
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
package gui.controllers;

import gui.addons.calculator.CalculatorHelper;
import gui.addons.notepad.NotePadHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

import javafx.stage.Stage;
import quiz.questions.nodes.ConfirmBox;
import quiz.QuizController;
import quiz.questions.NodeHelper;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TestController implements Initializable, Exitable {

    @FXML
    Button backButton, nextButton, notePadButton, calculatorButton, drawingPadButton;

    @FXML
    Label questionPrompt, questionDirections;

    @FXML
    VBox questionArea;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        if (!QuizController.preferences.isEmpty()) {//Get preferences and apply them, if any.

            notePadButton.setVisible(QuizController.preferences.get("Notepad"));

            calculatorButton.setVisible(QuizController.preferences.get("Calculator"));

            drawingPadButton.setVisible(QuizController.preferences.get("Drawing Pad"));

        }

        displayNewQuestion(); //Display the new question.

        backButton.setDisable(true); //Disable back button by default.
    }


    public void onNextButton() {

        if (QuizController.getCurrNum() < QuizController.getQuestionAmount() - 1) {

            QuizController.nextQuestion(); //Change to the next question.

            displayNewQuestion(); //Display the new question.
        }

        if (QuizController.getCurrNum() != 0)
            backButton.setDisable(false); //Enable the back button if not on first question.

        if (QuizController.getCurrNum() == QuizController.getQuestionAmount() - 1)
            nextButton.setDisable(true); //Disable next button if on last
    }


    public void onBackButton() {
        if (QuizController.getCurrNum() > 0) {

            QuizController.prevQuestion(); //Goto previous question.

            displayNewQuestion(); //Load previous question.

        }

        if (QuizController.getCurrNum() == 0) backButton.setDisable(true); //Disable back button if on last question.

        if (QuizController.getCurrNum() != QuizController.getQuestionAmount() - 1 && nextButton.isDisable()) {

            nextButton.setDisable(false); //Enable next button if not on last question.

        }
    }


    public void onSubmitButton(MouseEvent mouseEvent) {
        try {

            if (QuizController.responses.size() == QuizController.getQuestionAmount()) { //If all questions are answered.

                if (ConfirmBox.display("Are you sure you want to submit?")) {

                    exit(mouseEvent); //Exit this page.

                    displayResults(); //Create results page.

                }

            } else if (ConfirmBox.display("Some answers are unfinished. Are sure you want to submit?")) {//If all questions aren't answered

                exit(mouseEvent); //Exit this page.

                displayResults(); //Create results page.

            }
        } catch (IOException ioException) {

            ioException.printStackTrace();

        }

    }

    public void onCalculatorButton() throws IOException {
        if (!CalculatorHelper.isDisplaying()) { //Make sure one calculator only is open.

            Parent root = FXMLLoader.load(getClass().getResource("/calculator.fxml")); //Grab calculator

            Scene scene = new Scene(root);

            Stage stage = new Stage();

            stage.setAlwaysOnTop(true); //Keep calculator on test.

            stage.setScene(scene);

            stage.show();

            stage.setOnCloseRequest(event -> CalculatorHelper.setDisplaying(false)); //Set displaying to false if closed.
        }
    }

    public void onNotepadButton() throws IOException {
        if (!NotePadHelper.isDisplaying()) {
            Parent root = FXMLLoader.load(getClass().getResource("/notepad.fxml")); //Grab calculator

            Scene scene = new Scene(root);

            Stage stage = new Stage();

            stage.setAlwaysOnTop(true); //Keep notepad on test.

            stage.setScene(scene);

            stage.show();

            stage.setOnCloseRequest(event -> NotePadHelper.setDisplaying(false)); //Set displaying to false if closed.
        }
    }

    private void displayNewQuestion() {

        questionArea.getChildren().clear(); //Clear previous question from questionArea

        questionArea.getChildren().add(NodeHelper.getNodeFromQuestion(QuizController.getCurrQuestion())); //Add new question

        questionPrompt.setText(QuizController.getCurrQuestion().getPrompt()); // Set prompt

        questionDirections.setText(QuizController.getCurrQuestion().getDirections()); //Set directions
    }

    private void displayResults() throws IOException {

        Parent results = FXMLLoader.load(getClass().getResource("/results.fxml")); //Grab results fxml

        Scene scene = new Scene(results);

        Stage stage = new Stage();

        stage.setScene(scene);

        stage.show();
    }
}

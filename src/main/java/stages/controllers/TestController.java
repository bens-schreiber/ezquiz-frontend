package stages.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import javafx.stage.Stage;
import quiz.questions.nodes.ConfirmBox;
import quiz.QuizController;
import quiz.questions.NodeHelper;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class TestController implements Initializable {

    @FXML
    Button backButton;
    @FXML
    Button nextButton;
    @FXML
    Label questionPrompt;
    @FXML
    Label questionDirections;
    @FXML
    VBox questionArea;
    @FXML
    AnchorPane mainAnchor;
    @FXML
    Button notePadButton;
    @FXML
    Button calculatorButton;
    @FXML
    Button drawingPadButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        notePadButton.setVisible(QuizController.preferences.get("Notepad"));
        calculatorButton.setVisible(QuizController.preferences.get("Calculator"));
        drawingPadButton.setVisible(QuizController.preferences.get("Drawing Pad"));

        displayNewQuestion();
        backButton.setDisable(true);
    }


    public void onNextButton(MouseEvent mouseEvent) {

        if (QuizController.getCurrNum() < QuizController.getQuestionAmount() - 1) {
            QuizController.nextQuestion();
            displayNewQuestion();
        }

        if (QuizController.getCurrNum() != 0) backButton.setDisable(false);
        if (QuizController.getCurrNum() == QuizController.getQuestionAmount() - 1) nextButton.setDisable(true);
    }


    public void onBackButton(MouseEvent mouseEvent) {
        if (QuizController.getCurrNum() > 0) {
            QuizController.prevQuestion();
            displayNewQuestion();
        }

        if (QuizController.getCurrNum() == 0) backButton.setDisable(true);
        if (QuizController.getCurrNum() != QuizController.getQuestionAmount() - 1 && nextButton.isDisable())
            nextButton.setDisable(false);
    }


    public void onSubmitButton(MouseEvent mouseEvent) {
        try {
            if (QuizController.responses.size() == QuizController.getQuestionAmount()) {
                if (ConfirmBox.display("Are you sure you want to submit?")) {
                    exit(mouseEvent);
                    displayResults();
                }

            } else if (ConfirmBox.display("Some answers are unfinished. Are sure you want to submit?")) {
                exit(mouseEvent);
                displayResults();
            }
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    private void displayNewQuestion() {
        questionArea.getChildren().clear();
        questionArea.getChildren().add(NodeHelper.getNodeFromQuestion(QuizController.getCurrQuestion()));
        questionPrompt.setText(QuizController.getCurrQuestion().getPrompt());
        questionDirections.setText(QuizController.getCurrQuestion().getDirections());
    }

    private void displayResults() throws IOException {
        Parent results = FXMLLoader.load(getClass().getResource("/results.fxml"));
        Scene scene = new Scene(results);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();


    }

    private void exit(MouseEvent mouseEvent) {
        Node source = (Node) mouseEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }


}

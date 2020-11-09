package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

import javafx.stage.Stage;
import nodes.ConfirmBox;
import org.json.JSONException;
import quiz.Quiz;
import nodes.NodeHelper;
import stages.Results;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class TestController implements Initializable {

    @FXML
    Button backButton;
    @FXML
    Button nextButton;
    @FXML
    Label questionPrompt;
    @FXML
    VBox questionArea;
    @FXML
    AnchorPane mainAnchor;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        backButton.setDisable(true);
        questionPrompt.setText(Quiz.getCurrQuestion().getPrompt());
        questionArea.getChildren().add(NodeHelper.getNodeFromQuestion(Quiz.getCurrQuestion()));
    }


    public void onNextButton(MouseEvent mouseEvent) {

        if (Quiz.getCurrNum() < Quiz.getQuestionAmount() - 1) {
            Quiz.nextQuestion();
            questionArea.getChildren().clear();
            questionArea.getChildren().add(NodeHelper.getNodeFromQuestion(Quiz.getCurrQuestion()));
            questionPrompt.setText(Quiz.getCurrQuestion().getPrompt());
        }

        if (Quiz.getCurrNum() != 0) backButton.setDisable(false);
        if (Quiz.getCurrNum() == Quiz.getQuestionAmount() - 1) nextButton.setDisable(true);
    }

    public void onBackButton(MouseEvent mouseEvent) {
        if (Quiz.getCurrNum() > 0) {
            Quiz.prevQuestion();
            questionArea.getChildren().clear();
            questionArea.getChildren().add(NodeHelper.getNodeFromQuestion(Quiz.getCurrQuestion()));
            questionPrompt.setText(Quiz.getCurrQuestion().getPrompt());
        }

        if (Quiz.getCurrNum() == 0) backButton.setDisable(true);
        if (Quiz.getCurrNum() != Quiz.getQuestionAmount() - 1 && nextButton.isDisable()) nextButton.setDisable(false);
    }

    public void onSubmitButton(MouseEvent mouseEvent) {
        try {
            if (Quiz.responses.size() == Quiz.getQuestionAmount()) {
                if (ConfirmBox.display("Are you sure you want to submit?")) {
                    exit(mouseEvent);
                    Results.display();
                }

            } else if (ConfirmBox.display("Some answers are unfinished. Are sure you want to submit?")) {
                exit(mouseEvent);
                Results.display();
            }
        } catch (IOException | JSONException ioException) {
            ioException.printStackTrace();
        }
    }

    private void exit(MouseEvent mouseEvent) {
        final Node source = (Node) mouseEvent.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }


}

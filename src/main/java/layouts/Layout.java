package layouts;

import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.json.JSONException;
import questions.Question;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import quiz.QuizController;

import java.io.IOException;
import java.util.List;


public class Layout {
    private final List<Question> questions;
    private static int currQuestion = 0;
    private final Stage primaryStage;


    public Layout(List<Question> questions, Stage primaryStage) {
        this.questions = questions;
        this.primaryStage = primaryStage;

    }


    public BorderPane getLayout() {
        BorderPane layout = new BorderPane();

        Label label = new Label(questions.get(currQuestion).getPrompt());

        Button nextButton = new Button("Next");
        Button backButton = new Button("Back");
        Button submitButton = new Button("submit");

        HBox hbox = new HBox();
        hbox.getChildren().addAll(backButton, nextButton, submitButton);

        layout.setTop(label);
        layout.setCenter(LayoutHelper.getNodeFromQuestion(questions.get(currQuestion)));
        layout.setBottom(hbox);

        submitButton.setOnMouseClicked(this::handle);

        backButton.setDisable(true);
        nextButton.setOnMouseClicked(e -> {
            if (currQuestion < questions.size() - 1) {
                currQuestion++;
                layout.setCenter(LayoutHelper.getNodeFromQuestion(questions.get(currQuestion)));
                label.setText(questions.get(currQuestion).getPrompt());
            }

            if (currQuestion != 0) backButton.setDisable(false);
            if (currQuestion == questions.size() - 1) nextButton.setDisable(true);
        });


        backButton.setOnMouseClicked(e -> {
            if (currQuestion > 0) {
                currQuestion--;
                layout.setCenter(LayoutHelper.getNodeFromQuestion(questions.get(currQuestion)));
                label.setText(questions.get(currQuestion).getPrompt());
            }

            if (currQuestion == 0) backButton.setDisable(true);
            if (currQuestion != questions.size() - 1 && nextButton.isDisable()) nextButton.setDisable(false);
        });


        return layout;
    }

    private void handle(MouseEvent e) {
        try {
            if (QuizController.responses.size() == questions.size()) {
                if (ConfirmBox.display("Are you sure you want to submit?")) {
                    primaryStage.close();
                    Results.display();
                }

            } else if (ConfirmBox.display("Some answers are unfinished. Are sure you want to submit?")) {
                primaryStage.close();
                Results.display();
            }
            }
        catch (IOException | JSONException ioException) {
            ioException.printStackTrace();
            }
        }
}


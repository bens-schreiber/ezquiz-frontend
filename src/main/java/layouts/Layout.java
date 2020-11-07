package layouts;

import org.json.JSONException;
import questions.Question;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import quiz.QuizController;

import java.io.IOException;


public class Layout<submitButton> {
    private final Question question;

    public Layout(Question question) {
        this.question = question;
    }

    public BorderPane getLayout() {
        BorderPane layout = new BorderPane();

        Label label = new Label(question.getQuestion());

        Button nextButton = new Button("Next");
        Button backButton = new Button("Back");
        Button submitButton = new Button("submit");

        HBox hbox = new HBox();
        hbox.getChildren().addAll(backButton, nextButton, submitButton);

        layout.setTop(label);
        layout.setCenter(LayoutHelper.getNodeFromQuestion(question));
        layout.setBottom(hbox);

        submitButton.setOnMouseClicked(e -> {
            try {
                QuizController.checkAnswers();
            } catch (IOException | JSONException ioException) {
                ioException.printStackTrace();
            }
        });

        return layout;
    }


}


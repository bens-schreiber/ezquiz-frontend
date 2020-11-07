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
import java.util.List;


public class Layout {
    private final List<Question> question;
    private static int currQuestion = 0;


    public Layout(List<Question> question) {
        this.question = question;
    }

    public BorderPane getLayout() {
        BorderPane layout = new BorderPane();

        Label label = new Label(question.get(currQuestion).getQuestion());

        Button nextButton = new Button("Next");
        Button backButton = new Button("Back");
        Button submitButton = new Button("submit");

        HBox hbox = new HBox();
        hbox.getChildren().addAll(backButton, nextButton, submitButton);

        layout.setTop(label);
        layout.setCenter(LayoutHelper.getNodeFromQuestion(question.get(currQuestion)));
        layout.setBottom(hbox);

        submitButton.setOnMouseClicked(e -> {
            try {
                QuizController.checkAnswers();
            } catch (IOException | JSONException ioException) {
                ioException.printStackTrace();
            }
        });

        nextButton.setOnMouseClicked(e -> {
            if (currQuestion < question.size() - 1) {
                currQuestion++;
                layout.setCenter(LayoutHelper.getNodeFromQuestion(question.get(currQuestion)));
                label.setText(question.get(currQuestion).getQuestion());
                }
        });

        backButton.setOnMouseClicked(e -> {
            if (currQuestion > 0) {
                currQuestion--;
                layout.setCenter(LayoutHelper.getNodeFromQuestion(question.get(currQuestion)));
                label.setText(question.get(currQuestion).getQuestion());
            }
        });



        return layout;
    }


}


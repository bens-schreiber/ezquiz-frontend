package layouts;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONException;
import questions.Question;
import quiz.QuizController;

import java.io.IOException;
import java.util.List;


public class Results {
    public static void display() throws IOException, JSONException {
        List<Question> missedQuestions = QuizController.checkAnswers();
        int points = QuizController.responses.size() - missedQuestions.size();

        VBox vbox = new VBox();
        for (Question question : missedQuestions) {
            vbox.getChildren().add(new Label(question.getPrompt()));
        }

        Stage window = new Stage();
//        Label label = new Label(Integer.toString(points));
        Label label2 = new Label("Results: ");
        BorderPane layout = new BorderPane();
        layout.setTop(label2);
        layout.setCenter(vbox);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.show();


    }
}

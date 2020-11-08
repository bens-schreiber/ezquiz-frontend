package layouts;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.json.JSONException;
import quiz.QuizController;

import java.io.IOException;


public class Results {
    public static void display() throws IOException, JSONException {
        Stage window = new Stage();
        int points = QuizController.checkAnswers();
        Label label = new Label(Integer.toString(points));
        Label label2 = new Label("Results: ");
        BorderPane layout = new BorderPane();
        layout.setTop(label2);
        layout.setCenter(label);
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.show();

    }
}

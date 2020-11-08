package layouts.nodes;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import questions.Question;
import quiz.QuizController;
import java.util.List;

public class UserInput {
    public static VBox getNode(Question question) {

        TextField textField = new TextField();
        Label label = new Label("Answer:");

        textField.setOnAction(e -> {
            QuizController.responses.remove(question.getID());
            QuizController.addResponse(question.getID(), List.of(textField.getText()));
        });

        VBox vbox = new VBox();
        vbox.getChildren().addAll(label, textField);
        return vbox;
    }
}

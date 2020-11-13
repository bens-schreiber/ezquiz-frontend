package quiz.questions.nodes.questiontype;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import quiz.questions.Question;
import quiz.Quiz;

import java.util.List;

public class UserInput {
    public static VBox getNode(Question question) {

        TextField textField = new TextField();
        findPreviousAnswer(question, textField);
        textField.setMaxSize(115, 10);
        Label label = new Label("Answer:");

        textField.setOnKeyTyped(e -> {
            Quiz.responses.remove(question.getID());
            Quiz.addResponse(question.getID(), List.of(textField.getText()));
        });

        VBox vbox = new VBox();
        vbox.getChildren().addAll(label, textField);
        return vbox;
    }

    private static void findPreviousAnswer(Question question, TextField textField) {
        if (Quiz.responses.containsKey(question.getID())) {
            String prevAnswer = (Quiz.responses.get(question.getID())).get(0);
            textField.setText(prevAnswer);

        }
    }

}

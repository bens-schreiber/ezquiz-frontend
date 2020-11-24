package quiz.questions.nodes;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import quiz.questions.Question;

import java.util.Collections;


/**
 * Vbox containing the UserInput question node
 */

public class UserInput extends QuizNode {

    public UserInput(Question question) {

        super(question);
        this.node = createNode();

    }

    private VBox createNode() {

        TextField textField = new TextField();

        textField.setMaxSize(115, 10);

        Label label = new Label("Answer:");

        textField.setOnKeyTyped(e -> this.response = Collections.singletonList(textField.getText()));

        VBox vbox = new VBox();

        vbox.getChildren().addAll(label, textField);

        return vbox;

    }

}

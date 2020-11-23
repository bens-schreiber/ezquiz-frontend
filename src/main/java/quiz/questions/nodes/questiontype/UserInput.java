package quiz.questions.nodes.questiontype;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import quiz.questions.Question;
import quiz.QuizManager;

import java.util.List;

/**
 * Vbox containing the UserInput question node
 */

public class UserInput {
    public static VBox getNode() {

        TextField textField = new TextField();

        findPreviousAnswer(textField);

        textField.setMaxSize(115, 10);

        Label label = new Label("Answer:");


        textField.setOnKeyTyped(e -> {

            if (QuizManager.getResponses().size() > QuizManager.getCurrNum()) {
                QuizManager.removeResponse(QuizManager.getCurrNum());
            }

            QuizManager.addResponse(
                    QuizManager.getCurrNum(),
                    List.of(textField.getText()));

        });

        VBox vbox = new VBox();

        vbox.getChildren().addAll(label, textField);

        return vbox;

    }

    private static void findPreviousAnswer(TextField textField) {

        if (QuizManager.getCurrNum() < QuizManager.getResponses().size()) {
            if (!QuizManager.getResponses().get(QuizManager.getCurrNum()).isEmpty()) {

                String prevAnswer = (QuizManager.getResponses().get(QuizManager.getCurrNum())).get(0);

                textField.setText(prevAnswer);


            }
        }
    }

}

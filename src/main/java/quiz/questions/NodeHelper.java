package quiz.questions;

import quiz.questions.nodes.questiontype.*;
import javafx.scene.layout.VBox;

public class NodeHelper {

    public static VBox getNodeFromQuestion(Question question) {
        if (question.getType().equals("1")) {
            return MultipleChoice.getNode(question);
        }

        if (question.getType().equals("2")) {
            return TrueOrFalse.getNode(question);
        }

        if (question.getType().equals("3")) {
            return CheckBoxType.getNode(question);
        }

        if (question.getType().equals("4")) {
            return UserInput.getNode(question);
        }


        return null;
    }

}

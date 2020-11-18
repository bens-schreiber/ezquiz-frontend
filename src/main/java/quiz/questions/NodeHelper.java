package quiz.questions;

import quiz.questions.nodes.questiontype.*;
import javafx.scene.layout.VBox;

public class NodeHelper {

    public static VBox getNodeFromQuestion(Question question) {

        return switch (question.getType()) {

            case "1" -> MultipleChoice.getNode(question);

            case "2" -> TrueOrFalse.getNode(question);

            case "3" -> CheckBoxType.getNode(question);

            case "4" -> UserInput.getNode(question);

            default -> null;
        };
    }

}

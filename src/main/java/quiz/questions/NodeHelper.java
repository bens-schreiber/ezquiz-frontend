package quiz.questions;

import quiz.questions.nodes.questiontype.*;
import javafx.scene.layout.VBox;

/**
 * Contains method for getting a question node from a question objects type value
 */

public class NodeHelper {

    public static VBox getNodeFromQuestion(Question question) {

        return switch (question.getType()) {

            case "1" -> MultipleChoice.getNode();

            case "2" -> TrueOrFalse.getNode();

            case "3" -> CheckBoxType.getNode();

            case "4" -> UserInput.getNode();

            default -> null;

        };
    }

}

package quiz.questions;

import quiz.questions.nodes.*;

/**
 * Contains method for getting a question node from a question objects type value
 */

public class QuizNodeFactory {

    //factory method
    public static QuizNode getNodeFromQuestion(Question question) {

        return switch (question.getType()) {

            case "1" -> new MultipleChoice(question);

            case "2" -> new TrueOrFalse(question);

            case "3" -> new CheckBoxType(question);

            case "4" -> new UserInput(question);

            default -> null;

        };
    }

}

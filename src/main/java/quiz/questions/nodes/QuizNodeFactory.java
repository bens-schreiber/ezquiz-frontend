package quiz.questions.nodes;

import quiz.questions.Question;

/**
 * Contains method for getting a question node from a question objects type value
 */

public class QuizNodeFactory {

    //factory method
    public static QuizNode getNodeFromQuestion(Question question) {

        return switch (question.getType()) {

            case "multiple" -> new MultipleChoice(question);

            case "t_f" -> new TrueOrFalse(question);

            case "checkbox" -> new CheckBoxType(question);

            case "input" -> new UserInput(question);

            default -> null;

        };
    }

}

package quiz.questions.nodes;

import javafx.scene.layout.VBox;
import quiz.questions.Question;

import java.util.List;

public abstract class QuizNode {

    VBox node;
    Question question;
    List<String> response;

    public QuizNode(Question question) {
        this.question = question;
    }

    public VBox getNode() {
        return this.node;
    }

    public Question getQuestion() {
        return question;
    }

    public List<String> getResponse() {
        return response;
    }
}

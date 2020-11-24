package quiz.questions.nodes;

import javafx.scene.layout.VBox;
import quiz.questions.Question;

import java.util.List;

public abstract class QuizNode {

    protected VBox node;
    protected Question question;
    protected boolean correct;

    //Initiate response with a fake value if left empty
    protected List<String> response = List.of("null");

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

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public boolean isCorrect() {
        return correct;
    }
}

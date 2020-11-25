package quiz.questions.nodes;

import javafx.scene.Node;
import quiz.questions.Question;

import java.util.List;

/**
 * QuizNode class that keeps track of the question and response, as well as the javafx node
 */

public class QuizNode {

    private Node node;
    private final Question question;
    private boolean correct;

    //Initiate response with a fake value if left empty
    protected List<String> response = List.of("null");

    public QuizNode(Question question) {
        this.question = question;
    }

    /**
     * Getters
     */
    public Node getNode() {
        return this.node;
    }

    public Question getQuestion() {
        return question;
    }

    public List<String> getResponse() {
        return response;
    }


    /**
     * Setters
     */
    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    public void setNode(Node node) {
        this.node = node;
    }

    public boolean isCorrect() {
        return correct;
    }


}

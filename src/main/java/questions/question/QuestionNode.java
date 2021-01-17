package questions.question;

import javafx.scene.Node;
import questions.nodes.TypeNode;

import java.util.List;

/**
 * Container for all JavaFX information (type node) , on top of the text attributes of Question
 */
public class QuestionNode extends Question {

    //TypeNode that contains the question type, and user response to it.
    private final TypeNode node;
    private boolean correct;

    /**
     * Class constructor. Create a Question and assign JavaFX node.
     */
    public QuestionNode(Type type, Subject subject, List<String> options, String prompt, String directions, int id, TypeNode node) {

        super(type, subject, options, prompt, directions, id);
        super.shuffleOptions();

        this.node = node;

    }

    /**
     * Getters
     */
    public Node getNode() {
        return this.node.getNode();
    }

    public List<String> getResponse() {
        return this.node.getResponse();
    }

    public boolean isAnswered() {
        return node.isAnswered();
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

}




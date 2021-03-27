package com.benschreiber.question;

import com.benschreiber.question.fxnodes.TypeNode;
import javafx.scene.Node;
import javafx.scene.control.Tab;

import java.util.List;

/**
 * Container for all JavaFX information (type node) , on top of the text attributes of Question
 */
public class QuestionNode extends Question {

    //TypeNode that contains the classes.question type, and user response to it.
    private final TypeNode node;
    private boolean correct;

    /**
     * Class constructor. Create a Question and assign JavaFX node.
     */
    public QuestionNode(Type type, String prompt, String directions, int id, TypeNode node) {

        super(type, prompt, directions, id);

        this.node = node;

    }


    //For use in the quizzes tabWizard
    public Tab getAsTab() {
        Tab tab = new Tab();
        tab.setContent(node.getNode());
        return tab;
    }

    //For use in results
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




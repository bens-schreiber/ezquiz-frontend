package com.benschreiber.gui.fxobjs.questiondisplays;

import com.benschreiber.question.Question;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;


/**
 * Contains the JavaFX display node and response.
 */
public abstract class TypeNode {

    protected List<String> response;

    protected VBox node;

    protected String fontSize = "-fx-font-size: 15";

    protected TypeNode(String directions, String prompt) {

        VBox vBox = new VBox(15);

        Label questionDirections = new Label(directions);
        Label questionPrompt = new Label(prompt);
        questionDirections.setStyle("-fx-font-size: 20");
        questionPrompt.setStyle("-fx-font-size: 30");

        vBox.getChildren().addAll(questionDirections, questionPrompt);

        this.node = vBox;
    }

    public Node getNode() {
        return this.node;
    }

    public List<String> getResponse() {
        return this.response;
    }

    public boolean isAnswered() {
        return !(response == null) && !response.isEmpty();
    }

    public static class Factory {

        public static TypeNode typeNodeFromQuestion(Question question) {

            return switch (question.getType()) {

                case MULTIPLECHOICE -> new MultipleChoice(question.getOptions(), question.getDirections(), question.getPrompt());

                case TRUEORFALSE -> new TrueOrFalse(question.getDirections(), question.getPrompt());

                case CHECKBOX -> new CheckBoxNode(question.getOptions(), question.getDirections(), question.getPrompt());

                case WRITTEN -> new Written(question.getDirections(), question.getPrompt());
            };
        }
    }
}

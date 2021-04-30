package com.benschreiber.gui.fxobjs.qbuilderdisplays;

import com.benschreiber.question.Question;
import javafx.scene.Node;

import java.util.List;

public abstract class OptionNode {

    protected Node node;

    //Get the options provided
    public abstract List<String> getOptions();

    //Get the answer chosen on the classes.question
    public abstract List<String> getAnswer();

    public abstract boolean isAnswered();

    //Get the JavaFX node for displaying
    public Node getNode() {
        return this.node;
    }

    public static class Factory {
        public static OptionNode nodeFromType(Question.Type type) {
            return switch (type) {
                case MULTIPLECHOICE -> new MultipleChoice();
                case TRUEORFALSE -> new TrueOrFalse();
                case CHECKBOX -> new CheckBoxNode();
                case WRITTEN -> new Written();
            };
        }
    }
}

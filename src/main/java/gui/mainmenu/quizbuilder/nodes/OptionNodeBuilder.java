package gui.mainmenu.quizbuilder.nodes;

import question.Question;

public class OptionNodeBuilder {

    private Question.Type type;

    public OptionNodeBuilder() {
    }

    public OptionNodeBuilder type(Question.Type type) {
        this.type = type;
        return this;
    }

    public OptionNode build() {
        return switch (type) {
            case MULTIPLECHOICE -> new MultipleChoice();
            case TRUEORFALSE -> new TrueOrFalse();
            case CHECKBOX -> new CheckBoxNode();
            case WRITTEN -> new Written();
        };

    }
}

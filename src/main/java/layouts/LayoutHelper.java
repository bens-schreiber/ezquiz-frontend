package layouts;

import questions.Question;
import javafx.scene.layout.VBox;

class LayoutHelper {


    public static VBox getNodeFromQuestion(Question question) {
        if (question.getType().equals("1")) {
            return MultipleChoice.getNode(question);
        }

        return null;
    }
}

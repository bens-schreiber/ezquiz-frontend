package layouts;

import layouts.questiontype.*;
import questions.Question;
import javafx.scene.layout.VBox;

class LayoutHelper {

    public static VBox getNodeFromQuestion(Question question) {
        if (question.getType().equals("1")) {
            return MultipleChoice.getNode(question);
        }

        if (question.getType().equals("2")) {
            return TrueOrFalse.getNode(question);
        }

        if (question.getType().equals("3")) {
            return CheckBoxType.getNode(question);
        }

        if (question.getType().equals("4")) {
            return UserInput.getNode(question);
        }

//        if (question.getType().equals("5")) {
//            return DragAndDrop.getNode(question);
//        }


        return null;
    }

}

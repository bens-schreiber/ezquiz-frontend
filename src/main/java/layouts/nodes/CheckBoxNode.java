package layouts.nodes;

import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import questions.Question;
import quiz.QuizController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckBoxNode {
    public static VBox getNode(Question question) {

        CheckBox box1 = new CheckBox(question.getOptions().get(0));
        CheckBox box2 = new CheckBox(question.getOptions().get(1));
        CheckBox box3 = new CheckBox(question.getOptions().get(2));
        CheckBox box4 = new CheckBox(question.getOptions().get(3));

        for (CheckBox checkBox : Arrays.asList(box1, box2, box3, box4)) {

            checkBox.setOnMouseClicked(e -> {
                QuizController.responses.remove(question.getID());
                            QuizController.addResponse(question.getID(),
                                    handleOptions(List.of(box1, box2, box3, box4)));
            });

        }

        VBox vbox = new VBox();
        vbox.getChildren().addAll(box1, box2, box3, box4);
        return vbox;
    }

    private static List<String> handleOptions(List<CheckBox> boxes) {
        String response = "";

        for (CheckBox box : boxes) {
            if (box.isSelected())
                response += box.getText() + ",";
        }
        return (new ArrayList<>(Arrays.asList(response.split(","))));
    }
}

package layouts.nodes;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import questions.Question;
import quiz.QuizController;

import java.util.Arrays;
import java.util.List;

public class TrueOrFalse {
    public static VBox getNode(Question question) {
        RadioButton radio1 = new RadioButton("true");
        RadioButton radio2 = new RadioButton("false");

        ToggleGroup buttons = new ToggleGroup();
        for (RadioButton radioButton : Arrays.asList(radio1, radio2)) {
            radioButton.setToggleGroup(buttons);

            radioButton.setOnMouseClicked(e -> {
                QuizController.responses.remove(question.getID());
                QuizController.addResponse(question.getID(), List.of(radioButton.getText()));
            });
        }

        VBox vbox = new VBox();
        vbox.getChildren().addAll(radio1, radio2);
        return vbox;
    }

}

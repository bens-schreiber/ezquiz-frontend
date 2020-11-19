package quiz.questions.nodes.questiontype;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import quiz.questions.Question;
import quiz.QuizManager;

import java.util.Arrays;
import java.util.List;

public class TrueOrFalse {
    public static VBox getNode(Question question) {

        RadioButton radio1 = new RadioButton("true");

        RadioButton radio2 = new RadioButton("false");


        findPreviousAnswer(question, radio1, radio2);

        ToggleGroup buttons = new ToggleGroup();

        for (RadioButton radioButton : Arrays.asList(radio1, radio2)) {

            radioButton.setToggleGroup(buttons);

            radioButton.setOnMouseClicked(e -> {

                QuizManager.responses.remove(question.getID());

                QuizManager.addResponse(question.getID(), List.of(radioButton.getText()));

            });
        }


        VBox vbox = new VBox(15); //Set spacing to 15

        vbox.getChildren().addAll(radio1, radio2);

        return vbox;

    }

    private static void findPreviousAnswer(Question question, RadioButton radio1, RadioButton radio2) {
        if (QuizManager.responses.containsKey(question.getID())) {

            String prevAnswer = (QuizManager.responses.get(question.getID())).get(0);

            for (RadioButton radioButton : Arrays.asList(radio1, radio2)) {

                if (radioButton.getText().equals(prevAnswer)) {

                    radioButton.setSelected(true);

                    break;

                }
            }
        }
    }

}

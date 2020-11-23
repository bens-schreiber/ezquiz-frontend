package quiz.questions.nodes.questiontype;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import quiz.questions.Question;
import quiz.QuizManager;

import java.util.Arrays;
import java.util.List;

/**
 * VBox containing the TrueOrFalse question node
 */

public class TrueOrFalse {
    public static VBox getNode() {

        RadioButton radio1 = new RadioButton("true");

        RadioButton radio2 = new RadioButton("false");


        findPreviousAnswer(radio1, radio2);

        ToggleGroup buttons = new ToggleGroup();

        for (RadioButton radioButton : Arrays.asList(radio1, radio2)) {

            radioButton.setToggleGroup(buttons);

            radioButton.setOnMouseClicked(e -> {

                if (QuizManager.getResponses().size() > QuizManager.getCurrNum()) {
                    QuizManager.removeResponse(QuizManager.getCurrNum());
                }

                QuizManager.addResponse(
                        QuizManager.getCurrNum(),
                        List.of(radioButton.getText()));

            });
        }


        VBox vbox = new VBox(15); //Set spacing to 15

        vbox.getChildren().addAll(radio1, radio2);

        return vbox;

    }

    private static void findPreviousAnswer(RadioButton radio1, RadioButton radio2) {
        if (QuizManager.getCurrNum() < QuizManager.getResponses().size()) {
            if (!QuizManager.getResponses().get(QuizManager.getCurrNum()).isEmpty()) {

                String prevAnswer = (QuizManager.getResponses().get(QuizManager.getCurrNum())).get(0);

                for (RadioButton radioButton : Arrays.asList(radio1, radio2)) {

                    if (radioButton.getText().equals(prevAnswer)) {

                        radioButton.setSelected(true);

                        break;

                    }
                }
            }
        }
    }

}

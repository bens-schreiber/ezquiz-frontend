package quiz.questions.nodes.questiontype;

import quiz.questions.Question;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import quiz.QuizManager;

import java.util.Arrays;
import java.util.List;

/**
 * VBox containing the multiple choice question node
 */


public class MultipleChoice {
    public static VBox getNode() {

        Question question = QuizManager.getQuestion(QuizManager.getCurrNum());

        RadioButton radio1 = new RadioButton(question.getOptions().get(0));

        RadioButton radio2 = new RadioButton(question.getOptions().get(1));

        RadioButton radio3 = new RadioButton(question.getOptions().get(2));

        RadioButton radio4 = new RadioButton(question.getOptions().get(3));

        //Make answers not disappear when going back.
        findPreviousAnswer(radio1, radio2, radio3, radio4);

        ToggleGroup mChoice = new ToggleGroup(); //Make buttons only have 1 toggle at a time.

        for (RadioButton radioButton : Arrays.asList(radio1, radio2, radio3, radio4)) {

            radioButton.setToggleGroup(mChoice);

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

        vbox.getChildren().addAll(radio1, radio2, radio3, radio4);

        return vbox;

    }

    private static void findPreviousAnswer(RadioButton radio1, RadioButton radio2, RadioButton radio3, RadioButton radio4) {

        if (QuizManager.getCurrNum() < QuizManager.getResponses().size()) {
            if (!QuizManager.getResponses().get(QuizManager.getCurrNum()).isEmpty()) {

                String prevAnswer = (QuizManager.getResponses().get(QuizManager.getCurrNum())).get(0);

                for (RadioButton radioButton : Arrays.asList(radio1, radio2, radio3, radio4)) {

                    if (radioButton.getText().equals(prevAnswer)) {

                        radioButton.setSelected(true);

                        break;

                    }
                }
            }
        }
    }
}

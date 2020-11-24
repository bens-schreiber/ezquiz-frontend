package quiz.questions.nodes;

import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import quiz.QuizManager;
import quiz.questions.Question;
import quiz.questions.nodes.QuizNode;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * VBox containing the TrueOrFalse question node
 */

public class TrueOrFalse extends QuizNode {

    public TrueOrFalse(Question question) {

        super(question);
        this.node = createNode();

    }


    private VBox createNode() {

        RadioButton radio1 = new RadioButton("true");

        RadioButton radio2 = new RadioButton("false");

        ToggleGroup buttons = new ToggleGroup();

        for (RadioButton radioButton : Arrays.asList(radio1, radio2)) {

            radioButton.setToggleGroup(buttons);

            radioButton.setOnMouseClicked(e -> this.response = Collections.singletonList(radioButton.getText()));
        }


        VBox vbox = new VBox(15); //Set spacing to 15

        vbox.getChildren().addAll(radio1, radio2);

        return vbox;

    }

//    private static void findPreviousAnswer(RadioButton radio1, RadioButton radio2) {
//        if (QuizManager.getCurrNum() < QuizManager.getResponses().size()) {
//            if (!QuizManager.getResponses().get(QuizManager.getCurrNum()).isEmpty()) {
//
//                String prevAnswer = (QuizManager.getResponses().get(QuizManager.getCurrNum())).get(0);
//
//                for (RadioButton radioButton : Arrays.asList(radio1, radio2)) {
//
//                    if (radioButton.getText().equals(prevAnswer)) {
//
//                        radioButton.setSelected(true);
//
//                        break;
//
//                    }
//                }
//            }
//        }
//    }

}

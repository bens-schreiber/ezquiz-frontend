package quiz.questions.nodes;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import quiz.questions.Question;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;
import quiz.QuizManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * VBox containing the multiple choice question node
 */


public class MultipleChoice extends QuizNode {

    public MultipleChoice(Question question) {
        super(question);
        this.node = createNode();
    }

    private VBox createNode() {

        int buttonAmount = question.getOptions().size();

        RadioButton[] radioButtons = new RadioButton[buttonAmount];

        ToggleGroup mChoice = new ToggleGroup();

        for (int i = 0; i < buttonAmount; i++) {

            RadioButton radioButton = new RadioButton(question.getOptions().get(i));

            radioButtons[i] = radioButton;

            radioButton.setToggleGroup(mChoice);

            radioButton.setOnMouseClicked(e -> this.response = Collections.singletonList(radioButton.getText()));

        }

        VBox vbox = new VBox(15); //Set spacing to 15

        vbox.getChildren().addAll(radioButtons);

        return vbox;

    }
}

//    private static void findPreviousAnswer(RadioButton radio1, RadioButton radio2, RadioButton radio3, RadioButton radio4) {
//
//        if (QuizManager.getCurrNum() < QuizManager.getResponses().size()) {
//            if (!QuizManager.getResponses().get(QuizManager.getCurrNum()).isEmpty()) {
//
//                String prevAnswer = (QuizManager.getResponses().get(QuizManager.getCurrNum())).get(0);
//
//                for (RadioButton radioButton : Arrays.asList(radio1, radio2, radio3, radio4)) {
//
//                    if (radioButton.getText().equals(prevAnswer)) {
//
//                        radioButton.setSelected(true);
//
//                        break;
//
//                    }

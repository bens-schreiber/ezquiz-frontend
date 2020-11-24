package quiz.questions.nodes;

import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import quiz.questions.Question;
import quiz.QuizManager;

import java.util.Arrays;
import java.util.List;

/**
 * Question
 */

public class CheckBoxType extends QuizNode {

    public CheckBoxType(Question question) {
        super(question);
        this.node = createNode();
    }


    private VBox createNode() {

        int boxAmount = question.getOptions().size();

        CheckBox[] boxes = new CheckBox[boxAmount];

        for (int i = 0; i < boxAmount; i++) {

            CheckBox checkBox = new CheckBox(question.getOptions().get(i));

            boxes[i] = checkBox;

            checkBox.setOnMouseClicked(e -> {

                StringBuilder stringBuilder = new StringBuilder();

                for (CheckBox box : boxes) {

                    if (box.isSelected())

                        stringBuilder.append(box.getText()).append(",");

                }

                this.response = Arrays.asList(stringBuilder.toString().split(","));

            });
        }

        VBox vbox = new VBox(15); //Set spacing to 15

        vbox.getChildren().addAll(boxes);

        return vbox;

    }


//    private static void findPreviousAnswer(CheckBox checkBox1, CheckBox checkBox2, CheckBox checkBox3, CheckBox checkBox4) {
//
//        if (QuizManager.getCurrNum() < QuizManager.getResponses().size()) {
//            if (!QuizManager.getResponses().get(QuizManager.getCurrNum()).isEmpty()) {
//
//                List<String> prevAnswer = (QuizManager.getResponses().get(QuizManager.getCurrNum()));
//
//                for (CheckBox checkBox : Arrays.asList(checkBox1, checkBox2, checkBox3, checkBox4)) {
//
//                    if (prevAnswer.contains(checkBox.getText())) checkBox.setSelected(true);
//
//                }
//            }
//        }
//    }
}

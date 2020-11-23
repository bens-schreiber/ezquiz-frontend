package quiz.questions.nodes.questiontype;

import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import quiz.questions.Question;
import quiz.QuizManager;

import java.util.Arrays;
import java.util.List;

/**
 * Question
 */

public class CheckBoxType {

    public static VBox getNode() {

        Question question = QuizManager.getQuestion(QuizManager.getCurrNum());

        CheckBox box1 = new CheckBox(question.getOptions().get(0));

        CheckBox box2 = new CheckBox(question.getOptions().get(1));

        CheckBox box3 = new CheckBox(question.getOptions().get(2));

        CheckBox box4 = new CheckBox(question.getOptions().get(3));

        findPreviousAnswer(box1, box2, box3, box4);

        for (CheckBox checkBox : Arrays.asList(box1, box2, box3, box4)) {

            checkBox.setOnMouseClicked(e -> {

                if (QuizManager.getResponses().size() > QuizManager.getCurrNum()) {
                    QuizManager.removeResponse(QuizManager.getCurrNum());
                }

                QuizManager.addResponse(QuizManager.getCurrNum(),

                        handleOptions(List.of(box1, box2, box3, box4)));

            });

        }


        VBox vbox = new VBox(15); //Set spacing to 15

        vbox.getChildren().addAll(box1, box2, box3, box4);

        return vbox;

    }

    private static List<String> handleOptions(List<CheckBox> boxes) {

        StringBuilder response = new StringBuilder();

        for (CheckBox box : boxes) {

            if (box.isSelected())

                response.append(box.getText()).append(",");

        }

        return Arrays.asList(response.toString().split(","));

    }

    private static void findPreviousAnswer(CheckBox checkBox1, CheckBox checkBox2, CheckBox checkBox3, CheckBox checkBox4) {

        if (QuizManager.getCurrNum() < QuizManager.getResponses().size()) {
            if (!QuizManager.getResponses().get(QuizManager.getCurrNum()).isEmpty()) {

                List<String> prevAnswer = (QuizManager.getResponses().get(QuizManager.getCurrNum()));

                for (CheckBox checkBox : Arrays.asList(checkBox1, checkBox2, checkBox3, checkBox4)) {

                    if (prevAnswer.contains(checkBox.getText())) checkBox.setSelected(true);

                }
            }
        }
    }
}

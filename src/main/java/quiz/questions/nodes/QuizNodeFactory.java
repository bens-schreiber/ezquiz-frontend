package quiz.questions.nodes;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import quiz.questions.Question;

import java.util.Arrays;
import java.util.Collections;

/**
 * Contains method for getting a quiz node from a question objects type value
 */

public class QuizNodeFactory {

    //factory method
    public static QuizNode getNodeFromQuestion(Question question) {

        QuizNode quizNode = new QuizNode(question);

        return switch (question.getType()) {

            case "multiple" -> setMultipleChoiceNode(quizNode);

            case "t_f" -> setTrueOrFalseNode(quizNode);

            case "checkbox" -> setCheckBoxNode(quizNode);

            case "input" -> setUserInputNode(quizNode);

            default -> null;

        };
    }

    private static QuizNode setMultipleChoiceNode(QuizNode quizNode) {
        Question question = quizNode.getQuestion();
        int buttonAmount = question.getOptions().size();

        RadioButton[] radioButtons = new RadioButton[buttonAmount];

        ToggleGroup mChoice = new ToggleGroup();

        for (int i = 0; i < buttonAmount; i++) {

            RadioButton radioButton = new RadioButton(question.getOptions().get(i));

            radioButtons[i] = radioButton;

            radioButton.setToggleGroup(mChoice);

            radioButton.setOnMouseClicked(e -> quizNode.response = Collections.singletonList(radioButton.getText()));

        }

        VBox vbox = new VBox(15); //Set spacing to 15

        vbox.getChildren().addAll(radioButtons);

        quizNode.setNode(vbox);
        return quizNode;
    }

    private static QuizNode setCheckBoxNode(QuizNode quizNode) {
        Question question = quizNode.getQuestion();
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

                quizNode.response = Arrays.asList(stringBuilder.toString().split(","));

            });
        }

        VBox vbox = new VBox(15); //Set spacing to 15

        vbox.getChildren().addAll(boxes);

        quizNode.setNode(vbox);
        return quizNode;
    }

    private static QuizNode setTrueOrFalseNode(QuizNode quizNode) {

        RadioButton radio1 = new RadioButton("true");

        RadioButton radio2 = new RadioButton("false");

        ToggleGroup buttons = new ToggleGroup();

        for (RadioButton radioButton : Arrays.asList(radio1, radio2)) {

            radioButton.setToggleGroup(buttons);

            radioButton.setOnMouseClicked(e -> quizNode.response = Collections.singletonList(radioButton.getText()));
        }


        VBox vbox = new VBox(15); //Set spacing to 15

        vbox.getChildren().addAll(radio1, radio2);

        quizNode.setNode(vbox);
        return quizNode;
    }

    private static QuizNode setUserInputNode(QuizNode quizNode) {
        TextField textField = new TextField();

        textField.setMaxSize(115, 10);

        Label label = new Label("Answer:");

        textField.setOnKeyTyped(e -> quizNode.response = Collections.singletonList(textField.getText()));

        VBox vbox = new VBox();

        vbox.getChildren().addAll(label, textField);

        quizNode.setNode(vbox);
        return quizNode;
    }

}

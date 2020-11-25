package quiz.questions.nodes;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import quiz.questions.Question;

import java.util.Arrays;
import java.util.Collections;

public class QuizNodeBuilder {

    private final Question question;
    private QuizNode quizNode;
    private Node node;

    public QuizNodeBuilder(Question question) {
        this.question = question;
    }

    public QuizNodeBuilder create() {
        this.quizNode = new QuizNode(question);
        return this;
    }

    public QuizNodeBuilder setNode() {
        return switch (question.getType()) {

            case "multiple" -> this.setMultipleChoiceNode();

            case "t_f" -> this.setTrueOrFalseNode();

            case "checkbox" -> this.setCheckBoxNode();

            case "input" -> this.setUserInputNode();

            default -> null;

        };
    }

    private QuizNodeBuilder setMultipleChoiceNode() {
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

        this.node = vbox;
        return this;
    }

    private QuizNodeBuilder setCheckBoxNode() {
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

        this.node = vbox;
        return this;
    }

    private QuizNodeBuilder setTrueOrFalseNode() {

        RadioButton radio1 = new RadioButton("true");

        RadioButton radio2 = new RadioButton("false");

        ToggleGroup buttons = new ToggleGroup();

        for (RadioButton radioButton : Arrays.asList(radio1, radio2)) {

            radioButton.setToggleGroup(buttons);

            radioButton.setOnMouseClicked(e -> quizNode.response = Collections.singletonList(radioButton.getText()));
        }


        VBox vbox = new VBox(15); //Set spacing to 15

        vbox.getChildren().addAll(radio1, radio2);

        this.node = vbox;
        return this;
    }

    private QuizNodeBuilder setUserInputNode() {
        TextField textField = new TextField();

        textField.setMaxSize(115, 10);

        Label label = new Label("Answer:");

        textField.setOnKeyTyped(e -> quizNode.response = Collections.singletonList(textField.getText()));

        VBox vbox = new VBox();

        vbox.getChildren().addAll(label, textField);

        this.node = vbox;
        return this;
    }

    public QuizNode build() {
        quizNode.setNode(node);
        return quizNode;
    }

}

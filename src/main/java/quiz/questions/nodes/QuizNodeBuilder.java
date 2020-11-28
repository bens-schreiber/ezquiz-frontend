package quiz.questions.nodes;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import quiz.questions.Question;

import java.util.Arrays;
import java.util.Collections;

/**
 * QuizNode builder
 * returns a QuizNode
 */

public class QuizNodeBuilder {

    private final QuizNode quizNode;
    private Node node;

    //Initiate
    public QuizNodeBuilder(Question question) {
        this.quizNode = new QuizNode(question);
    }

    public QuizNodeBuilder setMultipleChoiceNode() {

        //Define the question
        Question question = quizNode.getQuestion();

        //Find how many RadioButtons are needed
        int buttonAmount = question.getOptions().size();

        //Create an array of RadioButtons
        RadioButton[] radioButtons = new RadioButton[buttonAmount];

        //Create a toggle group so only one button can be selected at a time
        ToggleGroup mChoice = new ToggleGroup();

        //Iterate through the radioButton array, init buttons
        for (int i = 0; i < buttonAmount; i++) {

            RadioButton radioButton = new RadioButton(question.getOptions().get(i));

            radioButtons[i] = radioButton;

            radioButton.setToggleGroup(mChoice);

            //On mouse clicked, send button text to response, overwrite old
            radioButton.setOnMouseClicked(e -> quizNode.setResponse(Collections.singletonList(radioButton.getText())));

        }

        //Set spacing to 15
        VBox vbox = new VBox(15);
        vbox.getChildren().addAll(radioButtons);
        this.node = vbox;
        return this;
    }

    public QuizNodeBuilder setCheckBoxNode() {

        //Define the question
        Question question = quizNode.getQuestion();

        //Get box amount from options size
        int boxAmount = question.getOptions().size();

        //Create an array of checkboxes
        CheckBox[] boxes = new CheckBox[boxAmount];

        //Iterate through the array and init each button
        for (int i = 0; i < boxAmount; i++) {

            CheckBox checkBox = new CheckBox(question.getOptions().get(i));

            boxes[i] = checkBox;

            //On mouse clicked, record response to responses
            checkBox.setOnMouseClicked(e -> {

                StringBuilder stringBuilder = new StringBuilder();
                for (CheckBox box : boxes) {
                    if (box.isSelected())
                        stringBuilder.append(box.getText()).append(",");
                }

                quizNode.setResponse(Arrays.asList(stringBuilder.toString().split(",")));

            });
        }

        //Set spacing to 15
        VBox vbox = new VBox(15);
        vbox.getChildren().addAll(boxes);

        this.node = vbox;
        return this;
    }

    public QuizNodeBuilder setTrueOrFalseNode() {

        //Establish options
        RadioButton radio1 = new RadioButton("true");
        RadioButton radio2 = new RadioButton("false");

        //Create toggleGroup to make only one button at a time selectable
        ToggleGroup buttons = new ToggleGroup();

        //Iterate through buttons and init functionality
        for (RadioButton radioButton : Arrays.asList(radio1, radio2)) {

            radioButton.setToggleGroup(buttons);

            radioButton.setOnMouseClicked(e -> quizNode.setResponse(Collections.singletonList(radioButton.getText())));
        }

        //Set spacing to 15
        VBox vbox = new VBox(15);
        vbox.getChildren().addAll(radio1, radio2);

        this.node = vbox;
        return this;
    }

    //Sets user input node
    public QuizNodeBuilder setUserInputNode() {

        TextField textField = new TextField();
        textField.setMaxSize(115, 10);
        Label label = new Label("Answer:");

        //Set functionality
        textField.setOnKeyTyped(e -> quizNode.setResponse(Collections.singletonList(textField.getText())));

        VBox vbox = new VBox();
        vbox.getChildren().addAll(label, textField);

        this.node = vbox;
        return this;
    }

    //Build method
    public QuizNode build() {
        quizNode.setNode(node);
        return quizNode;
    }

}

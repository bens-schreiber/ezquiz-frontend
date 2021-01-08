package quiz.questions.nodes;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import quiz.questions.Question;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Container for all information regarding the Question
 */
public class QuizNode implements TypeNode {

    //TypeNode that contains the question type, and user response to it.
    private final TypeNode node;

    private final Question question;
    private boolean correct;

    /**
     * Class constructor.
     */
    public QuizNode(Question question) {

        this.question = question;

        //Determine what Type of question
        this.node = switch (question.getType()) {
            case MULTIPLECHOICE -> new MultipleChoice(question);
            case TRUEORFALSE -> new TrueOrFalse();
            case CHECKBOX -> new CheckBoxNode(question);
            case WRITTEN -> new Written();
        };

    }

    /**
     * Getters
     */
    public Node getNode() {
        return this.node.getNode();
    }

    public List<String> getResponse() {
        return this.node.getResponse();
    }

    public boolean isAnswered() {
        return node.isAnswered();
    }

    public Question getQuestion() {
        return question;
    }


    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}

/**
 * Helper classes that records the response of the input to the corresponding Type node.
 */
class Written implements TypeNode {

    private List<String> response = Collections.emptyList();
    private final Node writtenNode;

    public Written() {

        TextField textField = new TextField();
        textField.setMaxSize(115, 10);
        Label label = new Label("Answer:");

        //Set functionality
        textField.setOnKeyTyped(e -> this.response = Collections.singletonList(textField.getText()));

        VBox vbox = new VBox(15);
        vbox.getChildren().addAll(label, textField);
        this.writtenNode = vbox;

    }

    public Node getNode() {
        return this.writtenNode;
    }

    public List<String> getResponse() {
        return this.response;
    }

    public boolean isAnswered() {
        return !response.isEmpty();
    }
}

class TrueOrFalse implements TypeNode {

    private List<String> response = Collections.emptyList();
    private final Node trueOrFalseNode;

    public TrueOrFalse() {

        //Establish options
        RadioButton radio1 = new RadioButton("true");
        RadioButton radio2 = new RadioButton("false");

        //Create toggleGroup to make only one button at a time selectable
        ToggleGroup buttons = new ToggleGroup();

        //Iterate through buttons and init functionality
        for (RadioButton radioButton : Arrays.asList(radio1, radio2)) {

            radioButton.setToggleGroup(buttons);
            radioButton.setOnMouseClicked(e -> this.response = Collections.singletonList(radioButton.getText()));
        }

        //Set spacing to 15
        VBox vbox = new VBox(15);
        vbox.getChildren().addAll(radio1, radio2);
        this.trueOrFalseNode = vbox;
    }


    public Node getNode() {
        return this.trueOrFalseNode;
    }

    public List<String> getResponse() {
        return this.response;
    }

    public boolean isAnswered() {
        return !response.isEmpty();
    }


}

class MultipleChoice implements TypeNode {

    private List<String> response = Collections.emptyList();
    private final Node multipleChoiceQuestionNode;

    public MultipleChoice(Question question) {

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
            radioButton.setOnMouseClicked(e -> this.response = Collections.singletonList(radioButton.getText()));

        }

        //Set spacing to 15
        VBox vbox = new VBox(15);
        vbox.getChildren().addAll(radioButtons);
        this.multipleChoiceQuestionNode = vbox;
    }

    public Node getNode() {
        return multipleChoiceQuestionNode;
    }

    public List<String> getResponse() {
        return this.response;
    }

    public boolean isAnswered() {
        return !response.isEmpty();
    }
}

class CheckBoxNode implements TypeNode {

    private List<String> response = Collections.emptyList();
    private final Node checkBoxNode;

    public CheckBoxNode(Question question) {
        //Get box amount from options size
        int boxAmount = question.getOptions().size();

        //Create an array of checkboxes
        CheckBox[] boxes = new javafx.scene.control.CheckBox[boxAmount];

        //Iterate through the array and init each button
        for (int i = 0; i < boxAmount; i++) {

            javafx.scene.control.CheckBox checkBox = new javafx.scene.control.CheckBox(question.getOptions().get(i));

            boxes[i] = checkBox;

            //On mouse clicked, record response to responses
            checkBox.setOnMouseClicked(e -> {

                StringBuilder stringBuilder = new StringBuilder();
                for (javafx.scene.control.CheckBox box : boxes) {
                    if (box.isSelected())
                        stringBuilder.append(box.getText()).append(",");
                }
                this.response = Arrays.asList(stringBuilder.toString().split(","));
            });
        }

        VBox vbox = new VBox(15);
        vbox.getChildren().addAll(boxes);
        checkBoxNode = vbox;
    }

    public Node getNode() {
        return this.checkBoxNode;
    }

    public List<String> getResponse() {
        return this.response;
    }

    public boolean isAnswered() {
        return !response.isEmpty();
    }
}




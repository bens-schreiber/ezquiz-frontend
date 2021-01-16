package quiz.question.nodes;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import quiz.question.Question;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Container for all information regarding the Question
 */
public class QuestionNode {

    //TypeNode that contains the question type, and user response to it.
    private final TypeNode node;

    private final Question question;

    private boolean correct;

    /**
     * Class constructor.
     */
    public QuestionNode(Question question) {

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


    /**
     * Helper classes that records the response of the input to the corresponding Type node.
     */
    private static class Written extends TypeNode {

        public Written() {

            TextField textField = new TextField();
            textField.setMaxSize(115, 10);
            Label label = new Label("Answer:");

            //Set functionality
            textField.setOnKeyTyped(e -> this.response = Collections.singletonList(textField.getText()));

            VBox vbox = new VBox(15);
            vbox.getChildren().addAll(label, textField);

            this.node = vbox;

        }
    }

    private static class TrueOrFalse extends TypeNode {

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

            this.node = vbox;
        }
    }

    private static class MultipleChoice extends TypeNode {

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

            this.node = vbox;
        }
    }

    private static class CheckBoxNode extends TypeNode {

        public CheckBoxNode(Question question) {
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

                    //Make a new linked list everytime so we don't need to track removing.
                    LinkedList<String> list = new LinkedList<>();
                    for (CheckBox box : boxes) {
                        if (box.isSelected())
                            list.add(box.getText());
                    }
                    this.response = list;
                });
            }

            VBox vbox = new VBox(15);
            vbox.getChildren().addAll(boxes);

            this.node = vbox;
        }
    }
}




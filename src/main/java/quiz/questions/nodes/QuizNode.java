package quiz.questions.nodes;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import quiz.questions.Question;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Contains javaFX Node to display a Question with its appropriate type.
 * Records responses to questions.
 * Flagged as correct or incorrect on QuizManager.checkAnswers().
 */
public class QuizNode {

    private final Node node;
    private final Question question;
    private boolean correct;

    //Initiate response with a fake value if left empty
    private List<String> response = List.of("null");

    /**
     * Class constructor.
     */
    public QuizNode(Question question) {

        this.question = question;

        this.node = getTypeNode();

    }

    /**
     * @return question type node correlating to questionType.
     */
    private Node getTypeNode() {
        switch (this.getQuestion().getType()) {

            case "multiple" -> {

                //Define the question
                Question question = this.getQuestion();

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
                    radioButton.setOnMouseClicked(e -> this.setResponse(Collections.singletonList(radioButton.getText())));

                }

                //Set spacing to 15
                VBox vbox = new VBox(15);
                vbox.getChildren().addAll(radioButtons);
                return vbox;
            }


            case "t_f" -> {

                //Establish options
                RadioButton radio1 = new RadioButton("true");
                RadioButton radio2 = new RadioButton("false");

                //Create toggleGroup to make only one button at a time selectable
                ToggleGroup buttons = new ToggleGroup();

                //Iterate through buttons and init functionality
                for (RadioButton radioButton : Arrays.asList(radio1, radio2)) {

                    radioButton.setToggleGroup(buttons);
                    radioButton.setOnMouseClicked(e -> this.setResponse(Collections.singletonList(radioButton.getText())));
                }

                //Set spacing to 15
                VBox vbox = new VBox(15);
                vbox.getChildren().addAll(radio1, radio2);
                return vbox;
            }


            case "checkbox" -> {

                //Define the question
                Question question = this.getQuestion();

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
                        this.setResponse(Arrays.asList(stringBuilder.toString().split(",")));
                    });
                }

                VBox vbox = new VBox(15);
                vbox.getChildren().addAll(boxes);
                return vbox;
            }

            case "input" -> {

                TextField textField = new TextField();
                textField.setMaxSize(115, 10);
                Label label = new Label("Answer:");

                //Set functionality
                textField.setOnKeyTyped(e -> this.setResponse(Collections.singletonList(textField.getText())));

                VBox vbox = new VBox(15);
                vbox.getChildren().addAll(label, textField);
                return vbox;

            }

        }
        return null;

    }

    /**
     * Getters
     */
    public Node getNode() {
        return this.node;
    }

    public Question getQuestion() {
        return question;
    }

    public List<String> getResponse() {
        return response;
    }

    public boolean isCorrect() {
        return correct;
    }


    /**
     * Setters
     */

    public void setResponse(List<String> response) {
        this.response = response;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
package questions.nodes;

import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

import java.util.Collections;
import java.util.List;

public class MultipleChoice implements TypeNode {

    private String response;

    private final Node node;

    public MultipleChoice(List<String> options) {

        //Find how many RadioButtons are needed
        int buttonAmount = options.size();

        //Create an array of RadioButtons
        RadioButton[] radioButtons = new RadioButton[buttonAmount];

        //Create a toggle group so only one button can be selected at a time
        ToggleGroup mChoice = new ToggleGroup();

        //Iterate through the radioButton array, init buttons
        for (int i = 0; i < buttonAmount; i++) {

            RadioButton radioButton = new RadioButton(options.get(i));

            radioButtons[i] = radioButton;

            radioButton.setToggleGroup(mChoice);

            //On mouse clicked, send button text to response, overwrite old
            radioButton.setOnMouseClicked(e -> this.response = radioButton.getText());

        }

        //Set spacing to 15
        VBox vbox = new VBox(15);
        vbox.getChildren().addAll(radioButtons);

        this.node = vbox;
    }

    @Override
    public Node getNode() {
        return this.node;
    }

    @Override
    public List<String> getResponse() {
        return Collections.singletonList(this.response);
    }

    @Override
    public boolean isAnswered() {
        return !(response == null) && !response.isEmpty();
    }

}

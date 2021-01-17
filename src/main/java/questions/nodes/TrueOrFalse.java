package questions.nodes;

import javafx.scene.Node;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.VBox;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TrueOrFalse implements TypeNode {

    private List<String> response = Collections.emptyList();

    private final Node node;

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

    @Override
    public Node getNode() {
        return this.node;
    }

    @Override
    public List<String> getResponse() {
        return this.response;
    }

    @Override
    public boolean isAnswered() {
        return !response.isEmpty();
    }

}

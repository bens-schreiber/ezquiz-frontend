package questions.nodes;

import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class CheckBoxNode implements TypeNode {

    private List<String> response = Collections.emptyList();

    private final Node node;

    public CheckBoxNode(List<String> options) {
        //Get box amount from options size
        int boxAmount = options.size();

        //Create an array of checkboxes
        CheckBox[] boxes = new CheckBox[boxAmount];

        //Iterate through the array and init each button
        for (int i = 0; i < boxAmount; i++) {

            CheckBox checkBox = new CheckBox(options.get(i));

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

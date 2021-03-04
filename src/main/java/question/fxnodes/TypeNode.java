package question.fxnodes;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.util.List;


/**
 * Contains the JavaFX display node and response.
 */
public class TypeNode {

    protected List<String> response;

    protected VBox node;

    protected TypeNode(String directions, String prompt) {

        VBox vBox = new VBox(15);

        vBox.getChildren().addAll(new Label(directions), new Label(prompt));

        this.node = vBox;
    }

    public Node getNode() {
        return this.node;
    }

    public List<String> getResponse() {
        return this.response;
    }

    public boolean isAnswered() {
        return !(response == null) && !response.isEmpty();
    }
}

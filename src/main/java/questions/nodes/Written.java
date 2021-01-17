package questions.nodes;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.Collections;
import java.util.List;

public class Written implements TypeNode {

    private List<String> response = Collections.emptyList();

    private final Node node;

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

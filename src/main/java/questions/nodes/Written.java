package questions.nodes;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.util.Collections;
import java.util.List;

public class Written implements TypeNode {

    private String response;

    private final Node node;

    {

        TextField textField = new TextField();
        textField.setMaxSize(115, 10);
        Label label = new Label("Answer:");

        //Set functionality
        textField.setOnKeyTyped(e -> this.response = textField.getText());

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
        return Collections.singletonList(this.response);
    }

    @Override
    public boolean isAnswered() {
        return !(response == null) && !response.isEmpty();
    }
}

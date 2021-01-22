package questions.nodes;

import javafx.scene.Node;

import java.util.List;

abstract public class TypeNode {

    protected List<String> response;

    protected Node node;

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

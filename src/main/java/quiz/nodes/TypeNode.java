package quiz.nodes;

import javafx.scene.Node;

import java.util.Collections;
import java.util.List;

abstract class TypeNode {

    protected List<String> response = Collections.emptyList();

    protected Node node;

    public Node getNode() {
        return node;
    }

    public List<String> getResponse() {
        return this.response;
    }

    public boolean isAnswered() {
        return !response.isEmpty();
    }

}

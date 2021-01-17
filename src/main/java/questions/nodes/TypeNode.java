package questions.nodes;

import javafx.scene.Node;

import java.util.List;

public interface TypeNode {

    Node getNode();

    List<String> getResponse();

    boolean isAnswered();

}

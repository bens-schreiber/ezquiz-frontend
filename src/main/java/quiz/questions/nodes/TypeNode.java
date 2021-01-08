package quiz.questions.nodes;

import javafx.scene.Node;

import java.util.List;

interface TypeNode {

    Node getNode();

    List<String> getResponse();

    boolean isAnswered();

}
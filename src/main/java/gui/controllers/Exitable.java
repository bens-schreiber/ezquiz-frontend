package gui.controllers;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public interface Exitable {
    default void exit(MouseEvent mouseEvent) {

        Node source = (Node) mouseEvent.getSource();

        Stage stage = (Stage) source.getScene().getWindow();

        stage.close();
    }
}

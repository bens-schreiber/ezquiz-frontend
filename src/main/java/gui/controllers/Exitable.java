package gui.controllers;

import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;


public interface Exitable {

    /**
     * Method to exit specific stage but keep program running
     */

    default void exit(MouseEvent mouseEvent) {

        Node source = (Node) mouseEvent.getSource();

        Stage stage = (Stage) source.getScene().getWindow();

        stage.close();
    }
}

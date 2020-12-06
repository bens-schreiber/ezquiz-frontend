package gui.popups;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Displays errors to user.
 */

public class ErrorBox {

    /**
     * @param fatal if the program should be terminated.
     */
    public static void display(String error, boolean fatal) {
//todo: make this an fxml scene
        //Establish the error scene
        VBox vbox = new VBox(10);
        vbox.getChildren().add(new Label(error));
        Button button = new Button("Close");
        vbox.getChildren().add(button);
        vbox.setAlignment(Pos.CENTER);

        //Set stage
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);

        if (fatal) {
            stage.setTitle("A fatal error occurred.");
            button.setOnAction(e -> Platform.exit());


        } else {
            stage.setTitle("An error occurred.");
            button.setOnAction(e -> stage.close());
        }

        stage.setHeight(100);
        stage.setWidth(200);
        Scene scene = new Scene(vbox);
        scene.getStylesheets().add("css/standard_button.css");
        stage.setScene(scene);
        stage.showAndWait();
    }
}

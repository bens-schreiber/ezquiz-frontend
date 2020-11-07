package layouts;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.json.JSONException;
import quiz.QuizController;

import java.io.IOException;


public class AlertBox {
    public static void display(String alertMessage) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Alert");
        window.setMinWidth(250);

        Label label = new Label();
        label.setText(alertMessage);

        Button denyButton = new Button("No");
        Button confirmButton = new Button("Yes");
        denyButton.setOnMouseClicked(e -> window.close());
        confirmButton.setOnMouseClicked(e -> {
            try {
                QuizController.checkAnswers();
            } catch (IOException | JSONException ioException) {
                ioException.printStackTrace();
            }
            window.close();
        });


        BorderPane layout = new BorderPane();
        layout.setTop(label);
        HBox hbox = new HBox(10);
        hbox.getChildren().addAll(confirmButton, denyButton);
        hbox.setAlignment(Pos.CENTER);
        layout.setCenter(hbox);

        Scene scene = new Scene(layout);
        window.initStyle(StageStyle.UNDECORATED);
        window.setScene(scene);
        window.showAndWait();

    }
}

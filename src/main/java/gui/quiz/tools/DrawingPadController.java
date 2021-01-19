package gui.quiz.tools;

import etc.Constants;
import gui.quiz.QuizController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

/**
 * Provides methods for ActionEvents on DrawingPad stage.
 */
public class DrawingPadController implements Initializable {

    @FXML
    private CheckBox eraser;

    @FXML
    private TextField brushSize;

    @FXML
    private ColorPicker colorPicker;

    @FXML
    private Button clearButton;

    //Make a Stage variable because these are their own PopupStage and must be closed from inside
    private static Stage stage;

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        DrawingPadController.stage = stage;
    }


    /**
     * Initial run method.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        colorPicker.setOnAction(e -> QuizController.changeColor(colorPicker.getValue()));

        brushSize.setOnKeyTyped(e -> {

            if (!brushSize.getText().isEmpty()) {
                QuizController.changeWidth(Double.parseDouble(brushSize.getText()));
            }

        });

        eraser.setOnAction(event -> QuizController.changeColor(Color.rgb(225, 225, 225)));


        UnaryOperator<TextFormatter.Change> modifyChange = c -> {
            if (c.isContentChange()) {
                int newLength = c.getControlNewText().length();
                if (newLength > Constants.MAX_BRUSH_SIZE) {
                    // replace the input text with the last len chars
                    String tail = c.getControlNewText().substring(newLength - Constants.MAX_BRUSH_SIZE, newLength);
                    c.setText(tail);
                    // replace the range to complete text
                    // valid coordinates for range is in terms of old text
                    int oldLength = c.getControlText().length();
                    c.setRange(0, oldLength);
                }
            }
            return c;
        }; //Limit how much text can be typed.

        brushSize.setTextFormatter(new TextFormatter<>(modifyChange));

        clearButton.setOnAction(event -> QuizController.clearCanvas());

    }
}

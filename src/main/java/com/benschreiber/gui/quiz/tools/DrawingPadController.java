package com.benschreiber.gui.quiz.tools;

import com.benschreiber.etc.Constants;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
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

    private GraphicsContext gc;

    //Make a Stage variable because these are their own PopupStage and must be closed from inside
    private static Stage stage;

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        DrawingPadController.stage = stage;
    }

    public static void setPane(Pane pane) {
        DrawingPadController.pane = pane;
    }

    private static Pane pane;


    /**
     * Initial run method.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        Canvas paintCanvas = new Canvas(pane.getWidth(), pane.getHeight());
        paintCanvas.setOnMouseDragged(this::canvasOnDragged);
        paintCanvas.setOnMousePressed(this::canvasOnPressed);

        gc = paintCanvas.getGraphicsContext2D();
        gc.setStroke(Color.WHITE);

        pane.getChildren().add(1, paintCanvas);

        gc.setStroke(Color.WHITE);


        colorPicker.setOnAction(e -> {
            eraser.setSelected(false);
            gc.setStroke(colorPicker.getValue());
        });

        brushSize.setOnKeyTyped(e -> {

            if (!brushSize.getText().isEmpty()) {
                gc.setLineWidth((Double.parseDouble(brushSize.getText())));
            }

        });


        eraser.setOnAction(event -> {

            if (eraser.isSelected()) {
                gc.setStroke(Color.rgb(225, 225, 225));
            } else {
                gc.setStroke(colorPicker.getValue());
            }
        });


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

        clearButton.setOnAction(event -> clearCanvas());


    }

    private void canvasOnPressed(MouseEvent e) {
        //begin drawing
        gc.beginPath();
        gc.lineTo(e.getX(), e.getY());
        gc.stroke();
    }

    //When dragging mouse
    private void canvasOnDragged(MouseEvent event) {

        gc.lineTo(event.getX(), event.getY());

        gc.stroke();

    }

    private void clearCanvas() {
        gc.clearRect(0, 0, pane.getWidth(), pane.getHeight());
    }
}

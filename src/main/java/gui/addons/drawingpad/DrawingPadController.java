package gui.addons.drawingpad;

import etc.Constants;
import gui.controllers.TestController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.paint.Color;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class DrawingPadController implements Initializable {

    @FXML
    private CheckBox eraser;

    @FXML
    private TextField brushSize;

    @FXML
    private ColorPicker colorPicker;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //todo: make these not lambdas

        colorPicker.setOnAction(e -> {

            TestController.changeColor(colorPicker.getValue());

        });

        brushSize.setOnKeyTyped(e -> {

            if (!brushSize.getText().equals("")) {
                TestController.changeWidth(Double.parseDouble(brushSize.getText()));
            }

        });

        eraser.setOnAction(event -> {
            TestController.changeColor(Color.rgb(158, 158, 158));
        });


        UnaryOperator<TextFormatter.Change> modifyChange = c -> {
            if (c.isContentChange()) {
                int newLength = c.getControlNewText().length();
                if (newLength > Constants.brushMaxSize) {
                    // replace the input text with the last len chars
                    String tail = c.getControlNewText().substring(newLength - Constants.brushMaxSize, newLength);
                    c.setText(tail);
                    // replace the range to complete text
                    // valid coordinates for range is in terms of old text
                    int oldLength = c.getControlText().length();
                    c.setRange(0, oldLength);
                }
            }
            return c;
        }; //Limit how much text can be typed.

        brushSize.setTextFormatter(new TextFormatter<Object>(modifyChange));

    }
}

package gui.addons.drawingpad;

import gui.controllers.TestController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class DrawingPadController implements Initializable {

    @FXML
    private CheckBox eraser;

    @FXML
    private TextField brushSize;

    @FXML
    private ColorPicker colorPicker;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        colorPicker.setOnAction(e -> {
            TestController.changeColor(colorPicker.getValue());
        });

        brushSize.setOnKeyTyped(e -> {
            TestController.changeWidth(Double.parseDouble(brushSize.getText()));
        });

    }
}

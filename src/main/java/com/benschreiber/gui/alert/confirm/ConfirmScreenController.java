package com.benschreiber.gui.alert.confirm;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class ConfirmScreenController implements Initializable {

    private static String prompt;

    private static Stage stage;

    private static boolean response;

    @FXML
    Label promptLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        promptLabel.setText(prompt);
    }

    /**
     * If the user clicks Confirm
     */
    public void confirmButtonClicked() {
        stage.close();
        response = true;
    }

    /**
     * If the user clicks deny
     */
    public void denyButtonClicked() {
        stage.close();
        response = false;
    }

    /**
     * Getters/Setters
     */
    public static void setStage(Stage stage) {
        ConfirmScreenController.stage = stage;
    }

    public static void setPrompt(String prompt) {
        ConfirmScreenController.prompt = prompt;
    }

    public static boolean getResponse() {
        return response;
    }

}

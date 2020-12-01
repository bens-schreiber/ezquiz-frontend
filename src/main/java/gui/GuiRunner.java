package gui;

import gui.addons.ErrorBox;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

/**
 * Run the initial Start menu
 */

public class GuiRunner extends Application {

    @Override
    public void start(Stage primaryStage) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/start.fxml")); //Display the start page.

            Scene scene = new Scene(root);

            primaryStage.setResizable(false);

            primaryStage.setScene(scene);

            primaryStage.show();

            GuiHelper.addWindow("Start", primaryStage);

        } catch (IOException | NullPointerException e) {
            ErrorBox.display("A page failed to load.", true);
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}

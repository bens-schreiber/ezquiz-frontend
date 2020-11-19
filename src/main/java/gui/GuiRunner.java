package gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Run the initial Start menu
 */

public class GuiRunner extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/start.fxml")); //Display the start page.

        Scene scene = new Scene(root);

//        primaryStage.setAlwaysOnTop(true);
//        primaryStage.initStyle(StageStyle.UNDECORATED);


        primaryStage.setScene(scene);

        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}

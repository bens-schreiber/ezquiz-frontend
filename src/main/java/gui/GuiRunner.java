package gui;

import javafx.application.Application;
import javafx.stage.Stage;

/**
 * Create and initialize Primary Stage
 */
public class GuiRunner extends Application {

    @Override
    public void start(Stage primaryStage) {

        try {

            primaryStage.setResizable(false);
            primaryStage.setScene(FXHelper.getScene(FXHelper.Window.LOGIN));

            //Pass the PrimaryStage given from start method to FXController for future controllers to access
            FXController.setPrimaryStage(primaryStage);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}

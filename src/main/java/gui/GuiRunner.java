package gui;

import gui.etc.FXHelper;
import gui.popup.notification.UserNotifier;
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

            //Pass the PrimaryStage given from start method to PrimaryStageHelper for all classes to access
            PrimaryStageHolder.setPrimaryStage(primaryStage);
            PrimaryStageHolder.getPrimaryStage().show();

        } catch (Exception e) {
            new UserNotifier("The program failed to load").display();
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}

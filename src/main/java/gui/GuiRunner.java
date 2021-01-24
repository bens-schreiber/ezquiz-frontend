package gui;

import gui.etc.FXHelper;
import gui.popup.error.ErrorNotifier;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * JavaFX GUIRunner, get a Primary Stage and assign it to the start menu.
 */
public class GuiRunner extends Application {

    @Override
    public void start(Stage primaryStage) {

        try {

            primaryStage.setResizable(false);
            primaryStage.setScene(FXHelper.getScene(FXHelper.Window.MAINMENU));

            //Pass the PrimaryStage given from start method to PrimaryStageHelper for all classes to access
            PrimaryStageHolder.setPrimaryStage(primaryStage);
            PrimaryStageHolder.getPrimaryStage().show();

        } catch (Exception e) {
            new ErrorNotifier("The program failed to load", true).display(primaryStage);
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}

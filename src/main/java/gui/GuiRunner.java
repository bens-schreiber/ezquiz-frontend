package gui;

import gui.etc.FXHelper;
import gui.etc.Window;
import gui.popup.error.ErrorNotifier;
import gui.startmenu.StageHolder;
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
            primaryStage.setScene(FXHelper.getScene(Window.STARTPAGE));
            StageHolder.setPrimaryStage(primaryStage);
            primaryStage.show();

        } catch (Exception e) {
            new ErrorNotifier("The program failed to load", true).display();
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}

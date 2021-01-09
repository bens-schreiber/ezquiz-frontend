package gui;

import gui.popups.ErrorBox;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX GUIRunner, runs initial start menu and passes in primary stage
 */

public class GuiRunner extends Application {

    @Override
    public void start(Stage primaryStage) {

        try {

            primaryStage = StageHelper.createAndAddStage(Window.STARTPAGE);
            primaryStage.setResizable(false);

            primaryStage.show();

        } catch (IOException | NullPointerException e) {
            ErrorBox.display("A page failed to load.", true);
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}

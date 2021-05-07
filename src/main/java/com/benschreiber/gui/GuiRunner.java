package com.benschreiber.gui;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * Create and initialize Primary Stage
 */
public class GuiRunner extends Application {

    @Override
    public void start(Stage primaryStage) {

        try {

            //Set stage title and icon
            primaryStage.setTitle("EZQuiz");
            Image icon = new Image(getClass().getResourceAsStream("/pictures/quiz/ezquiz_logos-04.png"));
            primaryStage.getIcons().add(icon);


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

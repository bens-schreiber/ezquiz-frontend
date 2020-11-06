package gui;

import Questions.Question;
import Questions.QuestionBuilder;
import apis.Requests;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import layouts.MultipleChoice;


public class guiMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Quiz");
        BorderPane layout = MultipleChoice.getLayout((QuestionBuilder.questionFromJSON(Requests.getQuestion(1))));

        Scene scene = new Scene(layout, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

package gui;

import Questions.Question;
import Questions.QuestionBuilder;
import apis.Requests;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import layouts.BaseLayout;



public class guiMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        primaryStage.setTitle("Quiz");
        BaseLayout layout = new BaseLayout(QuestionBuilder.questionFromJSON(Requests.getQuestion(1)));

        Scene scene = new Scene(layout.getBaseLayout(), 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

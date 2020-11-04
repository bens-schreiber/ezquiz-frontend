package gui;

import Questions.Question;
import Questions.QuestionBuilder;
import apis.Requests;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;
public class guiMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        //stupid
        primaryStage.setTitle("Quiz");
        StackPane layout = new StackPane();
        Question a = QuestionBuilder.questionFromJSON(Requests.getRandQuestion());
        Label label = new Label(a.getQuestion());
        layout.getChildren().add(label);

        Scene scene = new Scene(layout, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}



import questions.Question;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import layouts.Layout;

import java.util.List;


public class guiMain extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Quiz");

        List<Question> questions = quiz.QuizController.getQuestions();

        Layout layout = new Layout(questions.get(0));


        Scene scene = new Scene(layout.getLayout(), 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

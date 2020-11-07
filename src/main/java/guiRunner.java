

import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.StageStyle;
import questions.Question;


import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import layouts.Layout;

import java.util.List;


public class guiRunner extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

        List<Question> questions = quiz.QuizController.getQuestions();
        Layout layout = new Layout(questions);

        /*
        * Change this to fullscreen and size of screen
        * Doesn't quite work on Ubuntu.
        * */

//        Rectangle2D visualBounds= Screen.getPrimary().getVisualBounds(); //Get the screen size
//        Scene scene = new Scene(layout.getLayout(), visualBounds.getWidth(), visualBounds.getHeight());
//        primaryStage.setAlwaysOnTop(true);
//        primaryStage.initStyle(StageStyle.UNDECORATED);

        Scene scene = new Scene(layout.getLayout(), 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

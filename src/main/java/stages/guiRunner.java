package stages;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import quiz.Quiz;


public class guiRunner extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception {

        Quiz.getQuestions();

//        Rectangle2D visualBounds= Screen.getPrimary().getVisualBounds(); //Get the screen size
//        Scene scene = new Scene(layout.getLayout(), visualBounds.getWidth(), visualBounds.getHeight());
//        primaryStage.setAlwaysOnTop(true);
//        primaryStage.initStyle(StageStyle.UNDECORATED);

        Parent root = FXMLLoader.load(getClass().getResource("/test.fxml"));
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}

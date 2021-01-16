package gui;
import javafx.stage.Stage;

public class PrimaryStageHolder {

    private PrimaryStageHolder() {
    }

    private static Stage primaryStage;

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void setPrimaryStage(Stage primaryStage) {
        PrimaryStageHolder.primaryStage = primaryStage;
    }
}

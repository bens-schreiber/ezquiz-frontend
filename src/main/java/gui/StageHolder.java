package gui;
import gui.popup.confirm.ConfirmNotifier;
import gui.popup.notification.UserNotifier;
import javafx.stage.Stage;

public class StageHolder {

    protected StageHolder() {
    }

    protected UserNotifier userNotifier = new UserNotifier();

    protected ConfirmNotifier confirmNotifier = new ConfirmNotifier();

    private static Stage primaryStage = new Stage();

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void setPrimaryStage(Stage primaryStage) {
        StageHolder.primaryStage.close();
        StageHolder.primaryStage = primaryStage;
        primaryStage.show();
    }
}

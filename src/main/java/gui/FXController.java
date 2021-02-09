package gui;
import gui.popup.confirm.ConfirmNotifier;
import gui.popup.notification.UserNotifier;
import javafx.stage.Stage;

public class FXController {

    protected FXController() {
    }

    protected UserNotifier userNotifier = new UserNotifier();
    protected ConfirmNotifier confirmNotifier = new ConfirmNotifier();

    private static Stage primaryStage = new Stage();

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void setPrimaryStage(Stage primaryStage) {
        FXController.primaryStage.close();
        FXController.primaryStage = primaryStage;
        primaryStage.show();
    }

    //General alert / error messages
    public enum AlertText {

        INTERNAL_ERROR("Whoops, an internal error has occurred."),
        EXTERNAL_ERROR("Whoops, a problem occurred with the server."),
        NO_CONNECTION("Connection to the server has failed. Log out and try again.");

        private final String text;

        AlertText(String text) {
            this.text = text;
        }

        public String toString() {
            return this.text;
        }
    }
}

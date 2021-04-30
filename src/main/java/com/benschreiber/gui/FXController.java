package com.benschreiber.gui;

import com.benschreiber.etc.Account;
import com.benschreiber.requests.Status;
import com.benschreiber.gui.windows.alert.confirm.ConfirmNotifier;
import com.benschreiber.gui.windows.alert.notification.UserNotifier;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Base for all FX Controllers to extend. Contains UserNotifier and ConfirmNotifier for reuse, error handling,
 * and the Primary Stage
 */
public class FXController {

    protected FXController() {}

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

    protected void errorHandle() {
        userNotifier.setText(AlertText.INTERNAL_ERROR.toString()).display();
    }

    protected void errorHandle(Stage stage) {
        userNotifier.setText(AlertText.INTERNAL_ERROR.toString()).display(stage);
    }

    protected void errorHandle(Status status) {

        switch (status) {

            case NO_CONTENT -> userNotifier.setText(AlertText.EXTERNAL_ERROR.toString()).display();

            case NO_CONNECTION -> userNotifier.setText(AlertText.NO_CONNECTION.toString()).display();

            //Session expired, log out, go to login screen
            case UNAUTHORIZED -> {
                userNotifier.setText(AlertText.EXPIRED_SESSION.toString()).display();
                Account.logout();
                FXController.getPrimaryStage().close();
                try {
                    FXController.setPrimaryStage(FXHelper.getPopupStage(FXHelper.Window.LOGIN, false));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                FXController.getPrimaryStage().show();

            }
        }
    }

    protected void errorHandle(Status status, Stage stage) {

        switch (status) {

            case NO_CONTENT -> userNotifier.setText(AlertText.EXTERNAL_ERROR.toString()).display(stage);

            case NO_CONNECTION -> userNotifier.setText(AlertText.NO_CONNECTION.toString()).display(stage);

            //Session expired, log out
            case UNAUTHORIZED -> {
                userNotifier.setText(AlertText.EXPIRED_SESSION.toString()).display(stage);
                Account.logout();
                FXController.getPrimaryStage().close();
                try {
                    FXController.setPrimaryStage(FXHelper.getPopupStage(FXHelper.Window.LOGIN, false));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                FXController.getPrimaryStage().show();

            }
        }
    }

    //General alert or error messages
    private enum AlertText {

        INTERNAL_ERROR("Whoops, an internal error has occurred."),
        EXTERNAL_ERROR("Whoops, a problem occurred with the server."),
        NO_CONNECTION("Connection to the server has failed. Log out and try again."),
        EXPIRED_SESSION("Session has expired. Logging out.");

        private final String text;

        AlertText(String text) {
            this.text = text;
        }

        public String toString() {
            return this.text;
        }
    }
}

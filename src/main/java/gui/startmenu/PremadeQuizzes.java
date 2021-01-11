package gui.startmenu;


//todo: make dis work :(

import gui.PrimaryStageHelper;
import gui.etc.FXHelper;
import gui.etc.Window;
import gui.popup.error.ErrorNotifier;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PremadeQuizzes extends PrimaryStageHelper {

    //Since it is a popup window, save the stage here
    static Stage stage;

    public void networkDesignButtonClicked() {

    }

    public void introToBusButtonClicked() {
    }

    public void busMathButtonClicked() {

    }

    public void marketingButtonClicked() {

    }

    public void customButtonClicked() {

        try {

            Scene scene = FXHelper.getScene(Window.CUSTOMQUIZ);

            stage.close();

            primaryStage.setScene(scene);

        } catch (Exception e) {

            new ErrorNotifier("A page failed to load", true).display();

            e.printStackTrace();

        }

    }

}

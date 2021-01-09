package gui.startmenu;


//todo: make dis work :(

import gui.etc.FXHelper;
import gui.etc.Window;
import gui.popup.error.ErrorNotifier;
import javafx.scene.Scene;

public class PremadeQuizzes extends StageHolder {

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
            getPrimaryStage().setScene(scene);

        } catch (Exception e) {

            new ErrorNotifier("A page failed to load", true).display();

            e.printStackTrace();

        }

    }

}

package gui.etc;

import gui.popup.error.ErrorNotifier;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.HashSet;

public class FXHelper {

    private FXHelper() {
    }

    private static final HashSet<Window> openedInstances = new HashSet<>();

    public static HashSet<Window> getOpenedInstances() {
        return openedInstances;
    }

    //Get a scene from Window.fxmlTable
    public static Scene getScene(Window window) throws IOException {

        return new Scene(FXMLLoader.load(FXHelper.class.getResource(Window.fxmlTable.get(window))));

    }

    //requireResponse if the page must be responded to before responding to others
    public static Stage getPopupStage(Window window, boolean requireResponse) throws IOException {

        //Set stage
        Stage stage = new Stage();

        if (requireResponse) {
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initStyle(StageStyle.UNDECORATED);
        }
        stage.setAlwaysOnTop(true);
        stage.setResizable(false);

        Scene scene = new Scene(FXMLLoader.load(ErrorNotifier.class.getResource(Window.fxmlTable.get(window))));
        stage.setScene(scene);

        return stage;

    }
}

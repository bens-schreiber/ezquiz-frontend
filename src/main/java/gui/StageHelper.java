package gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

/**
 * Stores submitted windows in HashMap so they can be accessed from different Controllers.
 */

public class StageHelper {

    private static final HashMap<Window, Stage> stages = new HashMap<>();

    //todo: remove this when code is fxml page
    public static void addStage(Window name, Stage stage) {
        stages.put(name, stage);
    }

    public static HashMap<Window, Stage> getStages() {
        return stages;
    }

    public static void closeStage(Window name) {

        //Close the stage
        stages.get(name).close();

        //Remove from openedWindows
        stages.remove(name);
    }

    public static void closeAllStages() {
        stages.forEach((s, stage) -> stage.close());
    }

    public static Stage createAndAddStage(Window name) throws IOException {

        //Find fxml path in hashmap
        String path = Window.fxmlTable.get(name);

        Scene scene = new Scene(FXMLLoader.load(StageHelper.class.getResource(path)));

        Stage stage = new Stage();

        stage.setScene(scene);

        addStage(name, stage);

        return stage;
    }

    /**
     * Reusable scenes to not reload information.
     */
    private static final HashMap<Window, Scene> reusableScenes = new HashMap<>();

    public static void addScene(Window name, Scene scene) {

        reusableScenes.put(name, scene);

    }

    public static void clearScenes() {
        reusableScenes.clear();
    }

    public static HashMap<Window, Scene> getScenes() {
        return reusableScenes;
    }

}

package gui;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.HashMap;

/**
 * Stores submitted windows in HashMap so they can be accessed from different Controllers.
 */

public class StageHelper {

    private static final HashMap<String, Stage> stages = new HashMap<>();

    public static void addStage(String name, Stage stage) {

        stages.put(name, stage);

    }

    public static HashMap<String, Stage> getStages() {
        return stages;
    }

    public static void closeStage(String name) {

        //Close the stage
        stages.get(name).close();

        //Remove from openedWindows
        stages.remove(name);
    }

    public static void closeAllStages() {
        stages.forEach((s, stage) -> stage.close());
    }

    /**
     * Reusable scenes to not reload information.
     */
    private static final HashMap<String, Scene> reusableScenes = new HashMap<>();

    public static void addScene(String name, Scene scene) {

        reusableScenes.put(name, scene);

    }

    public static void clearScenes() {
        reusableScenes.clear();
    }

    public static HashMap<String, Scene> getScenes() {
        return reusableScenes;
    }

}

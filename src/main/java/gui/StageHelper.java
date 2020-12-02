package gui;

import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores submitted windows in HashMap so they can be accessed from different Controllers
 */

public class StageHelper {

    private static final HashMap<String, Stage> openedWindows = new HashMap<>();

    public static void addWindow(String name, Stage stage) {

        openedWindows.put(name, stage);

    }

    public static HashMap<String, Stage> getOpenedWindows() {
        return openedWindows;
    }

    public static void closeWindow(String name) {

        //Close the stage
        openedWindows.get(name).close();

        //Remove from openedWindows
        openedWindows.remove(name);
    }

    public static void closeAll() {
        for (Map.Entry<String, Stage> entry : openedWindows.entrySet()) {
            entry.getValue().close();
        }
    }

}

package gui;

import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Put opened windows that need be closed by a different controller
 */

public class GuiHelper {

    private static HashMap<String, Stage> openedWindows = new HashMap<>();

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

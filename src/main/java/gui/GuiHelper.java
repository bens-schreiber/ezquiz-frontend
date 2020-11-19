package gui;

import javafx.stage.Stage;

import java.util.HashMap;

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

}

package gui.addons.notepad;

public class NotePadHelper {
    private static String savedText = "";

    private static boolean displaying;

    public static void setDisplaying(boolean val) {

        displaying = val;
    }

    public static boolean isDisplaying() {

        return displaying;
    }


    public static void setSavedText(String text) {
        savedText = text;

    }

    public static String getSavedText() {
        return savedText;

    }
}

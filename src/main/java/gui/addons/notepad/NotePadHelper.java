package gui.addons.notepad;

public class NotePadHelper {

    private static String savedText;

    public static void setSavedText(String text) {
        savedText = text;
    }

    public static String getSavedText() {
        return savedText;
    }

}

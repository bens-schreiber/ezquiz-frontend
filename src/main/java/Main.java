import gui.GuiRunner;

/**
 * Runs real main method.
 *
 * @author benschreiber
 */
public class Main {
    public static void main(String[] args) {
        GuiRunner.main(args); //Need to have a separate main from the JavaFX gui.guiMain... Doesn't work otherwise.
    }
}

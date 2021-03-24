package com.benschreiber;

import com.benschreiber.gui.GuiRunner;

/**
 * @author benschreiber
 */
public class Main {
    public static void main(String[] args) {
        GuiRunner.main(args); //Need to have a separate main from the JavaFX classes.gui.guiMain... Doesn't work otherwise.
    }
}
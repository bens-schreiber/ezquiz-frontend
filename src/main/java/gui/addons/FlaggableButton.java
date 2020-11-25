package gui.addons;

import etc.Constants;
import javafx.scene.control.Button;

public class FlaggableButton extends Button {
    private boolean flagged;

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;

        if (flagged) {
            this.setStyle(Constants.FLAGGED_COLOR);
        }
    }

    public boolean isFlagged() {
        return flagged;
    }
}

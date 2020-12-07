package gui.controllers.quiz;

import etc.Constants;
import javafx.scene.control.Button;

/**
 * Addon to buttons that allows them to be considered 'flagged' or not.
 * For use in QuizController.
 */
class FlagButton extends Button {

    private boolean flagged;


    public void setFlagged(boolean flagged) {
        this.flagged = flagged;

        if (flagged) {
            this.setStyle(Constants.FLAGGED_COLOR_FX);
        } else {
            this.setStyle(Constants.UNSELECTED_COLOR_FX);
        }
    }

    public boolean isFlagged() {
        return flagged;
    }
}

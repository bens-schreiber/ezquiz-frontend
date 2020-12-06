package gui.controllers.quiz;

import etc.Constants;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

/**
 * Addon to buttons that allows them to be considered 'flagged' or not.
 * For use in QuizController.
 */
class FlagButton extends Button {

    private boolean flagged;

    /**
     * @param hBox Node the flag button should be instantiated in.
     */
    public FlagButton(HBox hBox) {

        //Give style properties
        this.setPrefSize(40, 35);

        hBox.getChildren().add(this);

        //Set text to the number of the corresponding question
        this.setText(String.valueOf(hBox.getChildren().size()));
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;

        if (flagged) {
            this.setStyle(Constants.FLAGGED_COLOR_FX);
        }
    }

    public boolean isFlagged() {
        return flagged;
    }
}

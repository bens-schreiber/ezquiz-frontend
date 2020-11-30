package gui.addons;

import etc.Constants;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;

public class FlagButton extends Button {
    private boolean flagged;

    public FlagButton(HBox hBox) {

        //Give style properties
        this.setStyle(Constants.UNSELECTED_COLOR);
        this.setPrefSize(35, 35);

        hBox.getChildren().add(this);

        //Set text to the number of the corresponding question
        this.setText(String.valueOf(hBox.getChildren().size()));
    }

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

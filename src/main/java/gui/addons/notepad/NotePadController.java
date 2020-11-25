package gui.addons.notepad;

import etc.Constants;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class NotePadController implements Initializable {
    @FXML
    TextArea notepadText;

    @FXML
    Button saveButton;

    /**
     * Notepad Controller
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Limit how much text can be typed.
        UnaryOperator<TextFormatter.Change> modifyChange = c -> {
            if (c.isContentChange()) {
                int newLength = c.getControlNewText().length();
                if (newLength > Constants.MAX_NOTEPAD_TEXT) {
                    // replace the input text with the last len chars
                    String tail = c.getControlNewText().substring(newLength - Constants.MAX_NOTEPAD_TEXT, newLength);
                    c.setText(tail);
                    // replace the range to complete text
                    // valid coordinates for range is in terms of old text
                    int oldLength = c.getControlText().length();
                    c.setRange(0, oldLength);
                }
            }
            return c;
        };
        notepadText.setTextFormatter(new TextFormatter<>(modifyChange));

        //Put the saved text on the notepad
        notepadText.setText(NotePadHelper.getSavedText());

    }

    //Save the text
    public void onSaveButton() {

        NotePadHelper.setSavedText(notepadText.getText());

    }
}

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        UnaryOperator<TextFormatter.Change> modifyChange = c -> {
            if (c.isContentChange()) {
                int newLength = c.getControlNewText().length();
                if (newLength > Constants.notepadTextAmount) {
                    // replace the input text with the last len chars
                    String tail = c.getControlNewText().substring(newLength - Constants.notepadTextAmount, newLength);
                    c.setText(tail);
                    // replace the range to complete text
                    // valid coordinates for range is in terms of old text
                    int oldLength = c.getControlText().length();
                    c.setRange(0, oldLength);
                }
            }
            return c;
        }; //Limit how much text can be typed.

        notepadText.setTextFormatter(new TextFormatter<Object>(modifyChange));

        notepadText.setText(NotePadHelper.getSavedText());

    }

    public void onSaveButton() {

        NotePadHelper.setSavedText(notepadText.getText());
    }
}

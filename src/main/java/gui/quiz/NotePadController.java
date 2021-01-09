package gui.quiz;

import etc.Constants;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

/**
 * ActionEvent controller for the NotePad stage.
 */
public class NotePadController implements Initializable {
    @FXML
    TextArea notepadText;

    @FXML
    Button saveButton;

    private static String savedText;

    private static Stage stage;

    public static Stage getStage() {
        return stage;
    }

    public static void setStage(Stage stage) {
        NotePadController.stage = stage;
    }

    /**
     * Initial startup method.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Limit how much text can be typed.
        UnaryOperator<TextFormatter.Change> modifyChange = c -> {
            if (c.isContentChange()) {
                int newLength = c.getControlNewText().length();
                if (newLength > Constants.MAX_NOTEPAD_TEXT) {
                    c.setText("");
                }
            }
            return c;
        };
        notepadText.setTextFormatter(new TextFormatter<>(modifyChange));

        //Put the saved text on the notepad
        notepadText.setText(savedText);

    }

    //Save the text
    public void onSaveButton() {

        savedText = notepadText.getText();

    }
}

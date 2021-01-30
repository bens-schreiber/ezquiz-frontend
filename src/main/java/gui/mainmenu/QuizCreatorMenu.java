package gui.mainmenu;

import gui.account.Account;
import gui.mainmenu.excel.ExcelReader;
import gui.mainmenu.excel.ExcelValidateException;
import gui.popup.notification.UserNotifier;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;
import requests.DatabaseRequest;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class QuizCreatorMenu implements Initializable {

    @FXML
    TextField quizNameTextField;

    @FXML
    CheckBox revealAnswersCheckBox, enableNotePadCheckBox, enableCalculatorCheckBox, enableDrawingPadCheckBox;

    @FXML
    Label fileDisplay;

    static Stage stage;

    private ExcelReader excelReader;

    public void submitButtonClicked() {

        if (this.excelReader == null) {
            new UserNotifier("Upload questions before submitting.").display(stage);
        } else if (quizNameTextField.getText().isEmpty()) {
            new UserNotifier("Set a quiz name before submitting.").display(stage);
        } else {

            try {

                //Throws ExcelValidateException if invalid
                excelReader.validateFile();

                //Put preferences into the jsonobject
                JSONObject quiz = excelReader.sheetToJSON(quizNameTextField.getText())
                        .put("preferences", collectCheckBoxPreferences());

                switch (DatabaseRequest.postQuiz(quiz, Account.getUser())) {

                    case ACCEPTED -> stage.close();

                    case NO_CONTENT -> new UserNotifier("An error occurred while posting to the server.").display(stage);

                    case NO_CONNECTION -> new UserNotifier("Connection to the server failed.").display(stage);
                }

            } catch (ExcelValidateException e) {

                new UserNotifier(e.getErrorMsg()).display(stage);

            } catch (Exception e) {

                new UserNotifier("An unknown internal error occurred.").display(stage);
                e.printStackTrace();

            }
        }

    }

    public void questionBuilderButtonClicked() {
    }

    public void uploadExcelButtonClicked() {

        //Open a FileChooser to select the excel file.
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Excel File");
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            //Attempt to read as a valid excel file in the specific format required.
            this.excelReader = new ExcelReader(selectedFile);
            fileDisplay.setText(fileDisplay.getText() + excelReader.getFilePath());
        }

    }

    private JSONObject collectCheckBoxPreferences() throws JSONException {

        JSONObject preferences = new JSONObject();
        preferences.put("calculator", enableCalculatorCheckBox.isSelected() ? 1 : 0);
        preferences.put("answers", revealAnswersCheckBox.isSelected() ? 1 : 0);
        preferences.put("notepad", enableNotePadCheckBox.isSelected() ? 1 : 0);
        preferences.put("drawingpad", enableDrawingPadCheckBox.isSelected() ? 1 : 0);

        return preferences;

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //No special characters, size of 8 only
        quizNameTextField.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText().matches("[ $&+,:;=\\\\?@#|/'<>.^*()%!-]")) {
                change.setText("");
            } else if (quizNameTextField.getText().length() > 16) {
                change.setText("");
            }

            return change;
        }));
    }
}

package gui.mainmenu;

import gui.FXController;
import gui.account.Account;
import gui.mainmenu.excel.ExcelReader;
import gui.mainmenu.excel.ExcelValidateException;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONObject;
import requests.DatabaseRequest;
import requests.Status;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class QuizUpload extends FXController implements Initializable {

    @FXML
    TextField quizNameTextField;

    @FXML
    CheckBox revealAnswersCheckBox, enableNotePadCheckBox, enableCalculatorCheckBox, enableDrawingPadCheckBox;

    @FXML
    Label fileDisplay;

    private static Stage stage;

    private ExcelReader excelReader;

    public void submitButtonClicked() {

        if (this.excelReader == null) {

            userNotifier.setText("Upload questions before submitting.").display(stage);

        } else if (quizNameTextField.getText().isEmpty()) {

            userNotifier.setText("Set a quiz name before submitting.").display(stage);

        } else {

            try {

                //Throws ExcelValidateException if invalid
                excelReader.validateFile();

                //Assemble json to send to the server. True/False is represented as 1 or 0 bit in SQL.
                JSONObject quizJSON = excelReader.sheetToJSON()
                        .put("preferences",
                                new JSONObject()
                                        .put("calculator", enableCalculatorCheckBox.isSelected() ? 1 : 0)
                                        .put("answers", revealAnswersCheckBox.isSelected() ? 1 : 0)
                                        .put("notepad", enableNotePadCheckBox.isSelected() ? 1 : 0)
                                        .put("drawingpad", enableDrawingPadCheckBox.isSelected() ? 1 : 0)
                        )
                        .put("quiz",
                                new JSONObject()
                                        .put("quizname", quizNameTextField.getText())
                                        .put("quizowner", Account.getUser().getUsername())
                        );

                //Attempt to post the Quiz to the server. Handle errors
                Status status = DatabaseRequest.postQuiz(quizJSON, Account.getUser());
                if (status == Status.ACCEPTED) {
                    stage.close();
                } else {
                    errorHandle(status, stage);
                }


            } catch (ExcelValidateException e) {

                userNotifier.setText(e.getErrorMsg()).display(stage);

            } catch (Exception e) {

                e.printStackTrace();
                errorHandle(stage);

            }

        }

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

    public static void setStage(Stage stage) {
        QuizUpload.stage = stage;
    }
}

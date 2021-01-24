package gui.mainmenu;

import gui.PrimaryStageHolder;
import gui.mainmenu.excel.ExcelReader;
import gui.mainmenu.excel.ExcelValidateException;
import gui.popup.error.ErrorNotifier;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONException;
import requests.DatabaseRequest;

import java.io.File;
import java.io.IOException;

public class UploadQuiz {

    static Stage stage;

    public void uploadFileClicked() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Excel File");

        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            try {

                ExcelReader excelReader = new ExcelReader(selectedFile);
                excelReader.readFile();
                DatabaseRequest.uploadQuiz(excelReader.toJSON(), "database/questions/");

            } catch (IOException | JSONException | InterruptedException e) {
                e.printStackTrace();
            } catch (ExcelValidateException e) {
                new ErrorNotifier(e.getErrorMsg(), false).display(PrimaryStageHolder.getPrimaryStage());
            }
        }

    }
}

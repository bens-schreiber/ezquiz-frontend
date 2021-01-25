package gui.mainmenu;

import gui.PrimaryStageHolder;
import gui.mainmenu.excel.ExcelReader;
import gui.mainmenu.excel.ExcelValidateException;
import gui.popup.error.ErrorNotifier;
import javafx.fxml.Initializable;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONException;
import requests.DatabaseRequest;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminMenu implements Initializable {


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //todo: make server and request method for getting quizzes made by you, all the buttons

    }

    public void uploadQuizClicked() {

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Excel File");

        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {

            try {

                ExcelReader excelReader = new ExcelReader(selectedFile);
                excelReader.readFile();

                if (DatabaseRequest.postQuiz(excelReader.toJSON()) == 202) {

                    //use error notifier as success popup
                    new ErrorNotifier("Successfully uploaded.", false).display(PrimaryStageHolder.getPrimaryStage());

                } else new ErrorNotifier("Failed to upload.", false).display(PrimaryStageHolder.getPrimaryStage());

            } catch (IOException | JSONException | InterruptedException e) {
                e.printStackTrace();
            } catch (ExcelValidateException e) {
                new ErrorNotifier(e.getErrorMsg(), false).display(PrimaryStageHolder.getPrimaryStage());
            }
        }

    }

    public void deleteQuizClicked() {

    }

    public void updateQuizClicked() {

    }

    public void getExcelClicked() {

    }

}

package gui.mainmenu;

import etc.Constants;
import gui.account.Account;
import gui.account.Quiz;
import gui.mainmenu.excel.ExcelReader;
import gui.mainmenu.excel.ExcelValidateException;
import gui.popup.notification.UserNotifier;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.json.JSONException;
import requests.DatabaseRequest;
import requests.QuestionJSONRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AdminMenu implements Initializable {

    @FXML
    TableView<Quiz> quizzesTable;

    @FXML
    TableColumn<Quiz, String> nameColumn, keyColumn;


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Set column constructors
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        keyColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getKey())));

        try {

            //Try to fetch keys from database
            quizzesTable.setItems(DatabaseRequest.getCreatedQuizzes(Account.getUser()));

        } catch (InterruptedException | IOException e) {

            e.printStackTrace();


        } catch (JSONException ignored) {
        }

    }

    public void uploadQuizClicked() {

        //Open a FileChooser to select the excel file.
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Excel File");
        File selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {

            //Attempt to read as a valid excel file in the specific format required.
            try {

                ExcelReader excelReader = new ExcelReader(selectedFile);
                excelReader.validateFile();

                switch (DatabaseRequest.postQuiz(excelReader.sheetToJSONArray(), Account.getUser())) {

                    case ACCEPTED -> quizzesTable.setItems(DatabaseRequest.getCreatedQuizzes(Account.getUser()));

                    case NO_CONTENT -> new UserNotifier("An error occurred while posting to the server.").display();

                    case NO_CONNECTION -> new UserNotifier("Connection to the server failed.").display();
                }


            } catch (ExcelValidateException e) {

                new UserNotifier(e.getErrorMsg()).display();

            } catch (Exception e) {

                new UserNotifier("An unknown error occurred.").display();

            }
        }
    }

    public void deleteQuizClicked() {

        try {

            if (quizzesTable.getSelectionModel().getSelectedItem() != null) {

                switch (DatabaseRequest.deleteQuiz(quizzesTable.getSelectionModel().getSelectedItem(), Account.getUser())) {

                    case ACCEPTED -> quizzesTable.setItems(DatabaseRequest.getCreatedQuizzes(Account.getUser()));

                    case NO_CONTENT -> new UserNotifier("An error occurred while deleting the quiz.").display();

                    case NO_CONNECTION -> new UserNotifier("Connection to the server failed.").display();
                }

            }

        } catch (Exception e) {

            e.printStackTrace();

            new UserNotifier("An unknown error occurred.").display();

        }

    }

    public void getExcelClicked() {

        if (quizzesTable.getSelectionModel().getSelectedItem() != null) {

            try {

                //Table only records name and key, use Account.getUser to get remaining parts for path.
                Quiz quiz = quizzesTable.getSelectionModel().getSelectedItem();
                Account.setQuiz(Account.getUser().getUsername(), quiz.getName(), quiz.getKey());

                //Request for answers along with the questions.
                QuestionJSONRequest request = new QuestionJSONRequest(Account.getUser(),
                        Constants.DEFAULT_PATH + "/questions/answer/" + Account.getQuizPath());

                request.initializeRequest();

                ExcelReader excelReader = new ExcelReader(request.getJson());

                //todo: error handle, choose where to store file on pc
                FileOutputStream outputStream = new FileOutputStream("/home/benschreiber/Documents/quiz.xlsx");
                excelReader.jsonToExcel().write(outputStream);
                outputStream.close();


            } catch (InterruptedException | IOException | JSONException e) {
                e.printStackTrace();
            }
        }

    }

}

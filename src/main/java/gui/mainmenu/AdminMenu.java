package gui.mainmenu;

import gui.account.Account;
import gui.account.Quiz;
import gui.etc.FXHelper;
import gui.mainmenu.quizbuilder.QuizBuilderTool;
import gui.popup.confirm.ConfirmNotifier;
import gui.popup.notification.UserNotifier;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import org.json.JSONException;
import requests.DatabaseRequest;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Pane for displaying in MainMenu.
 */
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

        try {

            Stage stage = FXHelper.getPopupStage(FXHelper.Window.QUIZ_UPLOAD, false);
            QuizUpload.stage = stage;
            stage.showAndWait();
            quizzesTable.setItems(DatabaseRequest.getCreatedQuizzes(Account.getUser()));

        } catch (Exception e) {

            new UserNotifier("An unknown internal error occurred.").display();

        }

    }

    public void quizBuilderClicked() {
        try {

            Stage stage = FXHelper.getPopupStage(FXHelper.Window.QUIZ_BUILDER, false);
            QuizBuilderTool.setStage(stage);
            stage.show();

        } catch (Exception e) {

            e.printStackTrace();

            new UserNotifier("An unknown internal error occurred.").display();

        }
    }

    public void deleteQuizClicked() {

        try {

            if (quizzesTable.getSelectionModel().getSelectedItem() != null) {

                if (new ConfirmNotifier("Are you sure you want to delete: "
                        + quizzesTable.getSelectionModel().getSelectedItem().getName() +
                        "? All existing keys and scores will be lost.").display().getResponse()) {

                    switch (DatabaseRequest.deleteQuiz(quizzesTable.getSelectionModel().getSelectedItem(), Account.getUser())) {

                        case ACCEPTED -> quizzesTable.setItems(DatabaseRequest.getCreatedQuizzes(Account.getUser()));

                        case NO_CONTENT -> new UserNotifier("An error occurred while deleting the quiz.").display();

                        case NO_CONNECTION -> new UserNotifier("Connection to the server failed.").display();
                    }
                }

            } else {

                new UserNotifier("Please select a created Quiz.").display();

            }

        } catch (Exception e) {

            e.printStackTrace();

            new UserNotifier("An unknown internal error occurred.").display();

        }

    }

    public void getExcelClicked() {

//        if (quizzesTable.getSelectionModel().getSelectedItem() != null) {
//
//            try {
//
//                //Table only records name and key, use Account.getUser to get remaining parts for path.
//                Quiz quiz = quizzesTable.getSelectionModel().getSelectedItem();
//                Account.setQuiz(Account.getUser().getUsername(), quiz.getName(), quiz.getKey());
//
//                //Request for answers along with the questions.
//                QuizJSONRequest request = new QuizJSONRequest(Account.getUser(),
//                        Constants.DEFAULT_PATH + "questions/answers/" + Account.getQuiz().getKey());
//
//                request.initializeRequest();
//
//                ExcelReader excelReader = new ExcelReader(request.getQuestionJSON());
//
//                //Open a DirectoryChooser to choose where to store the excel
//                DirectoryChooser directoryChooser = new DirectoryChooser();
//                File selectedDirectory = directoryChooser.showDialog(PrimaryStageHolder.getPrimaryStage());
//
//                FileOutputStream outputStream = new FileOutputStream(selectedDirectory.getAbsolutePath() + "/quiz.xlsx");
//
//                excelReader.jsonToExcel().write(outputStream);
//                outputStream.close();
//
//
//            } catch (ConnectException e) {
//
//                new UserNotifier("Could not connect to server.").display();
//
//            } catch (Exception e) {
//
//                new UserNotifier("An unknown error occurred").display();
//
//            }
//
//        } else {
//
//            new UserNotifier("Please select a created Quiz.").display();
//
//        }

    }

}

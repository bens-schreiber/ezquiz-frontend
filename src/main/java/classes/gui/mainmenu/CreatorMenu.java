package classes.gui.mainmenu;

import classes.etc.Account;
import classes.etc.Constants;
import classes.etc.Quiz;
import classes.gui.FXController;
import classes.gui.FXHelper;
import classes.gui.mainmenu.excel.ExcelReader;
import classes.gui.mainmenu.quizbuilder.QuizBuilderTool;
import classes.requests.DatabaseRequest;
import classes.requests.QuizJSONRequest;
import classes.requests.Status;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.json.JSONException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Pane for displaying in MainMenu.
 */
public class CreatorMenu extends FXController implements Initializable {

    @FXML
    TableView<Quiz> quizzesTable;

    @FXML
    TableColumn<Quiz, String> nameColumn, keyColumn;

    //Instances of other stages this scene can open
    private Stage quizBuilder = new Stage();
    private Stage quizUpload = new Stage();


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
            generalErrorHandle(Status.NO_CONNECTION);

        } catch (JSONException ignored) {
        }

    }

    public void uploadQuizClicked() {

        try {

            if (!quizUpload.isShowing()) {

                this.quizUpload = FXHelper.getPopupStage(FXHelper.Window.QUIZ_UPLOAD, false);

                //Pass the stage to the quizUpload so it can close itself
                QuizUpload.setStage(quizUpload);

                quizUpload.showAndWait();

                quizzesTable.setItems(DatabaseRequest.getCreatedQuizzes(Account.getUser()));

            }

        } catch (Exception e) {
            e.printStackTrace();
            generalErrorHandle();
        }

    }

    public void quizBuilderClicked() {
        try {

            //Check if an instance is already open
            if (!quizBuilder.isShowing()) {

                quizBuilder = FXHelper.getPopupStage(FXHelper.Window.QUIZ_BUILDER, false);

                //Give stage to QuizBuilderTool so it can close itself
                QuizBuilderTool.setStage(quizBuilder);

                quizBuilder.show();

            }

        } catch (Exception e) {
            e.printStackTrace();
            generalErrorHandle();
        }
    }

    public void deleteQuizClicked() {

        try {

            if (quizzesTable.getSelectionModel().getSelectedItem() != null) {

                if (confirmNotifier.setPrompt("Are you sure you want to delete: "
                        + quizzesTable.getSelectionModel().getSelectedItem().getName() +
                        "? All existing keys and scores will be lost.").display().getResponse()) {

                    Status status = DatabaseRequest.deleteQuiz(quizzesTable.getSelectionModel().getSelectedItem(), Account.getUser());
                    if (status == Status.ACCEPTED) {
                        quizzesTable.setItems(DatabaseRequest.getCreatedQuizzes(Account.getUser()));
                    } else {
                        generalErrorHandle(status);
                    }
                }

            } else {

                userNotifier.setText("Please select a created Quiz.").display();

            }

        } catch (Exception e) {

            e.printStackTrace();
            generalErrorHandle();

        }

    }

    public void getExcelClicked() {

        if (quizzesTable.getSelectionModel().getSelectedItem() != null) {

            try {

                //Table only records name and key, use Account.getUser to get remaining parts for path.
                Quiz quiz = quizzesTable.getSelectionModel().getSelectedItem();
                Account.setQuiz(Account.getUser().getUsername(), quiz.getName(), quiz.getKey());

                //Request for answers along with the questions.
                QuizJSONRequest request = new QuizJSONRequest(Account.getUser(),
                        Constants.DEFAULT_PATH + "questions/answers/" + Account.getQuiz().getKey());

                request.initializeRequest();

                ExcelReader excelReader = new ExcelReader(request.getQuestions());

                //Open a DirectoryChooser to choose where to store the excel
                DirectoryChooser directoryChooser = new DirectoryChooser();
                File selectedDirectory = directoryChooser.showDialog(FXController.getPrimaryStage());

                FileOutputStream outputStream = new FileOutputStream(selectedDirectory.getAbsolutePath() + "/quiz.xlsx");

                excelReader.jsonToExcel().write(outputStream);
                outputStream.close();


            } catch (ConnectException e) {

                userNotifier.setText("Could not connect to server.").display();

            } catch (Exception e) {

                e.printStackTrace();

                userNotifier.setText("An unknown error occurred").display();

            }

        } else {

            userNotifier.setText("Please select a created Quiz.").display();

        }

    }

}

package com.benschreiber.gui.windows.mainmenu;

import com.benschreiber.etc.Account;
import com.benschreiber.gui.FXController;
import com.benschreiber.gui.windows.alert.quizkey.EnterQuizKeyNotifier;
import com.benschreiber.requests.Status;
import com.benschreiber.etc.Quiz;
import com.benschreiber.gui.windows.quiz.QuizHelper;
import com.benschreiber.requests.DatabaseRequest;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Pane for displaying in MainMenu.
 */
public class QuizzesMenu extends FXController implements Initializable {

    @FXML
    TableView<Quiz> savedQuizKeys, previousQuizzes;

    @FXML
    TableColumn<Quiz, String> nameColumn, classColumn, keyColumn,
            prevQuizName, prevQuizClass, prevQuizScore, prevQuizKey;

    private final EnterQuizKeyNotifier enterQuizKeyNotifier = new EnterQuizKeyNotifier();


    //Right click menu
    private final ContextMenu contextMenu = new ContextMenu();
    {
        MenuItem takeQuiz = new MenuItem("Take Quiz");
        MenuItem takeRandomQuiz = new MenuItem("Take Randomized Quiz");
        takeQuiz.setStyle("-fx-text-fill: #000000");
        takeRandomQuiz.setStyle("-fx-text-fill: #000000");

        takeQuiz.setOnAction(e -> takeQuizClicked());
        takeRandomQuiz.setOnAction(e -> randomQuizClicked());

        contextMenu.getItems().addAll(takeQuiz, takeRandomQuiz);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Tell columns how to create data from the Quiz class.
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        classColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOwner()));
        keyColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getKey())));

        prevQuizName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        prevQuizClass.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOwner()));
        prevQuizKey.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getKey())));
        prevQuizScore.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getScore())));

        try {

            //Try to fetch keys from database
            savedQuizKeys.setItems(DatabaseRequest.getSavedQuizKeys(Account.getUser()));
            previousQuizzes.setItems(DatabaseRequest.getPreviousQuizzes());

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
            errorHandle(Status.NO_CONNECTION);
        } catch (JSONException ignored) {
        }

        savedQuizKeys.setContextMenu(contextMenu);

    }

    public void tableViewClicked() {

        //Whenever a quiz is selected in the table, set it as the Account Quiz.
        Account.setQuiz(savedQuizKeys.getSelectionModel().getSelectedItem());

    }

    public void addKeyClicked() {

        try {

            //Ask for input from user for a code.
            enterQuizKeyNotifier.display();

            if (enterQuizKeyNotifier.isKeyValid()) {

                //Refresh database
                Status status = DatabaseRequest.postQuizKey(Account.getUser(), enterQuizKeyNotifier.getKey());
                if (status == Status.ACCEPTED) {
                    savedQuizKeys.setItems(DatabaseRequest.getSavedQuizKeys(Account.getUser()));
                } else {
                    errorHandle(status);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            errorHandle();
        }

    }

    public void removeKeyClicked() {

        try {

            //Refresh database
            if (Account.getQuiz() != null) {
                Status status = DatabaseRequest.deleteQuizKey(savedQuizKeys.getSelectionModel().getSelectedItem(), Account.getUser());
                if (status == Status.ACCEPTED) {
                    savedQuizKeys.setItems(DatabaseRequest.getSavedQuizKeys(Account.getUser()));
                } else {
                    errorHandle(status);
                }
            }

        } catch (Exception e) {

            e.printStackTrace();
            errorHandle();

        }

        Account.setQuiz(null);

    }


    private void takeQuizClicked() {

        if (Account.getQuiz() != null) {

            if (confirmNotifier.setPrompt("Are you sure you want to take: " + Account.getQuiz().getName()).display().getResponse()) {
                try {

                    QuizHelper.startQuiz(false);

                } catch (Exception e) {

                    e.printStackTrace();
                    errorHandle();

                }
            }

        } else userNotifier.setText("You have not selected a Quiz").display();

    }

    private void randomQuizClicked() {

        if (Account.getQuiz() != null) {

            if (confirmNotifier.setPrompt("Are you sure you want to take: " + Account.getQuiz().getName()).display().getResponse()) {

                try {

                    QuizHelper.startQuiz(true);

                } catch (Exception e) {
                    e.printStackTrace();
                    errorHandle();

                }

            }

        } else userNotifier.setText("You have not selected a Quiz").display();

    }


}

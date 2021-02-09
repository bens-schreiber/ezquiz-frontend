package gui.mainmenu;

import gui.FXController;
import gui.account.Account;
import gui.account.Quiz;
import gui.popup.quizkey.EnterQuizKeyNotifier;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.json.JSONException;
import requests.DatabaseRequest;

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
            userNotifier.setText(AlertText.NO_CONNECTION).display();
        } catch (JSONException ignored) {
        }

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
                switch (DatabaseRequest.postQuizKey(Account.getUser(), enterQuizKeyNotifier.getKey())) {

                    case ACCEPTED -> savedQuizKeys.setItems(DatabaseRequest.getSavedQuizKeys(Account.getUser()));

                    case NO_CONTENT -> userNotifier.setText(AlertText.EXTERNAL_ERROR).display();

                    case NO_CONNECTION -> userNotifier.setText(AlertText.NO_CONNECTION).display();

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            userNotifier.setText(AlertText.INTERNAL_ERROR).display();
        }

    }

    public void removeKeyClicked() {

        try {

            //Refresh database
            switch (DatabaseRequest.deleteQuizKey(savedQuizKeys.getSelectionModel().getSelectedItem(), Account.getUser())) {

                case ACCEPTED -> savedQuizKeys.setItems(DatabaseRequest.getSavedQuizKeys(Account.getUser()));

                case NO_CONTENT -> userNotifier.setText(AlertText.EXTERNAL_ERROR).display();

                case NO_CONNECTION -> userNotifier.setText(AlertText.NO_CONNECTION).display();

            }

        } catch (Exception e) {

            e.printStackTrace();
            userNotifier.setText(AlertText.INTERNAL_ERROR).display();

        }

        Account.setQuiz(null);

    }


}

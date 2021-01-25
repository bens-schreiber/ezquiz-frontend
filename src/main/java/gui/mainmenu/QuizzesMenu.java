package gui.mainmenu;

import gui.etc.Account;
import gui.etc.FXHelper;
import gui.popup.enter.EnterNotifier;
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

public class QuizzesMenu implements Initializable {

    @FXML
    TableView<Account.Quiz> savedQuizKeys;

    @FXML
    TableColumn<Account.Quiz, String> nameColumn, classColumn, keyColumn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {


        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        classColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOwner()));
        keyColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getKey())));

        try {

            savedQuizKeys.setItems(DatabaseRequest.getSavedQuizzes());

        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        } catch (JSONException ignored) {
        }

    }

    public void tableViewClicked() {

        Account.setQuiz(savedQuizKeys.getSelectionModel().getSelectedItem());

    }

    public void addKeyClicked() {

        try {

            EnterNotifier enter = new EnterNotifier(FXHelper.Window.ENTERKEY).display();
            if (enter.isCodeEntered()) {

                DatabaseRequest.postQuizKey(Account.getUser());

                savedQuizKeys.setItems(DatabaseRequest.getSavedQuizzes());

            }

        } catch (IOException | InterruptedException | JSONException e) {
            e.printStackTrace();
        }

    }

    public void removeKeyClicked() {
        savedQuizKeys.getItems().remove(savedQuizKeys.getSelectionModel().getSelectedItem());
        //todo: remove from db
        Account.setQuiz(null);
    }


}

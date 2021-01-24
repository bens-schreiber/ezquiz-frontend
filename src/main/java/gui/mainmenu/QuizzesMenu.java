package gui.mainmenu;

import gui.etc.Account;
import gui.etc.FXHelper;
import gui.popup.enter.EnterNotifier;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class QuizzesMenu implements Initializable {

    @FXML
    TableView<Account.Quiz> savedQuizKeys;

    @FXML
    TableColumn<Account.Quiz, String> nameColumn, classColumn, keyColumn;

    private final ObservableList<Account.Quiz> quizzes = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getName()));
        classColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOwner()));
        keyColumn.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getKey())));
    }

    public void addKeyClicked() {

        try {

            EnterNotifier enter = new EnterNotifier(FXHelper.Window.ENTERKEY).display();
            if (enter.isCodeEntered()) {
                quizzes.add(Account.getQuiz());
                savedQuizKeys.setItems(quizzes);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void removeKeyClicked() {
        savedQuizKeys.getItems().remove(savedQuizKeys.getSelectionModel().getSelectedItem());
        Account.setQuiz(null);
    }


}

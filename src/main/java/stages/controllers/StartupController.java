package stages.controllers;

import etc.Constants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.json.JSONException;
import quiz.Quiz;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StartupController implements Initializable {
    @FXML
    private TextField questionAmountField;
    @FXML
    private ChoiceBox<String> subjectList;
    @FXML
    private ChoiceBox<String> typeList;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ObservableList<String> subjectListItems = FXCollections
                .observableArrayList("Math", "Science", "English", "History", "VideoGames");

        subjectList.setItems(subjectListItems);

        ObservableList<String> typeListItems = FXCollections
                .observableArrayList("input", "t_f", "checkbox", "multiple");

        typeList.setItems(typeListItems);
    }

    public void onStartButton(MouseEvent mouseEvent) throws IOException, JSONException {
        int questionAmount;

        if (!questionAmountField.getText().equals("")) {
            questionAmount = Integer.parseInt(questionAmountField.getText());
        } else questionAmount = Constants.defaultQuestionAmount;


        if (subjectList.getValue() != null && typeList.getValue() != null) {
            Quiz.loadQuestions(subjectList.getValue(), typeList.getValue(), questionAmount);
        } else if (subjectList.getValue() != null) {
            Quiz.loadQuestions(questionAmount, subjectList.getValue().toLowerCase());
        } else if (typeList.getValue() != null) {
            Quiz.loadQuestions(typeList.getValue(), questionAmount);
        } else {
            Quiz.loadQuestions(questionAmount);
        }

        exit(mouseEvent);
        displayTest();

    }

    private void displayTest() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/test.fxml"));
        Scene scene = new Scene(root, 1980, 1080);
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.show();
    }

    private void exit(MouseEvent mouseEvent) {
        Node source = (Node) mouseEvent.getSource();
        Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }
}

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
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.json.JSONException;
import quiz.QuizController;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class StartupController implements Initializable {
    @FXML
    private TextField questionAmountField;
    @FXML
    private ChoiceBox<String> subjectList;
    @FXML
    private ChoiceBox<String> typeList;
    @FXML
    private CheckBox calcPref;
    @FXML
    private CheckBox drawPref;
    @FXML
    private CheckBox notePadPref;


    @Override
    public void initialize(URL location, ResourceBundle resources) {


        ObservableList<String> subjectListItems = FXCollections
                .observableArrayList("Math", "Science", "English", "History", "VideoGames");
        ObservableList<String> typeListItems = FXCollections
                .observableArrayList("input", "t_f", "checkbox", "multiple");

        subjectList.setItems(subjectListItems);
        typeList.setItems(typeListItems);
    }

    public void onStartButton(MouseEvent mouseEvent) throws IOException, JSONException {
        int questionAmount;

        if (!questionAmountField.getText().equals("")) {
            questionAmount = Integer.parseInt(questionAmountField.getText());
        } else questionAmount = Constants.defaultQuestionAmount;

        for (CheckBox checkBox : Arrays.asList(calcPref, notePadPref, drawPref)) {
            QuizController.addPref(checkBox.getText(), checkBox.isSelected());
        }

        QuizController.loadQuestions(questionAmount, subjectList.getValue(), typeList.getValue());
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

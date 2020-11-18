package gui.controllers;

import etc.Constants;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

public class CustomTestController implements Initializable, Exitable {
    @FXML
    private TextField questionAmountField;

    @FXML
    private ChoiceBox<String> subjectList, typeList;

    @FXML
    private CheckBox calcPref, drawPref, notePadPref;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ObservableList<String> subjectListItems = FXCollections
                .observableArrayList("Math", "Science", "English", "History", "VideoGames");

        ObservableList<String> typeListItems = FXCollections
                .observableArrayList("input", "t_f", "checkbox", "multiple");

        subjectList.setItems(subjectListItems); //Establish dropdown menu items

        typeList.setItems(typeListItems); //Establish dropdown menu items
    }

    public void onStartButton(MouseEvent mouseEvent) throws IOException, JSONException {

        int questionAmount;

        if (!questionAmountField.getText().equals("")) {//If questionAmountField has a value

            questionAmount = Integer.parseInt(questionAmountField.getText()); //Set value to questionAmount

        } else {

            questionAmount = Constants.defaultQuestionAmount; //If no value, use the default.

        }

        for (CheckBox checkBox : Arrays.asList(calcPref, notePadPref, drawPref)) {//Put the preferences in

            QuizController.addPref(checkBox.getText(), checkBox.isSelected());

        }

        QuizController.loadQuestions(questionAmount, subjectList.getValue(), typeList.getValue()); //Load question

        exit(mouseEvent); //Close this page

        displayTest(); //Initialize the test.

    }

    private void displayTest() throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/test.fxml")); //Grab the test fxml

        Scene scene = new Scene(root, 1980, 1080);

        Stage stage = new Stage();

        stage.setScene(scene);

        stage.show();
    }

}

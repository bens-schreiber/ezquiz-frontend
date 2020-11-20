package gui.controllers;

import etc.Constants;
import gui.GuiHelper;
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
import javafx.stage.StageStyle;
import org.json.JSONException;
import quiz.QuizManager;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

public class CustomTestController implements Initializable {
    @FXML
    private TextField questionAmountField, testNameField, testTimeField;

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

    //Setup all preferences and begin custom test
    public void onStartButton(MouseEvent mouseEvent) throws IOException, JSONException {

        int questionAmount;

        if (questionAmountField.getText().equals("")) {

            questionAmount = Constants.defaultQuestionAmount; //If no value, use the default.

        } else {//If questionAmountField has a value

            questionAmount = Integer.parseInt(questionAmountField.getText()); //Set value to questionAmount

        }

        if (!testTimeField.getText().equals("")) {

            QuizManager.addPref("seconds", String.valueOf(
                    Integer.parseInt
                            (testTimeField.getText())
                            * 60)
            );

        }

        if (testNameField.getText().equals("")) {

            QuizManager.addPref("Quiz Name", "Custom Exam");

        } else {

            QuizManager.addPref("Quiz Name", testNameField.getText());

        }

        for (CheckBox checkBox : Arrays.asList(calcPref, notePadPref, drawPref)) {//Put addon prefs in

            QuizManager.addPref(checkBox.getText(), String.valueOf(checkBox.isSelected()));

        }


        QuizManager.loadQuestions(questionAmount, subjectList.getValue(), typeList.getValue()); //Load question

        GuiHelper.closeWindow("Custom"); //Close this page

        displayTest(); //Initialize the test.

    }

    private void displayTest() throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/test.fxml")); //Grab the test fxml

        Scene scene = new Scene(root);

        Stage stage = new Stage();

        stage.setAlwaysOnTop(true);

        stage.initStyle(StageStyle.UNDECORATED);

        stage.setScene(scene);

        stage.show();

        GuiHelper.addWindow("Quiz", stage);
    }

}

package gui.controllers.startpage;

import etc.Constants;
import gui.StageHelper;
import gui.popups.ErrorBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import quiz.QuizManager;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

/**
 * Provides methods for ActionEvents on Custom Quiz Page.
 */

public class CustomQuizController implements Initializable {
    @FXML
    private TextField questionAmountField, testNameField, testTimeField;

    @FXML
    private ChoiceBox<String> subjectList, typeList;

    @FXML
    private CheckBox calcPref, drawPref, notePadPref, correctAnswers;

    /**
     * Initial startup method.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ObservableList<String> subjectListItems = FXCollections
                .observableArrayList("None", "math", "science", "english", "history", "videogames");

        ObservableList<String> typeListItems = FXCollections
                .observableArrayList("None", "Written", "True or False", "Checkbox", "Multiple Choice");

        //Establish dropdown menu items
        subjectList.setItems(subjectListItems);
        typeList.setItems(typeListItems);

        //Establish default as none
        typeList.setValue("None");
        subjectList.setValue("None");
    }

    public void onBackButton() {

        try {

            StageHelper.getStages().get("Start")
                    .setScene(new Scene(FXMLLoader.load(getClass().getResource("/start.fxml"))));

        } catch (IOException | NullPointerException e) {
            ErrorBox.display("A page failed to load.", false);
            e.printStackTrace();
        }
    }

    //Setup all preferences and begin custom test
    public void onStartButton() {

        //Handle the questionAmount being empty
        //If no value, use the default.
        int questionAmount = questionAmountField.getText().isEmpty() ?
                Constants.DEFAULT_QUESTION_AMOUNT : Integer.parseInt(questionAmountField.getText());

        //Handle time field being empty
        //30 minutes default
        QuizManager.getPreferences().put("seconds", testTimeField.getText().isEmpty() ?
                "1800" : String.valueOf(Integer.parseInt(testTimeField.getText()) * 60));

        //Handle test name being empty
        //Default name is Custom Exam
        QuizManager.getPreferences().put("Quiz Name", testNameField.getText().isEmpty() ?
                "Custom Exam" : testNameField.getText());

        for (CheckBox checkBox : Arrays.asList(calcPref, notePadPref, drawPref, correctAnswers)) {//Put addon prefs in
            QuizManager.getPreferences().put(checkBox.getText(), String.valueOf(checkBox.isSelected()));
        }

        if (subjectList.getValue().equals("None")) {
            subjectList.setValue(null);
        }

        switch (typeList.getValue()) {

            case "Multiple Choice" -> typeList.setValue("multiple");

            case "True or False" -> typeList.setValue("t_f");

            case "Written" -> typeList.setValue("input");

            case "Checkbox" -> typeList.setValue("checkbox");

            case "None" -> typeList.setValue(null);
        }

        QuizManager.loadQuestions(questionAmount, subjectList.getValue(), typeList.getValue()); //Load question

        StageHelper.closeStage("Start"); //Close this page

        displayTest(); //Initialize the test.

    }

    private void displayTest() {

        try {

            Stage stage = StageHelper.createAndAddStage("Quiz", "/quiz.fxml");

            stage.setAlwaysOnTop(true);
            stage.initStyle(StageStyle.UNDECORATED);

            StageHelper.closeAllStages();
            StageHelper.clearScenes();

            stage.show();

        } catch (IOException | NullPointerException e) {
            ErrorBox.display("A page failed to load.", false);
            e.printStackTrace();
        }
    }

}

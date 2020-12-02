package gui.controllers.startup;

import etc.Constants;
import gui.StageHelper;
import gui.popups.ErrorBox;
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
                .observableArrayList("math", "science", "english", "history", "videogames");

        ObservableList<String> typeListItems = FXCollections
                .observableArrayList("input", "t_f", "checkbox", "multiple");

        subjectList.setItems(subjectListItems); //Establish dropdown menu items

        typeList.setItems(typeListItems); //Establish dropdown menu items
    }

    public void onBackButton() throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/start.fxml")); //Display the start page.
        StageHelper.getStages().get("Start").setScene(new Scene(root));
    }

    //Setup all preferences and begin custom test
    public void onStartButton() {

        int questionAmount;

        if (questionAmountField.getText().isEmpty()) {

            questionAmount = Constants.DEFAULT_QUESTION_AMOUNT; //If no value, use the default.

        } else {//If questionAmountField has a value

            questionAmount = Integer.parseInt(questionAmountField.getText()); //Set value to questionAmount

        }

        if (testTimeField.getText().isEmpty()) {

            QuizManager.getPreferences().put("seconds", "1800");

        } else {
            QuizManager.getPreferences().put("seconds", String.valueOf(Integer.parseInt(testTimeField.getText()) * 60));
        }

        if (testNameField.getText().isEmpty()) {

            QuizManager.getPreferences().put("Quiz Name", "Custom Exam");

        } else {

            QuizManager.getPreferences().put("Quiz Name", testNameField.getText());

        }

        for (CheckBox checkBox : Arrays.asList(calcPref, notePadPref, drawPref, correctAnswers)) {//Put addon prefs in

            QuizManager.getPreferences().put(checkBox.getText(), String.valueOf(checkBox.isSelected()));

        }


        QuizManager.loadQuestions(questionAmount, subjectList.getValue(), typeList.getValue()); //Load question

        StageHelper.closeStage("Start"); //Close this page

        displayTest(); //Initialize the test.

    }

    private void displayTest() {

        try {

            Stage stage = new Stage();
            stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("/quiz.fxml"))));

            stage.setAlwaysOnTop(true);
            stage.initStyle(StageStyle.UNDECORATED);

            StageHelper.closeAllStages();
            StageHelper.clearScenes();

            StageHelper.addStage("Quiz", stage);
            stage.show();

        } catch (IOException | NullPointerException e) {
            ErrorBox.display("A page failed to load.", false);
            e.printStackTrace();
        }
    }

}

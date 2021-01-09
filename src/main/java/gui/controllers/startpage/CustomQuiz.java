package gui.controllers.startpage;

import etc.Constants;
import gui.StageHelper;
import gui.Window;
import gui.popups.ErrorBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import quiz.Preference;
import quiz.QuizManager;
import quiz.questions.Question;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Provides methods for ActionEvents on Custom Quiz Page.
 */

public class CustomQuiz extends StartQuiz implements Initializable {
    @FXML
    private TextField questionAmountField, testNameField, testTimeField;

    @FXML
    private ChoiceBox<String> subjectList, typeList;

    @FXML
    private CheckBox calculator, drawingpad, notepad, showAnswers;


    /**
     * Initial startup method.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ObservableList<String> subjectListItems = FXCollections
                .observableArrayList("None", "Marketing", "Business Math", "Intro To Business", "Network Design");

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

            StageHelper.getStages().get(Window.STARTPAGE).setScene(StageHelper.getScenes().get(Window.STARTPAGE));

        } catch (NullPointerException e) {
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

        //30 minutes default
        if (!testTimeField.getText().isEmpty()) {
            QuizManager.getPreferences().put(Preference.TIME, testTimeField.getText());
        }

        //Handle test name being empty
        //Default name is Custom Exam
        QuizManager.getPreferences().put(Preference.QUIZNAME,
                testNameField.getText().isEmpty() ? "Custom Exam" : testNameField.getText());

        QuizManager.getPreferences().put(Preference.CALCULATOR, String.valueOf(calculator.isSelected()));
        QuizManager.getPreferences().put(Preference.NOTEPAD, String.valueOf(notepad.isSelected()));
        QuizManager.getPreferences().put(Preference.DRAWINGPAD, String.valueOf(drawingpad.isSelected()));
        QuizManager.getPreferences().put(Preference.SHOWANSWERS, String.valueOf(showAnswers.isSelected()));

        Question.Type type;
        type = switch (typeList.getValue()) {
            case "Multiple Choice" -> Question.Type.MULTIPLECHOICE;
            case "Written" -> Question.Type.WRITTEN;
            case "True or False" -> Question.Type.TRUEORFALSE;
            case "Checkbox" -> Question.Type.CHECKBOX;
            default -> null;
        };

        Question.Subject subject;
        subject = switch (subjectList.getValue()) {
            case "Marketing" -> Question.Subject.MARKETING;
            case "Business Math" -> Question.Subject.BUSMATH;
            case "Intro to Business" -> Question.Subject.INTBUS;
            case "Network Design" -> Question.Subject.NETWORKDESIGN;
            default -> null;
        };

        QuizManager.loadQuestions(questionAmount, type, subject);

        StageHelper.closeStage(Window.STARTPAGE); //Close this page

        displayTest(); //Initialize the test.

    }

}

package gui.startmenu;

import gui.PrimaryStageHelper;
import gui.etc.FXHelper;
import gui.etc.Window;
import gui.popup.error.ErrorNotifier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import quiz.Preference;
import quiz.QuestionManager;
import quiz.question.Question;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Provides methods for ActionEvents on Custom Quiz Page.
 */

public class CustomQuiz extends PrimaryStageHelper implements Initializable {

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

        //Make User Input limited
        testNameField.setTextFormatter(new TextFormatter<>(c -> {
            if (c.isContentChange()) {
                if (c.getControlNewText().length() > 20) {
                    c.setText("");
                }
            }
            return c;
        }));
        testTimeField.setTextFormatter(new TextFormatter<>(c -> {
            if (c.isContentChange()) {
                if (c.getControlNewText().length() > 4) {
                    c.setText("");
                }
            }
            return c;
        }));
        questionAmountField.setTextFormatter(new TextFormatter<>(c -> {
            if (c.isContentChange()) {
                if (c.getControlNewText().length() > 2) {
                    c.setText("");
                }
            }
            return c;
        }));
    }

    public void backButtonClicked() {

        try {

            primaryStage.setScene(FXHelper.getScene(Window.STARTPAGE));

        } catch (Exception e) {

            new ErrorNotifier("A page failed to load", true).display();

            e.printStackTrace();

        }
    }

    //Setup all preferences and begin custom test
    public void startButtonClicked() {

        //Attempt to cast user input to usable value, catch exception if not possible.
        try {

            //Handle the questionAmount being empty
            //If no value, use the default.
            int questionAmount = questionAmountField.getText().isEmpty() ? 0 : Integer.parseInt(questionAmountField.getText());

            //30 minutes default
            if (!testTimeField.getText().isEmpty()) {
                Preference.preferences.put(Preference.TIME, testTimeField.getText());
            }

            //Handle test name being empty
            //Default name is Custom Exam
            Preference.preferences.put(Preference.QUIZNAME, testNameField.getText().isEmpty() ? "Custom Exam" : testNameField.getText());
            Preference.preferences.put(Preference.CALCULATOR, String.valueOf(calculator.isSelected()));
            Preference.preferences.put(Preference.NOTEPAD, String.valueOf(notepad.isSelected()));
            Preference.preferences.put(Preference.DRAWINGPAD, String.valueOf(drawingpad.isSelected()));
            Preference.preferences.put(Preference.SHOWANSWERS, String.valueOf(showAnswers.isSelected()));

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


            QuestionManager.loadQuestions(questionAmount, type, subject);
            displayQuiz(false);

        } catch (IllegalArgumentException e) {
            new ErrorNotifier("A quiz failed to be created from the current selection.", false).display();
        }


    }

}

package gui.mainmenu.quizbuilder;

import gui.FXController;
import gui.alert.notification.UserNotifier;
import gui.mainmenu.excel.ExcelReader;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * JavaFX controller for the Quiz Builder
 */
public class QuizBuilderTool implements Initializable {

    @FXML
    Button backButton, nextButton;

    @FXML
    TabPane tabWizard;

    @FXML
    Label displayingPaneLabel;

    private static Stage stage;

    public static void setStage(Stage stage) {
        QuizBuilderTool.stage = stage;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            tabWizard.getTabs().add(new QuestionDisplayTab());
        } catch (IOException e) {
            e.printStackTrace();
        }
        displayPane();

    }

    public void addNewButtonClicked() {

        try {
            tabWizard.getTabs().add(new QuestionDisplayTab());
            nextButtonClicked();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void removeButtonClicked() {

        tabWizard.getTabs().remove(tabWizard.getSelectionModel().getSelectedItem());
        displayPane();


    }

    public void nextButtonClicked() {

        tabWizard.getSelectionModel().selectNext();
        displayPane();

    }

    public void backButtonClicked() {

        tabWizard.getSelectionModel().selectPrevious();
        displayPane();

    }


    public void submitButtonClicked() {

        if (tabWizard.getTabs().stream().allMatch(tab -> ((QuestionDisplayTab) tab).isAnswered())) {

            JSONObject questions = new JSONObject();

            int i = 0;
            for (Tab tab : tabWizard.getTabs()) {
                try {

                    QuestionDisplayTab displayTab = (QuestionDisplayTab) tab;

                    questions.put("obj" + i++, displayTab.getJSONQuestion());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            try {

                ExcelReader excelReader = new ExcelReader(questions);

                //Open a DirectoryChooser to choose where to store the excel
                DirectoryChooser directoryChooser = new DirectoryChooser();
                File selectedDirectory = directoryChooser.showDialog(FXController.getPrimaryStage());

                FileOutputStream outputStream = new FileOutputStream(selectedDirectory.getAbsolutePath() + "/quizbuilderquiz.xlsx");

                excelReader.jsonToExcel().write(outputStream);
                outputStream.close();
            } catch (IOException | JSONException e) {
                e.printStackTrace();
            }

        } else {
            //todo: be more specific on error handle
            new UserNotifier("Some questions are not properly filled out.").display(stage);
        }
    }


    private void displayPane() {
        int displayingPane = tabWizard.getSelectionModel().getSelectedIndex();
        nextButton.setDisable(displayingPane == tabWizard.getTabs().size() - 1);
        backButton.setDisable(displayingPane == 0);
        displayingPaneLabel.setText("#" + (displayingPane + 1) + "/" + tabWizard.getTabs().size());
    }

}

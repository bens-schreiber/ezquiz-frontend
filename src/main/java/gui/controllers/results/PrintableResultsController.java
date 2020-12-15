package gui.controllers.results;

import database.DatabaseRequest;
import etc.BitMap;
import gui.StageHelper;
import gui.popups.ErrorBox;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.DirectoryChooser;
import org.json.JSONException;
import quiz.QuizManager;
import quiz.questions.nodes.QuizNode;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 * Provides methods for ActionEvents on Printable Results Page.
 */

public class PrintableResultsController implements Initializable {

    @FXML
    Label testName, outOfLabel, percentageLabel;

    @FXML
    Button seeQuestionsButton;

    //todo: add chart stats
    @FXML
    LineChart lineChart;

    private BitMap bitMap;

    /**
     * Initial startup method.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Disable button if in preferences
        seeQuestionsButton.setDisable(!Boolean.parseBoolean(QuizManager.getPreferences().get("Show Correct Answers")));

        //Set test name
        testName.setText(testName.getText() + QuizManager.getPreferences().get("Quiz Name"));

        //Check all answers
        QuizManager.checkAnswers();

        //Get the amount of correct answers, get ID's for bitmap storage
        int correctAnswers = 0;
        ArrayList<Integer> ids = new ArrayList<>();
        for (QuizNode quizNode : QuizManager.getQuizNodes()) {

            ids.add(quizNode.getQuestion().getID());

            if (quizNode.isCorrect()) {
                correctAnswers++;
            }
        }

        //Create a bitmap data structure from ids
        bitMap = new BitMap(ids);

        //add how many correct out of possible, percentage, put bitmap to Base64
        outOfLabel.setText(correctAnswers + " out of " + QuizManager.getQuizNodes().size());
        percentageLabel.setText(((double) correctAnswers / (double) QuizManager.getQuizNodes().size() * 100) + "%");

    }


    public void onScreenshotButton() {

        try {
            Scene scene = StageHelper.getScenes().get("PrintableResults");
            WritableImage writableImage = scene.snapshot(null);

            DirectoryChooser directoryChooser = new DirectoryChooser();
            File file = directoryChooser.showDialog(StageHelper.getStages().get("Quiz"));


            ImageIO.write(
                    SwingFXUtils.fromFXImage(writableImage, null),
                    "png", new File(file.getAbsolutePath() + "/screenshot")
            );

        } catch (IOException ex) {

            ErrorBox.display("An error occurred trying to screenshot", false);

        }
    }

    public void onRetakeCodeButton() {
        try {

            String key = DatabaseRequest.uploadTestKey(bitMap.getBitMap());

            if (!key.isEmpty()) {

                Clipboard clipboard = Clipboard.getSystemClipboard();

                ClipboardContent content = new ClipboardContent();

                content.putString(key);

                clipboard.setContent(content);

            }
        } catch (JSONException | IOException | InterruptedException e) {
            e.printStackTrace();
        }


    }

    public void onPrintButton() {

    }

    public void onSeeQuestions() {

        //Try not to reload page if already created
        if (StageHelper.getScenes().containsKey("questionresults")) {

            StageHelper.getStages().get("Quiz").setScene(StageHelper.getScenes().get("questionresults"));

        } else {
            try {

                Parent results = FXMLLoader.load(getClass().getResource("/questionresults.fxml"));
                Scene scene = new Scene(results);
                StageHelper.addScene("questionresults", scene);
                StageHelper.getStages().get("Quiz").setScene(scene);

            } catch (IOException | NullPointerException e) {
                ErrorBox.display("A page failed to load.", false);
                e.printStackTrace();
            }
        }
    }
}

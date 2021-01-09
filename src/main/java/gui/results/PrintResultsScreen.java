package gui.results;

import etc.BitMap;
import etc.Preference;
import gui.etc.FXHelper;
import gui.etc.Window;
import gui.popup.error.ErrorNotifier;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import org.json.JSONException;
import quiz.QuizManager;
import quiz.nodes.QuizNode;
import requests.DatabaseRequest;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class PrintResultsScreen implements Initializable {

    @FXML
    Label testName, outOfLabel, percentageLabel;

    @FXML
    Button seeQuestionsButton;

    @FXML
    BarChart<String, Integer> barChart;

    private BitMap bitMap;

    private static Stage primaryStage;

    public static void setPrimaryStage(Stage primaryStage) {
        PrintResultsScreen.primaryStage = primaryStage;
    }

    /**
     * Initial startup method.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        loadBarChart();

        //Disable button if in preferences
        seeQuestionsButton.setDisable(!Boolean.parseBoolean(QuizManager.getPreferences().get(Preference.SHOWANSWERS)));

        //Set test name
        testName.setText(testName.getText() + QuizManager.getPreferences().get(Preference.QUIZNAME));

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
        outOfLabel.setText(correctAnswers + " out of " + QuizManager.getQuizNodes().length);
        percentageLabel.setText(((double) correctAnswers / (double) QuizManager.getQuizNodes().length * 100) + "%");

    }

    private void loadBarChart() {

        int[] subjs = {0, 0, 0, 0};

        for (QuizNode quizNode : QuizManager.getQuizNodes()) {

            switch (quizNode.getQuestion().getSubject()) {

                case INTBUS -> subjs[0]++;

                case NETWORKDESIGN -> subjs[1]++;

                case MARKETING -> subjs[2]++;

                case BUSMATH -> subjs[3]++;
            }
        }

        XYChart.Series<String, Integer> series = new XYChart.Series<>();
        series.setName("Amount of each question");
        series.getData().add(new XYChart.Data<>("Intro to Business", subjs[0]));
        series.getData().add(new XYChart.Data<>("Network Design", subjs[1]));
        series.getData().add(new XYChart.Data<>("Marketing", subjs[2]));
        series.getData().add(new XYChart.Data<>("Business Math", subjs[3]));

        barChart.getData().add(series);

    }

    public void screenshotButtonClicked() {

        try {
            Scene scene =primaryStage.getScene();
            WritableImage writableImage = scene.snapshot(null);

            DirectoryChooser directoryChooser = new DirectoryChooser();
            File file = directoryChooser.showDialog(primaryStage);

            ImageIO.write(
                    SwingFXUtils.fromFXImage(writableImage, null),
                    "png", new File(file.getAbsolutePath() + "/screenshot")
            );

        } catch (IOException ex) {

            new ErrorNotifier("An error occurred trying to screenshot", false).display();
            ex.printStackTrace();

        }
    }

    public void copyRetakeButtonClicked() {
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

    public void printButtonClicked() {

    }

    public void seeQuestionsButtonClicked() {

        try {
            primaryStage.setScene(FXHelper.getScene(Window.SEERESULTS));
            QuestionResultsScreen.setPrimaryStage(primaryStage);
        } catch (IOException e) {
            new ErrorNotifier("Results could not display.", true).display();
            e.printStackTrace();
        }
    }




}

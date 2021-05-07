package com.benschreiber.gui.windows.results;

import com.benschreiber.etc.Account;
import com.benschreiber.gui.FXController;
import com.benschreiber.gui.FXHelper;
import com.benschreiber.gui.fxobjs.QuestionNode;
import com.benschreiber.requests.DatabaseRequest;
import com.benschreiber.gui.windows.quiz.QuizHelper;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.print.PrinterJob;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

import javax.imageio.ImageIO;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Scene containing results you can print.
 */
public class ResultsController extends FXController implements Initializable {

    @FXML
    Label outOfLabel, percentageLabel;

    @FXML
    VBox questionsVBox;

    /**
     * Initial startup method.
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {

            //Check all answers
            QuizHelper.gradeQuestions();

            //Create classes.question answers display
            int quizNumber = 1;
            int correctAnswers = 0;
            for (QuestionNode questionNode : QuizHelper.getQuestionNodes()) {

                //Make a container for the answered classes.question, add classes.question to it
                VBox answeredQuestion = new VBox(15);
                answeredQuestion.setPrefWidth(1000);

                //Display classes.question number before classes.question, make bold
                Label label = new Label("Question " + quizNumber + ":");
                label.setStyle("-fx-font-weight: bold;");
                answeredQuestion.getChildren().add(label);

                answeredQuestion.getChildren().add(questionNode.getNode());

                //Make un-intractable.
                questionNode.getNode().setFocusTraversable(false);
                questionNode.getNode().setMouseTransparent(true);

                if (questionNode.isCorrect()) {

                    //Add to correct answers for later use.
                    correctAnswers++;

                    //Set background to green with low opacity
                    answeredQuestion.setStyle("-fx-background-color: rgba(86, 234, 99, .5);");

                } else {

                    //Set background to red with low opacity
                    answeredQuestion.setStyle("-fx-background-color: rgba(255, 158, 173, .7);");

                    //Show correct answer only if desired in preferences
                    if (QuizHelper.getPreferences().isShowAnswers()) {

                        answeredQuestion.getChildren().add(new Label("Correct answer: "
                                + questionNode.getAnswer()
                                .toString()
                                .replace("[", "")
                                .replace("]", "")
                        ));
                    }

                }

                //Add container to correct answers container.
                questionsVBox.getChildren().add(answeredQuestion);
                quizNumber++;
            }

            //If any Status aside from Accepted is given, handle the error accordingly.
            errorHandle(DatabaseRequest.postQuizScore(correctAnswers, Account.getUser(), Account.getQuiz()));

            //add how many correct out of possible, percentage, put bitmap to Base64
            outOfLabel.setText(correctAnswers + " out of " + QuizHelper.getQuestionNodes().length);
            percentageLabel.setText(((double) correctAnswers / (double) QuizHelper.getQuestionNodes().length * 100) + "%");

        } catch (Exception e) {
            e.printStackTrace();
            errorHandle();
        }

    }

    public void backToMainMenuClicked() {
        try {

            FXController.setPrimaryStage(FXHelper.getPopupStage(FXHelper.Window.MAIN_MENU, false));

        } catch (Exception e) {
            e.printStackTrace();
            errorHandle();
        }
    }

    public void screenshotButtonClicked() {

        try {

            Scene scene = FXController.getPrimaryStage().getScene();
            WritableImage writableImage = scene.snapshot(null);

            DirectoryChooser directoryChooser = new DirectoryChooser();
            File file = directoryChooser.showDialog(FXController.getPrimaryStage());

            if (file.isFile()) {

                ImageIO.write(
                        SwingFXUtils.fromFXImage(writableImage, null),
                        "png", new File(file.getAbsolutePath() + "/screenshot")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void printButtonClicked() {

        // Create a printer job for the default printer
        PrinterJob job = PrinterJob.createPrinterJob();
        if (job != null && job.showPrintDialog(getPrimaryStage())){
            boolean success = job.printPage(getPrimaryStage().getScene().getRoot());
            if (success) {
                job.endJob();
            }
        }

    }

}



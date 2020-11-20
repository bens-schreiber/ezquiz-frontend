package gui.controllers;

import gui.GuiHelper;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import quiz.questions.nodes.ConfirmBox;
import quiz.QuizManager;
import quiz.questions.NodeHelper;

import java.io.IOException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.IntStream;

/**
 * Main controller for test.
 */

public class TestController implements Initializable, Exitable {

    @FXML
    Button backButton, nextButton, notePadButton, calculatorButton, drawingPadButton;

    @FXML
    Label questionPrompt, questionDirections, quizName, quizTimer, currQuestionLabel;

    @FXML
    VBox questionArea;

    @FXML
    AnchorPane questionPane;

    @FXML
    HBox questionHBox;

    //Drawing pad canvas is apart of test stage, utilized by DrawingPadController
    @FXML
    Canvas paintCanvas;

    private static GraphicsContext gc;


    /**
     * Initial run method
     */

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        //Load all preferences if any
        loadPrefs();

        //Load the individual question buttons
        loadIndividualQuestionButtons();

        //Display the new question.
        displayNewQuestion();

        //Disable back button by default.
        backButton.setDisable(true);

        //Establish canvas properties
        paintCanvas.setDisable(true);
        gc = paintCanvas.getGraphicsContext2D();
        gc.setStroke(Color.WHITE);

        //Color the hbox that is currently selected
        colorCurrentBox();

    }


    /**
     * FXML Button Methods
     */

    //When next button is clicked
    public void onNextButton() {

        if (QuizManager.getCurrNum() < QuizManager.getQuestionAmount() - 1) {

            QuizManager.nextQuestion(); //Change to the next question.

            displayNewQuestion(); //Display the new question.
        }

        //Disable/enable next and back based on position
        backButton.setDisable(QuizManager.getCurrNum() == 0);
        nextButton.setDisable(QuizManager.getCurrNum() + 1 == QuizManager.getQuestionAmount());

        //Color the question button that is currently selected
        colorCurrentBox();

    }

    //When back button is clicked
    public void onBackButton() {

        if (QuizManager.getCurrNum() > 0) {

            QuizManager.prevQuestion(); //Goto previous question.

            displayNewQuestion(); //Load previous question.

        }

        //Disable/enable next and back based on position
        backButton.setDisable(QuizManager.getCurrNum() == 0);
        nextButton.setDisable(QuizManager.getCurrNum() + 1 == QuizManager.getQuestionAmount());

        //Color the question button that is currently selected
        colorCurrentBox();

    }


    //On an individual question clicked
    private void individualQuestionClicked(MouseEvent mouseEvent) {

        //Grab the spot of the question
        int questionSpot = questionHBox.getChildren().indexOf(mouseEvent.getSource());

        //Set current question to the spot
        QuizManager.setCurrNum(questionSpot);

        //Display the current question.
        displayNewQuestion();

        //Color the hbox that is currently selected
        colorCurrentBox();

        backButton.setDisable(questionSpot == 0);
        nextButton.setDisable(questionSpot + 1 == QuizManager.getQuestionAmount());

    }

    //On Flag Question clicked
    public void onFlagQuestion() {

        questionHBox.getChildren().get(QuizManager.getCurrNum()).setStyle(
                "-fx-background-color: #fb8804;"
        );

    }

    //When submit button is clicked
    public void onSubmitButton(MouseEvent mouseEvent) {

        //Determine if any questions have been flagged
        boolean flaggedQuestions = false;
        for (Node node : questionHBox.getChildren()) {

            if (node.getStyle().equals("-fx-background-color: #fb8804;")) {
                flaggedQuestions = true;
                break;
            }

        }

        try {

            //If any questions are flagged
            if (flaggedQuestions) {

                if (ConfirmBox.display("Some questions are flagged. Are you sure you want to submit?")) {

                    exit(mouseEvent); //Exit this page.

                    GuiHelper.closeAll(); //Close all addons

                    displayResults(); //Create results page.

                }

            }

            //If all questions are answered.
            else if (QuizManager.responses.size() == QuizManager.getQuestionAmount()) {

                if (ConfirmBox.display("Are you sure you want to submit?")) {

                    exit(mouseEvent); //Exit this page.

                    GuiHelper.closeAll(); //Close all addons

                    displayResults(); //Create results page.

                }

            }

            //If all questions aren't answered
            else if (ConfirmBox.display("Some answers are unfinished. Are sure you want to submit?")) {

                exit(mouseEvent); //Exit this page.

                GuiHelper.closeAll(); //Close all addons

                displayResults(); //Create results page.

            }
        } catch (IOException ioException) {

            ioException.printStackTrace();

        }

    }


    /**
     * addon button methods
     */

    //When the calculator button is clicked
    public void onCalculatorButton() throws IOException {

        if (!GuiHelper.getOpenedWindows().containsKey("calculator")) { //Make sure one calculator only is open.

            Parent root = FXMLLoader.load(getClass().getResource("/calculator.fxml")); //Grab calculator

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);

            stage.setAlwaysOnTop(true); //Keep notepad on test.
            stage.initStyle(StageStyle.UTILITY);//Get rid of minimize
            stage.resizableProperty().setValue(false);

            GuiHelper.addWindow("calculator", stage); //Add this to current stages

            stage.setOnCloseRequest(e -> { //Make sure if X is pressed it removes from stages
                GuiHelper.closeWindow("calculator");
            });

            stage.show();

        } else {

            //If already open, close all
            GuiHelper.closeWindow("calculator");

        }
    }

    //When the notepad button is clicked
    public void onNotepadButton() throws IOException {

        if (!GuiHelper.getOpenedWindows().containsKey("notepad")) {//Make sure only one notepad is open.

            Parent root = FXMLLoader.load(getClass().getResource("/notepad.fxml")); //Grab calculator

            //Establish scene and stage
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);

            stage.setAlwaysOnTop(true); //Keep notepad on test.
            stage.initStyle(StageStyle.UTILITY);//Get rid of minimize
            stage.resizableProperty().setValue(false); //Make non resizeable

            GuiHelper.addWindow("notepad", stage); //Add this to current stages

            stage.setOnCloseRequest(e -> { //Make sure if X is pressed it removes from stages
                GuiHelper.closeWindow("notepad");
            });

            stage.show();

        } else {

            //If already open, close all.
            GuiHelper.closeWindow("notepad");

        }
    }

    //On drawing button clicked
    public void onDrawingPadButton() throws IOException {

        if (!GuiHelper.getOpenedWindows().containsKey("drawingpad")) { //Make sure only one drawingpad is open

            Parent root = FXMLLoader.load(getClass().getResource("/drawingpad.fxml")); //Grab calculator

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(scene);

            stage.setAlwaysOnTop(true); //Keep pad on test.
            stage.initStyle(StageStyle.UTILITY);
            stage.resizableProperty().setValue(false);

            GuiHelper.addWindow("drawingpad", stage); //Add this to current stages

            stage.setOnCloseRequest(e -> { //Make sure if X is pressed it removes from stages, clears drawings

                paintCanvas.setDisable(true); //Disable canvas so other parts can be used

                gc.clearRect(0, 0, 1920, 1080); //Clear canvas

                GuiHelper.closeWindow("drawingpad");

            });

            paintCanvas.setDisable(false); //Enable the canvas

            stage.show();

        } else {

            //If already open, close all.
            paintCanvas.setDisable(true);

            gc.clearRect(0, 0, 1920, 1080);

            GuiHelper.closeWindow("drawingpad");

        }

    }


    /**
     * void help methods
     */

    //Load all preferences
    private void loadPrefs() {
        if (!QuizManager.preferences.isEmpty()) {

            //Get preferences and apply them, if any.
            notePadButton.setVisible(Boolean.parseBoolean(QuizManager.preferences.get("Notepad")));

            calculatorButton.setVisible(Boolean.parseBoolean(QuizManager.preferences.get("Calculator")));

            drawingPadButton.setVisible(Boolean.parseBoolean(QuizManager.preferences.get("Drawing Pad")));

            quizName.setText(QuizManager.preferences.get("Quiz Name"));

        } else {

            //Set default quiz name.
            quizName.setText("FBLA - Default 5 Question Quiz");

        }
    }

    //Load all question buttons
    private void loadIndividualQuestionButtons() {

        //Establish x amount of buttons in the hbox
        IntStream.range(0, QuizManager.getQuestionAmount())
                .forEach(e -> {

                    Button button = new Button();

                    button.setStyle("-fx-background-color: #c32148;");

                    button.setPrefSize(35, 35);

                    questionHBox.getChildren().add(button);

                    button.setOnMouseClicked(this::individualQuestionClicked);

                });

        questionHBox.setSpacing(4);

    }

    //Display the new question
    private void displayNewQuestion() {

        questionArea.getChildren().clear(); //Clear previous question from questionArea

        questionArea.getChildren().add(NodeHelper.getNodeFromQuestion(QuizManager.getCurrQuestion())); //Add new question

        questionPrompt.setText(QuizManager.getCurrQuestion().getPrompt()); // Set prompt

        questionDirections.setText(QuizManager.getCurrQuestion().getDirections()); //Set directions

        //Change label to current question num / question amount
        currQuestionLabel.setText((QuizManager.getCurrNum() + 1)
                + "/"
                + QuizManager.getQuestionAmount()
        );

        //If the button is not flagged
        questionHBox.getChildren().forEach(button -> {
            if (button.getStyle().equals("-fx-background-color: #c32148;") || button.getStyle().equals("-fx-background-color: #FF0000;")) {
                button.setStyle("-fx-background-color: #c32148;");
            }
        });

    }

    //Display results
    private void displayResults() throws IOException {

        Parent results = FXMLLoader.load(getClass().getResource("/results.fxml")); //Grab results fxml

        Scene scene = new Scene(results);

        Stage stage = new Stage();

        stage.setScene(scene);

        stage.show();

    }

    //Color box to show it is selected
    private void colorCurrentBox() {

        questionHBox.getChildren().get(QuizManager.getCurrNum()).setStyle(
                "-fx-background-color: #FF0000;"
        );
    }


    /**
     * Test Timer
     */
    private void startTimer() {
        //TODO: timer
    }

    /**
     * Paint Canvas Properties
     */


    //When pressing mouse
    public void canvasOnPressed(MouseEvent e) {
        //begin drawing
        gc.beginPath();

        gc.lineTo(e.getX(), e.getY());

        gc.stroke();
    }

    //When dragging mouse
    public void canvasOnDragged(MouseEvent event) {

        gc.lineTo(event.getX(), event.getY());

        gc.stroke();
    }

    public static void changeColor(Color color) {
        gc.setStroke(color);
    }

    public static void changeWidth(Double width) {
        gc.setLineWidth(width);
    }

}

package com.benschreiber.gui.windows.quiz;

import com.benschreiber.etc.Account;
import com.benschreiber.gui.Constants;
import com.benschreiber.gui.FXController;
import com.benschreiber.gui.FXHelper;
import com.benschreiber.gui.fxobjs.QuestionNode;
import com.benschreiber.question.Question;
import com.benschreiber.requests.AnswerJSONRequest;
import com.benschreiber.requests.QuizJSONRequest;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Starts and ends quizzes
 */
public class QuizHelper extends FXController {

    private QuizHelper() {
    }


    //contains the questions, responses, and javafx display
    private static QuestionNode[] questionNodes;
    public static QuestionNode[] getQuestionNodes() {
        return questionNodes;
    }

    //contains the quiz preferences
    private static Preference preferences;
    public static Preference getPreferences() {
        return preferences;
    }

    /**
     * Set Primary stage to Quiz
     * @param randomQuestions if the test should be random questions. False otherwise.
     */
    public static void startQuiz(boolean randomQuestions) throws InterruptedException, IOException, JSONException {

        //Initialize the values questionNodes and preferences
        if (randomQuestions) {
            initializeQuizValues(Constants.RANDOM_QUESTION_AMOUNT);
        } else {
            initializeQuizValues(Constants.MAXIMUM_QUESTION_AMOUNT);
        }

        //Get a Secure Stage from FXHelper, set primary stage to Quiz
        FXController.setPrimaryStage(FXHelper.getSecureStage(FXHelper.Window.QUIZ));

    }

    /**
     * Initialize the values of questionNodes and preferences
     */
    private static void initializeQuizValues(int amount) throws IllegalArgumentException, JSONException, IOException, InterruptedException {
        try {

            //Initialize the request, storing json in the object.
            QuizJSONRequest request = new QuizJSONRequest(Account.getUser())
                    .initializeRequest();

            //Create a pool of classes.question id's in the size of how many questions available, randomize order
            List<Integer> idPool = new LinkedList<>();
            JSONObject questions = request.getQuestions();
            for (int i = 0; i < questions.length(); i++) {
                idPool.add(i);
            }

            //Shuffle the IDs
            Collections.shuffle(idPool);

            //Initialize preferences
            if (validateJSON(request.getPreferences())) {
                preferences = new Preference(
                        Boolean.parseBoolean(request.getPreferences().get("calculator").toString()),
                        Boolean.parseBoolean(request.getPreferences().get("answers").toString()),
                        Boolean.parseBoolean(request.getPreferences().get("drawingpad").toString()),
                        Boolean.parseBoolean(request.getPreferences().get("notepad").toString()),
                        Integer.parseInt(request.getPreferences().get("timer").toString())
                );
            }

            //Initialize questions
            //If the specified amount is greater than possible, or less than 0, make maximum amount possible.
            questionNodes = new QuestionNode[amount > questions.length() || amount <= 0 ? questions.length() : amount];
            int i = 0;
            for (Question question : Question.Factory.arrayFromJSON(questions, questionNodes.length, idPool)) {
                questionNodes[i++] = new QuestionNode(question);
            }

        } catch (Exception e) {

            //Rethrow exception but get rid of any questions that are invalidly loaded.
            questionNodes = null;
            throw e;
        }
    }

    //Validate all required json is there.
    private static boolean validateJSON(JSONObject jsonObject) {
        return jsonObject.has("answers") && jsonObject.has("calculator") && jsonObject.has("notepad")
                && jsonObject.has("drawingpad") && jsonObject.has("timer");
    }



    /**
     * Set primary stage to Results.
     */
    public static void endQuiz() throws IOException {

        FXController.setPrimaryStage(FXHelper.getSecureStage(FXHelper.Window.PRINT_RESULTS));

    }



    /**
     * Grades stored QuestionNodes utilizing QuestionGrader class
     */
    public static void gradeQuestions() throws InterruptedException, IOException, JSONException {

        //Give all questions an answer.
        QuestionGrader.setAnswers(questionNodes, new AnswerJSONRequest(questionNodes).initializeRequest().getJson());

        //Grade the given answers
        for (QuestionNode questionNode : questionNodes) {

            if (questionNode.isAnswered()) {

                List<String> response = questionNode.getResponse();
                List<String> answer = questionNode.getAnswer();

                //user input type might be capitalized or spaced wrong. handle differently
                if (questionNode.getType() == Question.Type.WRITTEN) {
                    //Set to all lowercase and no spaces for minimal input based error
                    response = response.stream()
                            .map(String::toLowerCase)
                            .map(str -> str.replaceAll("\\s", ""))
                            .collect(Collectors.toList());

                    answer = questionNode.getAnswer().stream()
                            .map(String::toLowerCase)
                            .map(str -> str.replaceAll("\\s", ""))
                            .collect(Collectors.toList());
                }

                //Answer may be larger than one, so .containsAll is used
                //Check if answer is correct, if no response then mark wrong.
                System.out.println(answer);
                System.out.println(response);
                questionNode.setCorrect(answer.containsAll(response));

            } else {
                questionNode.setCorrect(false);
            }
        }
    }



    /**
     * Preferences for the quiz, determines what tools are displayed
     */
    public static class Preference {

        private final boolean calculator, showAnswers, drawingPad, notePad;
        private final int time;

        private Preference(boolean calculator, boolean showAnswers, boolean drawingPad, boolean notePad, int time) {
            this.calculator = calculator;
            this.showAnswers = showAnswers;
            this.drawingPad = drawingPad;
            this.notePad = notePad;
            this.time = time;
        }

        public boolean isCalculator() {
            return calculator;
        }

        public boolean isShowAnswers() {
            return showAnswers;
        }

        public boolean isDrawingPad() {
            return drawingPad;
        }

        public boolean isNotePad() {
            return notePad;
        }

        public int getTime() {
            return time;
        }
    }



    /**
     * Assist in the grading of questions
     */
    private static class QuestionGrader {

        /**
         * Set all quizNodes answers
         */
        //todo: why did you think this was a good idea. use binary search instead.
        static void setAnswers(QuestionNode[] questionNodes, JSONObject json) throws JSONException {

            /*
            Because the answers in the rest server are by default sorted by smallest to greatest ID, a pair class is made
            so that the pair list is searched and given correct answers, without effecting the positioning of the questions
            array (which would screw up positioning in Results)
            */

            //Fill pair array with quiz node and original index
            QuestionGrader.Pair[] pairs = IntStream.range(0, questionNodes.length)
                    .mapToObj(i -> new QuestionGrader.Pair(questionNodes[i], i)).toArray(QuestionGrader.Pair[]::new);

            //Sort the pair array so that it may be compared to the already sorted json.
            Arrays.sort(pairs);
            int i = 0;
            for (QuestionGrader.Pair pair : pairs) {

                //Get the correct answer based off the correlating position in the json
                pair.value.setAnswer(answerFromJSON((JSONObject) json.get("obj" + i++)));

                //Assign the value of the pair to its original index, not affecting the positioning of the array.
                questionNodes[pair.index] = pair.value;
            }
        }

        private static List<String> answerFromJSON(JSONObject json) throws JSONException {
            return new ArrayList<>(
                    Arrays.asList(json.get("answer").toString()
                            .split("/")));
        }

        private static class Pair implements Comparable<QuestionGrader.Pair> {

            public final QuestionNode value;
            public final int index;

            public Pair(QuestionNode questionNode, int index) {
                this.value = questionNode;
                this.index = index;
            }

            @Override
            public int compareTo(@NotNull QuizHelper.QuestionGrader.Pair o) {
                return this.value.getID() - o.value.getID();
            }
        }
    }
}

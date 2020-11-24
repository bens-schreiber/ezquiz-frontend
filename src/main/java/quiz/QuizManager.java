package quiz;

import etc.Constants;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;
import quiz.questions.Question;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import quiz.questions.*;
import database.Requests;
import org.json.JSONException;
import quiz.questions.nodes.QuizNode;
import quiz.questions.nodes.QuizNodeFactory;


public class QuizManager {

    //Hashmap of user preferences
    private static final HashMap<String, String> preferences = new HashMap<>();

    //List of QuizNodes that contain the questions, responses, and javafx information
    private static final List<QuizNode> quizNodes = new ArrayList<>();

    //Index of the QuizNodes the quiz is currently on
    private static int currQuestion = 0;


    /**
     * Load questions into QuizManager.questions
     */
    public static void loadQuestions(int amount, @Nullable String subject, @Nullable String type) throws IOException, JSONException {

        //Create a pool of question id's in the specific size of how many questions available
        List<Integer> idPool = IntStream
                .range(1, determineSize(subject, type))
                .boxed()
                .collect(Collectors.toList());

        //Randomize all of the id's positions.
        Collections.shuffle(idPool);

        //Initialize the id variable and request variable
        int id;
        JSONObject requestData;

        for (int i = 0; i < amount; i++) {

            //Remove the element and grab its value
            id = idPool.remove(0);

            if (subject == null && type == null) {//If subject and type are null, request based on all items in db.

                requestData = Requests.getQuestion(id);

            } else if (subject != null && type != null) {//If both subject and type, request for both

                requestData = Requests.getQuestionBySubjectAndType(subject, type, id);

            } else if (subject == null) {//If only type, request for type

                requestData = Requests.getQuestionByType(type, id);

            } else {//If only subject, request for subject

                requestData = Requests.getQuestionBySubject(subject, id);

            }

            Question question = QuestionFactory.questionFromJSON(requestData);
            question.shuffleOptions();

            quizNodes.add(QuizNodeFactory.getNodeFromQuestion(question)); //Make request.

        }


    }


    /**
     * Grab all answers to questions from responses with Requests.getQuestionAnswer
     */
    public static void checkAnswers() throws IOException, JSONException {

        //Iterate through quiz.questions along with hashmap, so it doesn't make unneeded requests
        for (QuizNode quizNode : quizNodes) {

            List<String> response = quizNode.getResponse();

            List<String> answer = QuestionFactory.answerFromJSON(Requests.getQuestionAnswer(quizNode.getQuestion()));

            //set question answer for use in Results
            quizNode.getQuestion().setAnswer(answer);

            //user input type might be capitalized or spaced wrong. handle differently
            if (quizNode.getQuestion().getType().equals("input")) {

                //Set to all lowercase and no spaces for minimal input based error
                response = response.stream()
                        .map(String::toLowerCase)
                        .map(str -> str.replaceAll("\\s", ""))
                        .collect(Collectors.toList());

                answer = answer.stream()
                        .map(String::toLowerCase)
                        .map(str -> str.replaceAll("\\s", ""))
                        .collect(Collectors.toList());
            }

            //Answer may be larger than one, so .containsAll is used
            //Check if answer is correct.
            quizNode.setCorrect(response.containsAll(answer));

        }

    }


    private static int determineSize(String subject, String type) {

        //Determine how large the id pool should be
        if (subject == null && type == null) {

            return Constants.DATABASE_SIZE;

        }

        if (subject != null && type != null) {

            return Constants.SUBJECT_TYPE_QUESTION_AMOUNT;

        }

        if (subject == null) {

            return Constants.TYPE_QUESTION_AMOUNT;

        }

        return Constants.SUBJECT_QUESTION_AMOUNT;

    }

    /**
     * Getters
     */
    public static int getCurrNum() {

        return currQuestion;

    }

    public static HashMap<String, String> getPreferences() {

        return preferences;
    }

    public static List<QuizNode> getQuestions() {

        return quizNodes;
    }

    public static QuizNode getCurrNode() {

        return quizNodes.get(currQuestion);
    }

    public static Question getCurrQuestion() {

        return quizNodes.get(currQuestion).getQuestion();

    }

    public static boolean allResponded() {
        for (QuizNode quizNode : quizNodes) {
            return quizNode.getResponse().isEmpty();
        }
        return false;
    }


    /**
     * Setters
     */

    public static void nextQuestion() {

        currQuestion++;

    }

    public static void prevQuestion() {

        currQuestion--;

    }

    public static void setCurrNum(int num) {

        currQuestion = num;

    }

}


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



public class QuizManager {

    private static final HashMap<String, String> preferences = new HashMap<>();

    static List<List<String>> responses = new ArrayList<>();

    private static final List<Question> questions = new ArrayList<>();

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

            questions.add(QuestionFactory.questionFromJSON(requestData)); //Make request.

        }

        for (Question question : questions) question.shuffleOptions();

    }


    /**
     * Grab all answers to questions from responses with Requests.getQuestionAnswer
     */
    public static List<Question> checkAnswers() throws IOException, JSONException {

        List<Question> correctQuestions = new ArrayList<>();

        //Iterate through quiz.questions along with hashmap, so it doesn't make unneeded requests
        int numberInQuiz = 0;
        for (List<String> response : responses) {

            //Request for the answer based off the correlating position in questions, get id
            List<String> answer = QuestionFactory.answerFromJSON(
                    Requests.getQuestionAnswer(questions.get(numberInQuiz)));

            //user input type might be capitalized or spaced wrong. handle differently
            if (questions.get(numberInQuiz).getType().equals("4")) {

                userInputAnswerHandle(response, answer, correctQuestions, numberInQuiz);

                //Answer may be larger than one, so .containsAll is used
            } else if (answer.containsAll(response)) {

                //Give the Question an answer value, for use in Results.
                questions.get(numberInQuiz).setAnswer(answer);

                correctQuestions.add(questions.get(numberInQuiz));

            }

            numberInQuiz++;

        }

        return correctQuestions;

    }

    private static void userInputAnswerHandle(List<String> response, List<String> answer, List<Question> correctQuestions, int quizQuestNum) {

        //Set to all lowercase and no spaces for minimal input based error
        response = response
                .stream()
                .map(String::toLowerCase)
                .map(str -> str.replaceAll("\\s", ""))
                .collect(Collectors.toList());

        //TODO: Change answers in db to lowercase and no spaces
        answer = answer
                .stream()
                .map(String::toLowerCase)
                .map(str -> str.replaceAll("\\s", ""))
                .collect(Collectors.toList());

        //Answer may be larger than one, so .containsAll is used
        if (answer.containsAll(response)) {

            questions.get(quizQuestNum).setAnswer(answer);

            correctQuestions.add(questions.get(quizQuestNum));

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

    public static Question getCurrQuestion() {

        return questions.get(currQuestion);

    }

    public static int getQuestionAmount() {

        return questions.size();

    }

    public static Question getQuestion(int index) {
        return questions.get(index);
    }


    /**
     * Setters
     */

    public static void addPref(String pref, String val) {

        preferences.put(pref, val);

    }

    public static void nextQuestion() {

        currQuestion++;

    }

    public static void prevQuestion() {

        currQuestion--;

    }

    public static void setCurrNum(int num) {

        currQuestion = num;

    }

    public static void addResponse(int index, List<String> response) {

        responses.add(index, response);

    }

    public static void removeResponse(int index) {
        responses.remove(index);
    }

    public static List<List<String>> getResponses() {
        return responses;
    }

    public static HashMap<String, String> getPreferences() {
        return preferences;
    }

}


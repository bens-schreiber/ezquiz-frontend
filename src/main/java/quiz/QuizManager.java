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

    public static final HashMap<String, String> preferences = new HashMap<>();

    public static LinkedHashMap<Integer, List<String>> responses = new LinkedHashMap<>();

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
            System.out.println(id);

            if (subject == null && type == null) {//If subject and type are null, request based on all items in db.

                requestData = Requests.getQuestion(id);

            } else if (subject != null && type != null) {//If both subject and type, request for both

                requestData = Requests.getQuestionBySubjectAndType(subject, type, id);

            } else if (subject == null) {//If only type, request for type

                requestData = Requests.getQuestionByType(type, id);

            } else {//If only subject, request for subject

                requestData = Requests.getQuestionBySubject(subject, id);

            }

            questions.add(QuestionBuilder.questionFromJSON(requestData)); //Make request.

        }

        for (Question question : questions) question.shuffleOptions();
    }


    /**
     * Grab all answers to questions from responses with Requests.getQuestionAnswer
     */
    public static List<Question> checkAnswers() throws IOException, JSONException {

        List<Question> correctQuestions = new ArrayList<>();

        //Iterate through quiz.questions along with hashmap, so it doesn't make unneeded requests
        int quizQuestNum = 0;

        for (Map.Entry<Integer, List<String>> entry : responses.entrySet()) {

            //Grab response from responses
            List<String> response = entry.getValue();

            //Request for the answer based off the id in responses
            List<String> answer = QuestionBuilder.answerFromJSON(Requests.getQuestionAnswer(entry.getKey()));

            //user input type might be capitalized or spaced wrong. handle differently
            if (questions.get(quizQuestNum).getType().equals("4")) {

                userInputAnswerHandle(response, answer, correctQuestions, quizQuestNum);

                //Answer may be larger than one, so .containsAll is used
            } else if (answer.containsAll(response)) {

                //Give the Question an answer value, for use in Results.
                questions.get(quizQuestNum).setAnswer(answer);

                correctQuestions.add(questions.get(quizQuestNum));

            }

            quizQuestNum++;

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

        //TODO: Change answer in db to lowercase and no spaces, this is unneeded
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

            return Constants.dbSize;

        }

        if (subject != null && type != null) {

            return Constants.subjectAndTypeQuestionAmount;

        }

        if (subject == null) {

            return Constants.typeQuestionAmount;

        }

        return Constants.subjectQuestionAmount;

    }

    public static void addResponse(int id, List<String> response) {

        responses.put(id, response);

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

}


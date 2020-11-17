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

import javax.security.auth.Subject;


public class QuizController {
    public static final HashMap<String, Boolean> preferences = new HashMap<>();
    public static LinkedHashMap<Integer, List<String>> responses = new LinkedHashMap<>();
    private static final List<Question> questions = new ArrayList<>();
    private static int currQuestion = 0;


    public static void loadQuestions(int amount, @Nullable String subject, @Nullable String type) throws IOException, JSONException {
        List<Integer> idPool = IntStream.range(0, determineSize(subject, type))
                .boxed()
                .collect(Collectors.toList());
        Collections.shuffle(idPool);

        JSONObject requestData;
        for (int i = 0; i < amount; i++) {
            int id = idPool.get(0);

            if (subject == null && type == null) {
                requestData = Requests.getQuestion(id);
            } else if (subject != null && type != null) {
                requestData = Requests.getQuestionBySubjectAndType(subject, type, id);
            } else if (subject == null) {
                requestData = Requests.getQuestionByType(type, id);
            } else {
                requestData = Requests.getQuestionBySubject(subject, id);
            }

            idPool.remove(0);
            questions.add(QuestionHelper.questionFromJSON(requestData));
        }

        for (Question question : questions) question.shuffleOptions();
    }



    public static List<Question> checkAnswers() throws IOException, JSONException {
        List<Question> correctQuestions = new ArrayList<>();

        int quizQuestNum = 0; //Iterate through quiz.questions along with hashmap, so it doesn't make unneeded requests
        for (Map.Entry<Integer, List<String>> entry : responses.entrySet()) {

            List<String> response = entry.getValue();
            List<String> answer = QuestionHelper.answerFromJSON(Requests.getQuestionAnswer(entry.getKey()));

            //user input type might be capitalized or spaced wrong. handle differently
            if (questions.get(quizQuestNum).getType().equals("4"))
                userInputAnswerHandle(response, answer, correctQuestions, quizQuestNum);


            else if (answer.containsAll(response)) {//Answer may be larger than one, so .containsAll is used
                questions.get(quizQuestNum).setAnswer(answer); //Give the Question an Answer, for use in Results.
                correctQuestions.add(questions.get(quizQuestNum));
            }

            quizQuestNum++;
        }

        return correctQuestions;
    }

    private static void userInputAnswerHandle(List<String> response, List<String> answer, List<Question> correctQuestions, int quizQuestNum) {
        response = response.stream()
                .map(String::toLowerCase)
                .map(str -> str.replaceAll("\\s", ""))
                .collect(Collectors.toList());

        answer = answer.stream()
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
        if (subject == null && type == null) return Constants.dbSize;

        if (subject != null && type != null) return Constants.subjectAndTypeQuestionAmount;

        if (subject == null) return Constants.typeQuestionAmount;

        return Constants.subjectQuestionAmount;
    }

    public static void addResponse(int id, List<String> response) {
        responses.put(id, response);
    }

    public static int getCurrNum() {
        return currQuestion;
    }

    public static Question getCurrQuestion() {
        return questions.get(currQuestion);
    }

    public static int getQuestionAmount() {
        return questions.size();
    }

    public static void addPref(String pref, boolean val) {
        preferences.put(pref, val);
    }

    public static void nextQuestion() {
        currQuestion++;
    }

    public static void prevQuestion() {
        currQuestion--;
    }

}


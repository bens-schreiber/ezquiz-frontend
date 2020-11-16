package quiz;

import etc.Constants;
import quiz.questions.Question;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import quiz.questions.*;
import database.Requests;
import org.json.JSONException;

public class Quiz {
    public static LinkedHashMap<Integer, List<String>> responses = new LinkedHashMap<>();
    private static final List<Question> questions = new ArrayList<>();
    private static int currQuestion = 0;


    public static void loadQuestions(int amount) throws IOException, JSONException {
        List<Integer> idPool = new ArrayList<>();
        for (int i = 1; i < Constants.dbSize; i++) {
            idPool.add(i);
        }
        Collections.shuffle(idPool);

        for (int i = 0; i < amount; i++) {
            int id = idPool.get(0);
            idPool.remove(0);
            questions.add(QuestionHelper.questionFromJSON(Requests.getQuestion(id)));
        }
        for (Question question : questions) question.shuffleOptions();
    }

    public static void loadQuestions(int amount, String subject) throws IOException, JSONException {
        List<Integer> idPool = new ArrayList<>();
        for (int i = 0; i < 7; i++) { //TODO: make constant
            idPool.add(i);
        }
        Collections.shuffle(idPool);

        for (int i = 0; i < amount; i++) {
            int id = idPool.get(0);
            idPool.remove(0);
            questions.add(QuestionHelper.questionFromJSON(Requests.getQuestionBySubject(subject, id)));
        }

        for (Question question : questions) question.shuffleOptions();
    }

    public static void loadQuestions(String type, int amount) throws IOException, JSONException { //this is dumb
        List<Integer> idPool = new ArrayList<>();
        for (int i = 0; i < 3; i++) { //TODO: make constant
            idPool.add(i);
        }

        for (int i = 0; i < amount; i++) {
            int id = idPool.get(0);
            idPool.remove(0);
            questions.add(QuestionHelper.questionFromJSON(Requests.getQuestionBySubject(type, id)));
        }

        for (Question question : questions) question.shuffleOptions();
    }

    public static void loadQuestions(String subject, String type, int amount) throws IOException, JSONException {
        List<Integer> idPool = new ArrayList<>();
        for (int i = 0; i < 2; i++) { //TODO: make constant
            idPool.add(i);
        }

        for (int i = 0; i < amount; i++) {
            int id = idPool.get(0);
            idPool.remove(0);
            questions.add(QuestionHelper.questionFromJSON(Requests.getQuestionBySubjectAndType(subject, type, id)));
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

    public static void nextQuestion() {
        currQuestion++;
    }

    public static void prevQuestion() {
        currQuestion--;
    }

}


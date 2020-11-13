package quiz;

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


    public static void loadQuestions() throws IOException, JSONException {
        loadQuestions(5);
    }

    public static void loadQuestions(int amount) throws IOException, JSONException {
        for (int i = 0; i < amount; i++) {
            Question q = QuestionHelper.questionFromJSON(Requests.getQuestion());
            if (!questions.contains(q)) questions.add(q);

            for (Question question : questions) {
                question.shuffleOptions();
            }
            //Have to shuffle options AFTER instantiation, or cannot check for duplicates.
        }
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


package quiz;

import questions.Question;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import questions.*;
import apis.Requests;
import org.json.JSONException;

public class Quiz {
    public static HashMap<Integer, List<String>> responses = new HashMap<>();
    private static final List<Question> questions = new ArrayList<>();
    private static int currQuestion = 0;


    public static void loadQuestions() throws IOException, JSONException {
        loadQuestions(5);
    }

    public static void loadQuestions(int amount) throws IOException, JSONException {
        for (int i = 0; i < amount; i++) {
            Question q = QuestionBuilder.questionFromJSON(Requests.getQuestion());
            if (!questions.contains(q)) questions.add(q);
        }

    }

    public static List<Question> checkAnswers() throws IOException, JSONException {
        List<Question> correctQuestions = new ArrayList<>();

        int quizQuestNum = 0; //Iterate through questions along with hashmap, so it doesn't make unneeded requests
        for (Map.Entry<Integer, List<String>> entry : responses.entrySet()) {
            Integer id = entry.getKey();
            List<String> response = entry.getValue();
            List<String> answers = QuestionBuilder.answerFromJSON(Requests.getQuestionAnswer(id));

            //user input type questions might be capitalized or spaced wrong. handle differently
            if (questions.get(quizQuestNum).getType().equals("4"))
                userInputAnswerHandle(response, answers, correctQuestions, quizQuestNum);


            else if (answers.containsAll(response)) //Answer may be larger than one, so .containsAll is used
                correctQuestions.add(questions.get(quizQuestNum));

            quizQuestNum++;
        }

        return correctQuestions;
    }

    private static void userInputAnswerHandle(List<String> response, List<String> answers, List<Question> correctQuestions, int quizQuestNum) {
        response = response.stream()
                .map(String::toLowerCase)
                .map(str -> str.replaceAll("\\s", ""))
                .collect(Collectors.toList());

        answers = answers.stream()
                .map(String::toLowerCase)
                .map(str -> str.replaceAll("\\s", ""))
                .collect(Collectors.toList());

        //Answer may be larger than one, so .containsAll is used
        if (answers.containsAll(response)) correctQuestions.add(questions.get(quizQuestNum));
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


package quiz;

import questions.Question;

import java.io.IOException;
import java.util.*;

import questions.*;
import apis.Requests;
import org.json.JSONException;

public class QuizController {
    public static HashMap<Integer, List<String>> responses = new HashMap<>();

    public static List<Question> getQuestions() throws IOException, JSONException {
        List<Question> questions = new ArrayList<>();
//        Question test = new Question("5", "math", Arrays.asList("1,2,3,4".split(",")), "Get number 4", 100);
        questions.add(QuestionBuilder.questionFromJSON(Requests.getQuestion(1)));
        questions.add(QuestionBuilder.questionFromJSON(Requests.getQuestion(2)));
        questions.add(QuestionBuilder.questionFromJSON(Requests.getQuestion(3)));
        questions.add(QuestionBuilder.questionFromJSON(Requests.getQuestion(4)));
        questions.add(QuestionBuilder.questionFromJSON(Requests.getQuestion(5)));
        return questions;

    }

    public static int checkAnswers() throws IOException, JSONException {
        int points = 0;

        for (Map.Entry<Integer, List<String>> entry : responses.entrySet()) {
            Integer id = entry.getKey();
            List<String> response = entry.getValue();

            if (response.size() == 1) {
                if (response.get(0).equals(Requests.getQuestionAnswer(id).get("answer"))) {
                    points++;
                }
            }

            else {
                if (QuestionBuilder.answerFromJSON(Requests.getQuestionAnswer(id))
                        .containsAll(response))
                    points++;
            }
        }
        return points;
    }

    public static void addResponse(int id, List<String> response) {
        responses.put(id, response);
    }


}


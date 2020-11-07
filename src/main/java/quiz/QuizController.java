package quiz;

import questions.Question;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import questions.*;
import apis.Requests;
import org.json.JSONException;

public class QuizController {
    public static HashMap<Integer, String> responses = new HashMap<>();

    public static List<Question> getQuestions() throws IOException, JSONException {
        List<Question> questions = new ArrayList<>();

        questions.add(QuestionBuilder.questionFromJSON(Requests.getQuestion(1)));
        questions.add(QuestionBuilder.questionFromJSON(Requests.getQuestion(2)));
        questions.add(QuestionBuilder.questionFromJSON(Requests.getQuestion(11)));


        return questions;

    }

    public static int checkAnswers() throws IOException, JSONException {
        int points = 0;

        for (Map.Entry<Integer, String> entry : responses.entrySet()) {
            Integer id = entry.getKey();
            String response = entry.getValue();
            try {
                if (Requests.getQuestionAnswer(id).get("answer").equals(response)) points++;

            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println(points);
        return points;
    }

    public static void addResponse(int id, String response) {
        responses.put(id,response);
    }


}


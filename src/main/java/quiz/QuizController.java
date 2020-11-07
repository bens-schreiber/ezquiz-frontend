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
    public static HashMap<Integer, String> responses = new HashMap<Integer, String>();

    public static List<Question> getQuestions() throws IOException, JSONException {
        List<Question> questions = new ArrayList<Question>();

        questions.add(QuestionBuilder.questionFromJSON(Requests.getQuestion(1)));
        return questions;

    }

    public static int checkAnswers() throws IOException, JSONException {
        int points = 0;

        for (Map.Entry<Integer, String> entry : responses.entrySet()) {
            Integer id = entry.getKey();
            String response = entry.getValue();
            try {
                if (Requests.getQuestionAnswer(id).get("answer") == response) points++;

            } catch (JSONException | IOException e) {
                e.printStackTrace();
            }
        }
        return points;
    }

    public static void addResponse(int id, String response) {
        responses.put(id,response);
    }


}

